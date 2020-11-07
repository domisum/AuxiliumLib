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
	private final T subject;
	
	// STATUS
	@Getter
	private boolean successful = false;
	@Nullable
	private final Consumer<ReservedWork<T>> onSuccessful;
	
	@Getter
	private boolean closed = false;
	@Nullable
	private final Consumer<ReservedWork<T>> onClose;
	
	
	// INIT
	@API
	public static <T> ReservedWork<T> of(T subject)
	{
		return new ReservedWork<>(subject, null, null);
	}
	
	@API
	public static <T> ReservedWork<T> ofOnClose(T subject, Consumer<ReservedWork<T>> onClose)
	{
		return new ReservedWork<>(subject, null, onClose);
	}
	
	@API
	public static <T> ReservedWork<T> ofOnSuccessfulOnClose(T subject, Consumer<ReservedWork<T>> onSuccessful, Consumer<ReservedWork<T>> onClose)
	{
		return new ReservedWork<>(subject, onSuccessful, onClose);
	}
	
	
	// STATUS
	public void successful()
	{
		successful = true;
		
		if(onSuccessful != null)
			onSuccessful.accept(this);
	}
	
	@Override
	public void close()
	{
		closed = true;
		
		if(onClose != null)
			onClose.accept(this);
	}
	
}
