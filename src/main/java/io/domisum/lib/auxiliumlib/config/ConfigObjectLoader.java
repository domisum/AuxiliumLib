package io.domisum.lib.auxiliumlib.config;

import com.google.gson.JsonParseException;
import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import io.domisum.lib.auxiliumlib.util.file.FileUtil;
import io.domisum.lib.auxiliumlib.util.file.FileUtil.FileType;
import io.domisum.lib.auxiliumlib.util.java.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
		return OBJECT_NAME()+"s";
	}
	
	
	// LOADING
	public ConfigObjectRegistry<T> load(File configDirectory)
			throws InvalidConfigException
	{
		logger.info("Loading {}...", OBJECT_NAME_PLURAL());
		
		String fileExtension = FILE_EXTENSION();
		if(fileExtension.startsWith("."))
			fileExtension = fileExtension.substring(1);
		
		var configObjectsById = new HashMap<String,T>();
		for(var file : FileUtil.listFilesRecursively(configDirectory, FileType.FILE))
			if(fileExtension.equalsIgnoreCase(FileUtil.getCompositeExtension(file)))
			{
				T configObject = loadConfigObjectFromFile(file);
				
				if(configObjectsById.containsKey(configObject.getId()))
					throw new InvalidConfigException(PHR.r("Duplicate config object id '{}'. Duplicate file: {}",
							configObject.getId(), file));
				configObjectsById.put(configObject.getId(), configObject);
			}
			else
				throw new InvalidConfigException(PHR.r(
						"Config directory of {} contains file with wrong extension: '{}' (expected extension: '{}')",
						OBJECT_NAME_PLURAL(), file.getName(), fileExtension));
		
		if(configObjectsById.isEmpty())
			logger.info("(There are no {})", OBJECT_NAME_PLURAL());
		
		var configObjectIds = configObjectsById.keySet();
		logger.info("...Loading {} done, loaded {}: [{}]",
				OBJECT_NAME_PLURAL(), configObjectsById.size(), StringUtil.collectionToString(configObjectIds, ", "));
		
		return new ConfigObjectRegistry<>(configObjectsById);
	}
	
	private T loadConfigObjectFromFile(File file)
			throws InvalidConfigException
	{
		String fileContent = FileUtil.readString(file);
		try
		{
			return createConfigObject(file, fileContent);
		}
		catch(JsonParseException|InvalidConfigException e)
		{
			String message = PHR.r("Invalid configuration of {} from file '{}'", OBJECT_NAME(), file.getName());
			throw new InvalidConfigException(message, e);
		}
	}
	
	private T createConfigObject(File file, String fileContent)
			throws InvalidConfigException
	{
		var configObject = deserialize(fileContent);
		
		String id = FileUtil.getNameWithoutCompositeExtension(file);
		ReflectionUtil.injectValue(configObject, "id", id);
		for(var dependency : getDependenciesToInject().entrySet())
			ReflectionUtil.injectValue(configObject, dependency.getKey(), dependency.getValue());
		
		configObject.validate();
		
		logger.info("Loaded {} {}", OBJECT_NAME(), configObject);
		return configObject;
	}
	
	
	// CREATE
	protected abstract T deserialize(String configContent)
			throws InvalidConfigException;
	
	protected abstract Map<Class<?>,Object> getDependenciesToInject();
	
}
