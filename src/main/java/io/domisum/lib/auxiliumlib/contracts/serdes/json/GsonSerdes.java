package io.domisum.lib.auxiliumlib.contracts.serdes.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.GsonUtil;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor
public class GsonSerdes<T>
	implements JsonSerdes<T>
{
	
	// ATTRIBUTES
	private final Gson gson;
	private final Class<T> clazz;
	
	
	// INIT
	@API
	public GsonSerdes(Class<T> clazz)
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
