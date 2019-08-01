package de.domisum.lib.auxilium.contracts.serialization;

import com.google.gson.JsonSyntaxException;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.json.GsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@API
@RequiredArgsConstructor
public class BasicJsonSerializer<T> implements JsonSerializer<T>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// ATTRIBUTES
	private final Class<T> classToSerialize;


	// SERIALIZE
	@Override
	public String serialize(T object)
	{
		return GsonUtil.get().toJson(object);
	}

	@Override
	public T deserialize(String objectString)
	{
		try
		{
			return GsonUtil.get().fromJson(objectString, classToSerialize);
		}
		catch(JsonSyntaxException e)
		{
			logger.error("failed to deserialize {}", objectString, e);
			throw e;
		}
	}

}
