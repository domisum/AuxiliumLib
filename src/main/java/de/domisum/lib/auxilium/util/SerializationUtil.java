package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SerializationUtil
{

	// SERIALIZATION
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

}
