package io.domisum.lib.auxiliumlib.contracts.serialization.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.json.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@API
public class GsonSerializer<T> implements JsonSerializer<T>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// ATTRIBUTES
	private final Gson gson;
	private final Class<T> clazz;


	// INIT
	public GsonSerializer(Class<T> clazz)
	{
		this(GsonUtil.get(), clazz);
	}

	public GsonSerializer(Gson gson, Class<T> clazz)
	{
		this.gson = gson;
		this.clazz = clazz;
	}


	// SERIALIZE
	@Override
	public String serialize(T object)
	{
		return gson.toJson(object);
	}

	@Override
	public T deserialize(String objectString)
	{
		try
		{
			return gson.fromJson(objectString, clazz);
		}
		catch(JsonSyntaxException e)
		{
			logger.error("failed to deserialize {}", objectString, e);
			throw e;
		}
	}

}
