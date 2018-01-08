package de.domisum.lib.auxilium.contracts.strategy;

public interface Strategy<T>
{

	boolean doesApplyTo(T object);

}
