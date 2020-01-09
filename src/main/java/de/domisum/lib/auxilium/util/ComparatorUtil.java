package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ComparatorUtil
{

	@API
	public static <T> Comparator<T> noOrder()
	{
		return (v, w)->0;
	}

}
