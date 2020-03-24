package io.domisum.lib.auxiliumlib.contracts.serialization.json;

import com.google.gson.JsonSyntaxException;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.json.GsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

@API
@RequiredArgsConstructor
public class TypeJsonSerializer<T> implements JsonSerializer<T>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// ATTRIBUTES
	private final Type type;


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
			return GsonUtil.get().fromJson(objectString, type);
		}
		catch(JsonSyntaxException e)
		{
			logger.error("failed to deserialize {}", objectString, e);
			throw e;
		}
	}

}
