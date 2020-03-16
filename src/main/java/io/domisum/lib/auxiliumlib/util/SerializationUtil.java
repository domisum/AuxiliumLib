package io.domisum.lib.auxiliumlib.util;

import com.google.gson.Gson;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import io.domisum.lib.auxiliumlib.util.json.GsonUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SerializationUtil
{

	// JAVA SERIALIZATION
	@API
	public static <T> byte[] serialize(T object)
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try(ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream))
		{
			out.writeObject(object);
		}
		catch(IOException var11)
		{
			throw new SerializationException(var11);
		}

		return byteArrayOutputStream.toByteArray();
	}

	@API
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] serialized)
	{
		try(ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serialized)))
		{

			return (T) in.readObject();
		}
		catch(ClassNotFoundException|IOException var13)
		{
			throw new SerializationException(var13);
		}
	}


	// GSON SERIALIZATION
	@API
	public static byte[] serializeAsJsonString(Object object)
	{
		return serializeAsJsonString(object, GsonUtil.get());
	}

	@API
	public static byte[] serializeAsJsonString(Object object, Gson gson)
	{
		String jsonString = gson.toJson(object);
		byte[] jsonByteArray = jsonString.getBytes(StandardCharsets.UTF_8);

		return jsonByteArray;
	}

	@API
	public static <T> T deserializeFromJsonString(byte[] jsonByteArray, Class<T> clazz)
	{
		Gson gson = GsonUtil.get();
		return deserializeFromJsonString(jsonByteArray, clazz, gson);
	}

	@API
	public static <T> T deserializeFromJsonString(byte[] jsonByteArray, Class<T> clazz, Gson gson)
	{
		String jsonString = new String(jsonByteArray, StandardCharsets.UTF_8);
		T object = gson.fromJson(jsonString, clazz);

		return object;
	}

	@API
	public static <T> T deserializeFromJsonString(byte[] jsonByteArray, Type type)
	{
		return deserializeFromJsonString(jsonByteArray, type, GsonUtil.get());
	}

	@API
	public static <T> T deserializeFromJsonString(byte[] jsonByteArray, Type type, Gson gson)
	{
		String jsonString = new String(jsonByteArray, StandardCharsets.UTF_8);
		T object = gson.fromJson(jsonString, type);

		return object;
	}

}
