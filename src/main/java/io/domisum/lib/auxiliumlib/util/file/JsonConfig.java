package io.domisum.lib.auxiliumlib.util.file;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.annotations.InitByDeserialization;
import io.domisum.lib.auxiliumlib.exceptions.InvalidConfigurationException;
import io.domisum.lib.auxiliumlib.util.json.GsonUtil;

import java.io.File;
import java.util.Map;

@API
public interface JsonConfig
{
	
	// INIT
	@API
	static <T extends JsonConfig> T load(File file, Class<T> tClass)
			throws InvalidConfigurationException
	{
		String fileContent = FileUtil.readString(file);
		return parse(fileContent, tClass);
	}
	
	@API
	static <T extends JsonConfig> T parse(String json, Class<T> tClass)
			throws InvalidConfigurationException
	{
		T jsonConfig = GsonUtil.get().fromJson(json, tClass);
		
		if(jsonConfig == null)
			throw new IllegalArgumentException("empty string to parse to "+tClass.getName()+": '"+json+"'");
		try
		{
			jsonConfig.validate();
		}
		catch(InvalidConfigurationException|RuntimeException e)
		{
			throw new InvalidConfigurationException("Invalid settings in "+tClass.getSimpleName(), e);
		}
		
		return jsonConfig;
	}
	
	
	// VALIDATE
	@InitByDeserialization
	void validate()
			throws InvalidConfigurationException;
	
	@API
	default <T> void validateContainsKey(Map<T,?> map, T key, String mapName)
			throws InvalidConfigurationException
	{
		if(!map.containsKey(key))
			throw new InvalidConfigurationException(mapName+" has to contain key "+key+", but didn't");
	}
	
	@API
	default void validateString(String toValidate, String fieldName)
			throws InvalidConfigurationException
	{
		if(toValidate == null)
			throw new InvalidConfigurationException(fieldName+" was missing from config");
	}
	
	@API
	default void validatePort(int port, String portName)
			throws InvalidConfigurationException
	{
		final int MAX_PORT_VALUE = 65535;
		
		if(port < 1 || port > MAX_PORT_VALUE)
			throw new InvalidConfigurationException("port "+portName+" out of range [1-"+MAX_PORT_VALUE+"]: "+port);
	}
	
}
