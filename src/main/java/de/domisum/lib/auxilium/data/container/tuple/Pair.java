package de.domisum.lib.auxilium.data.container.tuple;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Pair<T, U>
{

	// VALUES
	@Getter private final T a;
	@Getter private final U b;


	// GETTERS
	@API public Pair<U, T> getInverted()
	{
		return new Pair<>(b, a);
	}

}