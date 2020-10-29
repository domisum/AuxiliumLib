package io.domisum.lib.auxiliumlib.contracts.optionalsource;

public interface OptionalStorage<KeyT, T>
	extends OptionalSource<KeyT, T>
{
	
	void store(T element);
	
}
