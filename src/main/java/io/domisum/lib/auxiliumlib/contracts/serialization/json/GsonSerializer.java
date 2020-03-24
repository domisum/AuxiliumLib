package io.domisum.lib.auxiliumlib.contracts.serialization.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.json.GsonUtil;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor
public class GsonSerializer<T>
		implements JsonSerializer<T>
{
	
	// ATTRIBUTES
	private final Gson gson;
	private final Class<T> clazz;
	
	
	// INIT
	@API
	public GsonSerializer(Class<T> clazz)
	{
		this(GsonUtil.get(), clazz);
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
			return gson.fromJson(objectString, clazz);
		}
		catch(JsonSyntaxException e)
		{
			throw new JsonSyntaxException("failed to deserialize:\n"+objectString, e);
		}
	}
	
}
