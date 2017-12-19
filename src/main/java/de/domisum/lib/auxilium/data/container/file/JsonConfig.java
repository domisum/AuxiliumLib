package de.domisum.lib.auxilium.data.container.file;

import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.json.GsonUtil;
import org.apache.commons.lang3.Validate;

import java.io.File;

public abstract class JsonConfig
{

	// INIT
	@API public static <T extends JsonConfig> T load(File file, Class<T> tClass)
	{
		String fileContent = FileUtil.readString(file);
		return parse(fileContent, tClass);
	}

	@API public static <T extends JsonConfig> T parse(String json, Class<T> tClass)
	{
		T jsonConfig = GsonUtil.get().fromJson(json, tClass);

		if(jsonConfig == null)
			throw new IllegalArgumentException("empty string to parse to "+tClass.getName()+": '"+json+"'");
		jsonConfig.validate();

		return jsonConfig;
	}


	// VALIDATE
	protected abstract void validate();


	protected void validateString(String toValidate, String fieldName)
	{
		String failMessage = fieldName+" was missing from config";

		Validate.notNull(toValidate, failMessage);
	}

	protected void validatePort(int port)
	{
		Validate.inclusiveBetween(1, 65535, port, "port out of range [1-65535]: "+port);
	}

}
