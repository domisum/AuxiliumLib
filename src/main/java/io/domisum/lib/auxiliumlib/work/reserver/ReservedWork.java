package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@RequiredArgsConstructor
public final class ReservedWork<T>
	implements AutoCloseable
{
	
	@Getter private final T subject;
	
	// STATUS
	@Getter private boolean closed = false;
	@Nullable private final Consumer<ReservedWork<T>> onClose;
	
	@Getter private boolean successful = false;
	@Nullable private final Consumer<ReservedWork<T>> onSuccessful;
	@Nullable private final Consumer<ReservedWork<T>> onFail;
	
	
	// INIT
	@API
	public static <T> ReservedWork<T> of(T subject)
	{
		return new ReservedWork<>(subject, null, null, null);
	}
	
	@API
	public static <T> ReservedWork<T> ofOnClose(T subject, Consumer<ReservedWork<T>> onClose)
	{
		return new ReservedWork<>(subject, onClose, null, null);
	}
	
	@API
	public static <T> ReservedWork<T> ofOnCloseOnSuccessfulOnFail(
		T subject, Consumer<ReservedWork<T>> onClose, Consumer<ReservedWork<T>> onSuccessful, Consumer<ReservedWork<T>> onFail)
	{
		return new ReservedWork<>(subject, onClose, onSuccessful, onFail);
	}
	
	
	// STATUS
	public void successful()
	{
		if(successful)
			return;
		successful = true;
		
		if(onSuccessful != null)
			onSuccessful.accept(this);
	}
	
	@Override
	public void close()
	{
		if(closed)
			return;
		closed = true;
		
		if(!successful)
			if(onFail != null)
				onFail.accept(this);
		
		if(onClose != null)
			onClose.accept(this);
	}
	
}
