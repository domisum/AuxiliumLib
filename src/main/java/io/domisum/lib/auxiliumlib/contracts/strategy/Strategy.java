package io.domisum.lib.auxiliumlib.contracts.strategy;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public interface Strategy<T>
{
	
	@API
	boolean doesApplyTo(T object);
	
}
