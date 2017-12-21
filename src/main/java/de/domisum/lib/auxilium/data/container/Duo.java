package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@API
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Duo<T>
{

	// ATTRIBUTES
	private final T a;
	private final T b;


	// GETTERS
	@API public Duo<T> getInverted()
	{
		return new Duo<>(b, a);
	}

}