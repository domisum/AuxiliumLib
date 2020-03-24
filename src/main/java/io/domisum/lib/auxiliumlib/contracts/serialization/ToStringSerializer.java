package io.domisum.lib.auxiliumlib.contracts.serialization;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public interface ToStringSerializer<T>
{
	
	@API
	String serialize(T object);
	
	@API
	T deserialize(String objectString);
	
}
