package io.domisum.lib.auxiliumlib.datacontainers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class Reserved<T>
	implements AutoCloseable
{
	
	@Getter
	private final T reserved;
	private final Consumer<T> onClose;
	
	
	// STATUS
	@Override
	public void close()
	{
		onClose.accept(reserved);
	}
	
}
