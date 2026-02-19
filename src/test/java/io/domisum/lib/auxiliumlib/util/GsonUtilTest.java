package io.domisum.lib.auxiliumlib.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GsonUtilTest
{
	
	@Test
	public void testGetProperty()
	{
		assertProp(null, new JsonObject(), "a.b");
	}
	
	
	// ASSERT
	private void assertProp(Object expected, JsonElement element, String path)
	{Assertions.assertEquals(expected, GsonUtil.getProperty(element, path));}
	
}
