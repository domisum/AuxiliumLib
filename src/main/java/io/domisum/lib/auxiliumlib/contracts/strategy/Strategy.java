package io.domisum.lib.auxiliumlib.contracts.strategy;

public interface Strategy<T>
{

	boolean doesApplyTo(T object);

}
