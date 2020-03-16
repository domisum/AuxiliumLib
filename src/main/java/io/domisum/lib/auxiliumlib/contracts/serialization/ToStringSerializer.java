package io.domisum.lib.auxiliumlib.contracts.serialization;

public interface ToStringSerializer<T>
{

	String serialize(T object);

	T deserialize(String objectString);

}
