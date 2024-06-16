package io.domisum.lib.auxiliumlib.config;

import com.google.gson.JsonParseException;
import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.FileUtil;
import io.domisum.lib.auxiliumlib.util.FileUtil.FileType;
import io.domisum.lib.auxiliumlib.util.ReflectionUtil;
import io.domisum.lib.auxiliumlib.util.StringListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@API
public abstract class ConfigObjectLoader<T extends ConfigObject>
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// CONSTANT METHODS
	protected abstract String OBJECT_NAME();
	
	protected abstract String FILE_EXTENSION();
	
	@API
	protected String OBJECT_NAME_PLURAL()
	{
		return OBJECT_NAME() + "s";
	}
	
	protected boolean LOG_INDIVIDUAL_OBJECT_LOADS()
	{
		return true;
	}
	
	
	// LOADING
	public ConfigObjectRegistry<T> load(File configDirectory)
		throws ConfigException
	{
		logger.info("Loading {}...", OBJECT_NAME_PLURAL());
		
		var files = FileUtil.listFilesRecursively(configDirectory, FileType.FILE);
		var filesOrdered = files.stream()
			.sorted(Comparator.comparing(File::getPath))
			.toList();
		
		var configObjectsById = new HashMap<String, T>();
		for(var file : filesOrdered)
		{
			if(getPathInDir(configDirectory, file).startsWith(".git"))
				continue;
			
			T configObject = readConfigObject(file);
			
			if(configObjectsById.containsKey(configObject.getStringId()))
				throw new ConfigException(PHR.r("Duplicate config object id '{}'. Duplicate file: {}",
					configObject.getId(), file));
			configObjectsById.put(configObject.getStringId(), configObject);
		}
		
		if(configObjectsById.isEmpty())
			logger.info("(There are no {})", OBJECT_NAME_PLURAL());
		
		var configObjectIds = configObjectsById.keySet();
		String message = PHR.r("...Loading {} done, loaded {}", OBJECT_NAME_PLURAL(), configObjectsById.size());
		if(LOG_INDIVIDUAL_OBJECT_LOADS())
			message += PHR.r(": [{}]", StringListUtil.list(configObjectIds));
		logger.info(message);
		
		return new ConfigObjectRegistry<>(configObjectsById);
	}
	
	private String getPathInDir(File directory, File file)
	{
		String configDirPath = directory.getAbsoluteFile().getPath();
		String filePath = file.getAbsoluteFile().getPath();
		
		String pathInDir = filePath.substring(configDirPath.length());
		pathInDir = FileUtil.replaceDelimitersWithForwardSlash(pathInDir);
		if(pathInDir.startsWith("/"))
			pathInDir = pathInDir.substring(1);
		
		return pathInDir;
	}
	
	private T readConfigObject(File file)
		throws ConfigException
	{
		String fileExtension = FileUtil.getCompositeExtension(file);
		if(!getPureFileExtension().equalsIgnoreCase(fileExtension))
			throw new ConfigException(PHR.r(
				"Config directory of {} contains file with wrong extension: '{}' (expected extension: '{}')",
				OBJECT_NAME_PLURAL(), file.getName(), getPureFileExtension()));
		try
		{
			String id = FileUtil.getNameWithoutCompositeExtension(file);
			String fileContent = FileUtil.readString(file);
			return createConfigObject(id, fileContent);
		}
		catch(JsonParseException | ConfigException e)
		{
			String message = PHR.r("Invalid configuration of {} from file '{}'", OBJECT_NAME(), file.getName());
			throw new ConfigException(message, e);
		}
	}
	
	private String getPureFileExtension()
	{
		String fileExtension = FILE_EXTENSION();
		if(fileExtension.startsWith("."))
			fileExtension = fileExtension.substring(1);
		
		return fileExtension;
	}
	
	private T createConfigObject(String id, String fileContent)
		throws ConfigException
	{
		var configObject = deserialize(fileContent);
		
		ReflectionUtil.injectValue(configObject, "id", id);
		for(var dependency : getDependenciesToInject().entrySet())
			ReflectionUtil.injectValue(configObject, dependency.getKey(), dependency.getValue());
		
		configObject.validate();
		
		if(LOG_INDIVIDUAL_OBJECT_LOADS())
			logger.info("Loaded {} {}", OBJECT_NAME(), configObject);
		return configObject;
	}
	
	
	// CREATE
	protected abstract T deserialize(String configContent)
		throws ConfigException;
	
	protected abstract Map<Class<?>, Object> getDependenciesToInject();
	
}
