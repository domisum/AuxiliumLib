package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.json.GsonUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	public static <T> T deserialize(byte[] serialized)
	{
		try(ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serialized)))
		{
			// noinspection unchecked
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
		String jsonString = GsonUtil.getPretty().toJson(object);
		byte[] jsonByteArray = jsonString.getBytes(StandardCharsets.UTF_8);

		return jsonByteArray;
	}

	@API
	public static <T> T deserializeFromJsonString(byte[] jsonByteArray, Class<T> clazz)
	{
		String jsonString = new String(jsonByteArray, StandardCharsets.UTF_8);
		T object = GsonUtil.getPretty().fromJson(jsonString, clazz);

		return object;
	}

}
