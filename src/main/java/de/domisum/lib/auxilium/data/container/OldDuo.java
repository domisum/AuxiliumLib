package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class OldDuo<T, U>
{

	// VALUES
	public final T a;
	public final U b;


	// GETTERS
	@API public OldDuo<U, T> getInverted()
	{
		return new OldDuo<>(b, a);
	}

}