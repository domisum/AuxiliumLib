package de.domisum.lib.auxilium.async;

public interface SimpleFuture<T>
{

	T get();

	boolean isDone();

}
