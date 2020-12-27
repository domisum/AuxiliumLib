package io.domisum.lib.auxiliumlib.contracts.safesource;

public interface SafeSource<K, V>
{
	
	V get(K key);
	
}
