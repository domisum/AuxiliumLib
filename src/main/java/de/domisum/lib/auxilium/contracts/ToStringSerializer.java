package de.domisum.lib.auxilium.contracts;

public interface ToStringSerializer<T>
{

	String serialize(T object);

	T deserialize(String objectString);

}
