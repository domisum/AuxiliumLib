package io.domisum.lib.auxiliumlib.work;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@RequiredArgsConstructor
public final class ReservedWork<T>
	implements AutoCloseable
{
	
	@Getter
	private final T work;
	
	// STATUS
	@Getter
	private boolean successful = false;
	@Nullable
	private final Consumer<T> onSuccessful;
	
	@Getter
	private boolean closed = false;
	@Nullable
	private final Consumer<T> onClose;
	
	
	// INIT
	@API
	public static <T> ReservedWork<T> of(T work)
	{
		return new ReservedWork<>(work, null, null);
	}
	
	@API
	public static <T> ReservedWork<T> ofOnSuccessfulOnClose(T work, Consumer<T> onSuccessful, Consumer<T> onClose)
	{
		return new ReservedWork<>(work, onSuccessful, onClose);
	}
	
	
	// STATUS
	public void successful()
	{
		successful = true;
		
		if(onSuccessful != null)
			onSuccessful.accept(work);
	}
	
	@Override
	public void close()
	{
		closed = true;
		
		if(onClose != null)
			onClose.accept(work);
	}
	
}
