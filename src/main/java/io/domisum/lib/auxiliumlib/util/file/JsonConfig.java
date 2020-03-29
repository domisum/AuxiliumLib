package io.domisum.lib.auxiliumlib.util.file;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.annotations.InitByDeserialization;
import io.domisum.lib.auxiliumlib.exceptions.InvalidConfigurationException;
import io.domisum.lib.auxiliumlib.util.GsonUtil;

import java.io.File;

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
	
}
