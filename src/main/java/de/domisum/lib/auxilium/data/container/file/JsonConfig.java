package de.domisum.lib.auxilium.data.container.file;

import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.annotations.InitByDeserialization;
import de.domisum.lib.auxilium.util.java.exceptions.InvalidConfigurationException;
import de.domisum.lib.auxilium.util.json.GsonUtil;

import java.io.File;

@API
public interface JsonConfig
{

	// INIT
	@API
	static <T extends JsonConfig> T load(File file, Class<T> tClass)
	{
		String fileContent = FileUtil.readString(file);
		return parse(fileContent, tClass);
	}

	@API
	static <T extends JsonConfig> T parse(String json, Class<T> tClass)
	{
		T jsonConfig = GsonUtil.get().fromJson(json, tClass);

		if(jsonConfig == null)
			throw new IllegalArgumentException("empty string to parse to "+tClass.getName()+": '"+json+"'");
		try
		{
			jsonConfig.validate();
		}
		catch(RuntimeException e)
		{
			throw new InvalidConfigurationException("Invalid settings in "+tClass.getSimpleName(), e);
		}

		return jsonConfig;
	}


	// VALIDATE
	@InitByDeserialization
	void validate();


	@API
	default void validateString(String toValidate, String fieldName)
	{
		if(toValidate == null)
			throw new InvalidConfigurationException(fieldName+" was missing from config");
	}

	@API
	default void validatePort(int port)
	{
		final int MAX_PORT_VALUE = 65535;

		if(port < 1 || port > MAX_PORT_VALUE)
			throw new InvalidConfigurationException("port out of range [1-"+MAX_PORT_VALUE+"]: "+port);
	}

}
