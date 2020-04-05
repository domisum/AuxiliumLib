package io.domisum.lib.auxiliumlib.config;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.annotations.InitByDeserialization;
import io.domisum.lib.auxiliumlib.util.GsonUtil;
import io.domisum.lib.auxiliumlib.util.file.FileUtil;

import java.io.File;

@API
public interface JsonConfig
{
	
	// INIT
	@API
	static <T extends JsonConfig> T load(File file, Class<T> tClass)
			throws InvalidConfigException
	{
		String fileContent = FileUtil.readString(file);
		return parse(fileContent, tClass);
	}
	
	@API
	static <T extends JsonConfig> T parse(String json, Class<T> tClass)
			throws InvalidConfigException
	{
		T jsonConfig = GsonUtil.get().fromJson(json, tClass);
		
		if(jsonConfig == null)
			throw new IllegalArgumentException("empty string to parse to "+tClass.getName()+": '"+json+"'");
		try
		{
			jsonConfig.validate();
		}
		catch(InvalidConfigException|RuntimeException e)
		{
			throw new InvalidConfigException("Invalid settings in "+tClass.getSimpleName(), e);
		}
		
		return jsonConfig;
	}
	
	
	// VALIDATE
	@InitByDeserialization
	void validate()
			throws InvalidConfigException;
	
}
