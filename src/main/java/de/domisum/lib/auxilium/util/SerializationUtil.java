package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SerializationUtil
{

	// SERIALIZATION
	@API public static <T extends Serializable> byte[] serialize(T object)
	{
		return SerializationUtils.serialize(object);
	}

	@API public static <T extends Serializable> T deserialize(byte[] serialized)
	{
		return SerializationUtils.deserialize(serialized);
	}

}
