package io.domisum.lib.auxiliumlib.contracts.source.optional;

public interface OptionalStorage<K, V>
	extends OptionalSource<K, V>
{
	
	void store(V element);
	
}
