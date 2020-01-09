package de.domisum.lib.auxilium.data.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractURLTest
{

	@Test
	public void testSimpleUrlParsing()
	{
		Assertions.assertEquals("http://www.google.com", new AbstractURL("http://www.google.com").toString());
		Assertions.assertEquals("http://www.google.com", new AbstractURL("http://www.google.com/").toString());

		Assertions.assertEquals("http://www.google.com/test", new AbstractURL("http://www.google.com/test").toString());
		Assertions.assertEquals("http://www.google.com/test", new AbstractURL("http://www.google.com/test/").toString());
	}

	@Test
	public void testUrlExtension()
	{
		AbstractURL googleBase = new AbstractURL("http://www.google.com");

		Assertions.assertEquals("http://www.google.com/kek", new AbstractURL(googleBase, "kek").toString());
		Assertions.assertEquals("http://www.google.com/kek", new AbstractURL(googleBase, "kek/").toString());
		Assertions.assertEquals("http://www.google.com/kek", new AbstractURL(googleBase, "/kek/").toString());
		Assertions.assertEquals("http://www.google.com/somefile.html", new AbstractURL(googleBase, "somefile.html").toString());
	}

	@Test
	public void testJavaNetConversion()
	{
		Assertions.assertEquals("http://www.google.com", new AbstractURL("http://www.google.com").toNet().toString());

		Assertions.assertThrows(Exception.class, ()->new AbstractURL("invalidurl").toNet());
	}


	@Test
	public void testToString()
	{
		Assertions.assertEquals("http://www.google.com", new AbstractURL("http://www.google.com").toString());
		Assertions.assertEquals("http://www.google.com", new AbstractURL("http://www.google.com/").toString());
	}

}
