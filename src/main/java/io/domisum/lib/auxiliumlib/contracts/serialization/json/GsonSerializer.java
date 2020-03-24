package io.domisum.lib.auxiliumlib.contracts.serialization.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.json.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

@API
public class GsonSerializer<T> implements JsonSerializer<T>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// ATTRIBUTES
	private final Gson gson;
	private final Type type;


	// INIT
	@API
	public GsonSerializer(Type type)
	{
		this(GsonUtil.get(), type);
	}

	@API
	public GsonSerializer(Gson gson, Type type)
	{
		this.gson = gson;
		this.type = type;
	}


	// SERIALIZER
	@API
	@Override
	public String serialize(T object)
	{
		return gson.toJson(object);
	}

	@API
	@Override
	public T deserialize(String objectString)
	{
		try
		{
			return gson.fromJson(objectString, type);
		}
		catch(JsonSyntaxException e)
		{
			logger.error("failed to deserialize {}", objectString, e);
			throw e;
		}
	}

}
