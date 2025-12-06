package io.domisum.lib.auxiliumlib.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class CollectionUtilTest
{
	
	@Test
	public void testFraction()
	{
		double delta = 1e-8;
		
		var ab = CollectionUtil.fractions(Map.of("a", 7, "b", 3));
		Assertions.assertEquals(2, ab.size());
		Assertions.assertEquals(0.7, ab.get("a"), delta);
		Assertions.assertEquals(0.3, ab.get("b"), delta);
		
		var empty = CollectionUtil.fractions(Map.of());
		Assertions.assertEquals(0, empty.size());
		
		var sumZero = CollectionUtil.fractions(Map.of("a", 0, "b", 0.0));
		Assertions.assertEquals(2, sumZero.size());
		Assertions.assertEquals(0, sumZero.get("a"), delta);
		Assertions.assertEquals(0, sumZero.get("b"), delta);
	}
	
}
