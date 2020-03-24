package io.domisum.lib.auxiliumlib.datacontainers.tuple;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@API
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Pair<T, U>
{
	
	// VALUES
	@Getter
	private final T a;
	@Getter
	private final U b;
	
	
	// GETTERS
	@API
	public Pair<U,T> getInverted()
	{
		return new Pair<>(b, a);
	}
	
}