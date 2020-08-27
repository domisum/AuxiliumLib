package io.domisum.lib.auxiliumlib.contracts.serdes;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public interface StringSerdes<T>
{
	
	@API
	String serialize(T object);
	
	@API
	T deserialize(String objectString);
	
}
