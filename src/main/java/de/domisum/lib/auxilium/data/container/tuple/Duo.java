package de.domisum.lib.auxilium.data.container.tuple;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@API
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Duo<T>
{

	// ATTRIBUTES
	@Getter
	private final T a;
	@Getter
	private final T b;


	// GETTERS
	@API
	public Duo<T> getInverted()
	{
		return new Duo<>(b, a);
	}

}