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
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@API
public abstract class ConfigObjectLoader<T extends ConfigObject>
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// CONSTANT METHODS
	protected abstract String OBJECT_NAME();
	
	protected abstract String FILE_EXTENSION();
	
	private String OBJECT_NAME_PLURAL()
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
		
		var configObjects = new HashSet<T>();
		for(var file : FileUtil.listFilesRecursively(configDirectory, FileType.FILE))
			if(fileExtension.equalsIgnoreCase(FileUtil.getCompositeExtension(file)))
				configObjects.add(loadConfigObjectFromFile(file));
			else
				logger.warn("Config directory of {} contains file with wrong extension: '{}' (expected extension: '{}')",
						OBJECT_NAME_PLURAL(), file.getName(), fileExtension);
		
		if(configObjects.isEmpty())
			logger.info("(There are no {})", OBJECT_NAME_PLURAL());
		
		var configObjectIds = configObjects.stream().map(ConfigObject::getId).collect(Collectors.toSet());
		logger.info("...Loading {} done, loaded {}: [{}]", OBJECT_NAME_PLURAL(), configObjects.size(), StringUtil.collectionToString(configObjectIds, ", "));
		
		return new ConfigObjectRegistry<>(configObjects);
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
