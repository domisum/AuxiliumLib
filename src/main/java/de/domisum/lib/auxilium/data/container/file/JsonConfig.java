package de.domisum.lib.auxilium.data.container.file;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

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
		GsonBuilder builder = new GsonBuilder();
		for(Pair<Class<Object>, JsonDeserializer<Object>> pair : getDeserializers(tClass))
			builder.registerTypeAdapter(pair.getKey(), pair.getValue());


		T jsonConfig = builder.create().fromJson(json, tClass);

		if(jsonConfig == null)
			throw new IllegalArgumentException("empty string to parse to "+tClass.getName()+": '"+json+"'");
		jsonConfig.validate();

		return jsonConfig;
	}

	@SuppressWarnings({"JavaReflectionInvocation", "unchecked"})
	private static <T, C> Collection<Pair<Class<T>, JsonDeserializer<T>>> getDeserializers(Class<C> clazz)
	{
		try
		{
			Constructor<?> constructor = clazz.getConstructor(clazz);
			Object jsonConfig = constructor.newInstance();
			Method method = clazz.getDeclaredMethod("getDeserializers");

			return (Collection<Pair<Class<T>, JsonDeserializer<T>>>) method.invoke(jsonConfig);
		}
		catch(NoSuchMethodException|IllegalAccessException|InstantiationException|InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	@API protected Collection<Pair<Class<?>, JsonDeserializer<?>>> getDeserializers()
	{
		return new ArrayList<>();
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
