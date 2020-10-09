package io.domisum.lib.auxiliumlib.config;

import com.google.gson.JsonParseException;
import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.ReflectionUtil;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import io.domisum.lib.auxiliumlib.util.file.FileUtil;
import io.domisum.lib.auxiliumlib.util.file.FileUtil.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
		return OBJECT_NAME()+"s";
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
		
		String fileExtension = FILE_EXTENSION();
		if(fileExtension.startsWith("."))
			fileExtension = fileExtension.substring(1);
		
		String configDirPath = configDirectory.getAbsoluteFile().getPath();
		
		var configObjectsById = new HashMap<String, T>();
		var files = FileUtil.listFilesRecursively(configDirectory, FileType.FILE);
		var filesOrdered = files.stream()
			.sorted(Comparator.comparing(File::getAbsolutePath))
			.collect(Collectors.toList());
		
		for(var file : filesOrdered)
		{
			String path = file.getAbsoluteFile().getPath();
			String pathInDir = path.substring(configDirPath.length());
			pathInDir = FileUtil.replaceDelimitersWithForwardSlash(pathInDir);
			if(pathInDir.startsWith("/"))
				pathInDir = pathInDir.substring(1);
			
			if(pathInDir.startsWith(".git"))
				continue;
			
			if(fileExtension.equalsIgnoreCase(FileUtil.getCompositeExtension(file)))
			{
				T configObject = loadConfigObjectFromFile(file);
				
				if(configObjectsById.containsKey(configObject.getId()))
					throw new ConfigException(PHR.r("Duplicate config object id '{}'. Duplicate file: {}",
						configObject.getId(), file));
				configObjectsById.put(configObject.getId(), configObject);
			}
			else
				throw new ConfigException(PHR.r(
					"Config directory of {} contains file with wrong extension: '{}' (expected extension: '{}')",
					OBJECT_NAME_PLURAL(), file.getName(), fileExtension));
		}
		
		if(configObjectsById.isEmpty())
			logger.info("(There are no {})", OBJECT_NAME_PLURAL());
		
		var configObjectIds = configObjectsById.keySet();
		logger.info("...Loading {} done, loaded {}: [{}]",
			OBJECT_NAME_PLURAL(), configObjectsById.size(), StringUtil.collectionToString(configObjectIds, ", "));
		
		return new ConfigObjectRegistry<>(configObjectsById);
	}
	
	private T loadConfigObjectFromFile(File file)
		throws ConfigException
	{
		String fileContent = FileUtil.readString(file);
		try
		{
			return createConfigObject(file, fileContent);
		}
		catch(JsonParseException|ConfigException e)
		{
			String message = PHR.r("Invalid configuration of {} from file '{}'", OBJECT_NAME(), file.getName());
			throw new ConfigException(message, e);
		}
	}
	
	private T createConfigObject(File file, String fileContent)
		throws ConfigException
	{
		var configObject = deserialize(fileContent);
		
		String id = FileUtil.getNameWithoutCompositeExtension(file);
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
