package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Duo<T, U>
{

	// VALUES
	public final T a;
	public final U b;


	// GETTERS
	@API public Duo<U, T> getInverted()
	{
		return new Duo<>(this.b, this.a);
	}

}