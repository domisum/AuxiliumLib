package io.domisum.lib.auxiliumlib.contracts.source.safe;

public interface SafeSource<K, V>
{
	
	V get(K key);
	
}
