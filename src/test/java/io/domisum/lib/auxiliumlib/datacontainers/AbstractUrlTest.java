package io.domisum.lib.auxiliumlib.datacontainers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractUrlTest
{
	
	@Test
	public void testSimpleUrlParsing()
	{
		Assertions.assertEquals("http://www.google.com", new AbstractUrl("http://www.google.com").toString());
		Assertions.assertEquals("http://www.google.com", new AbstractUrl("http://www.google.com/").toString());
		
		Assertions.assertEquals("http://www.google.com/test", new AbstractUrl("http://www.google.com/test").toString());
		Assertions.assertEquals("http://www.google.com/test", new AbstractUrl("http://www.google.com/test/").toString());
	}
	
	@Test
	public void testUrlExtension()
	{
		AbstractUrl googleBase = new AbstractUrl("http://www.google.com");
		
		Assertions.assertEquals("http://www.google.com/kek", new AbstractUrl(googleBase, "kek").toString());
		Assertions.assertEquals("http://www.google.com/kek", new AbstractUrl(googleBase, "kek/").toString());
		Assertions.assertEquals("http://www.google.com/kek", new AbstractUrl(googleBase, "/kek/").toString());
		Assertions.assertEquals("http://www.google.com/somefile.html", new AbstractUrl(googleBase, "somefile.html").toString());
	}
	
	@Test
	public void testJavaNetConversion()
	{
		Assertions.assertEquals("http://www.google.com", new AbstractUrl("http://www.google.com").toNet().toString());
		
		Assertions.assertThrows(Exception.class, ()->new AbstractUrl("invalidurl").toNet());
	}
	
	
	@Test
	public void testToString()
	{
		Assertions.assertEquals("http://www.google.com", new AbstractUrl("http://www.google.com").toString());
		Assertions.assertEquals("http://www.google.com", new AbstractUrl("http://www.google.com/").toString());
	}
	
}
