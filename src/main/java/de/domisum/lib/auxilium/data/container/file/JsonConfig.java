package de.domisum.lib.auxilium.data.container.file;

import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.annotations.InitByDeserialization;
import de.domisum.lib.auxilium.util.json.GsonUtil;
import org.apache.commons.lang3.Validate;

import java.io.File;

@API
public interface JsonConfig
{

	// INIT
	@API static <T extends JsonConfig> T load(File file, Class<T> tClass)
	{
		String fileContent = FileUtil.readString(file);
		return parse(fileContent, tClass);
	}

	@API static <T extends JsonConfig> T parse(String json, Class<T> tClass)
	{
		T jsonConfig = GsonUtil.get().fromJson(json, tClass);

		if(jsonConfig == null)
			throw new IllegalArgumentException("empty string to parse to "+tClass.getName()+": '"+json+"'");
		jsonConfig.validate();

		return jsonConfig;
	}


	// VALIDATE
	@InitByDeserialization void validate();


	@API default void validateString(String toValidate, String fieldName)
	{
		if(toValidate == null)
			throw new IllegalArgumentException(fieldName+" was missing from config");
	}

	@API default void validatePort(int port)
	{
		final int MAX_PORT_VALUE = 65535;

		Validate.inclusiveBetween(1, MAX_PORT_VALUE, port, "port out of range [1-"+MAX_PORT_VALUE+"]: "+port);
	}

}
