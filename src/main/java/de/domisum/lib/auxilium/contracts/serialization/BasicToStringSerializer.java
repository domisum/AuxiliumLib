package de.domisum.lib.auxilium.contracts.serialization;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.json.GsonUtil;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor
public class BasicToStringSerializer<T> implements ToStringSerializer<T>
{

	// ATTRIBUTES
	private final Class<T> classToSerialize;


	// SERIALIZE
	@Override public String serialize(T object)
	{
		return GsonUtil.get().toJson(object);
	}

	@Override public T deserialize(String projectString)
	{
		return GsonUtil.get().fromJson(projectString, classToSerialize);
	}

}
