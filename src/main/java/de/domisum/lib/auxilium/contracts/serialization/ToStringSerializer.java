package de.domisum.lib.auxilium.contracts.serialization;

public interface ToStringSerializer<T>
{

	String serialize(T object);

	T deserialize(String objectString);

}
