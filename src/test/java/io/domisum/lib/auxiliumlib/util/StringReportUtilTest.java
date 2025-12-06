package io.domisum.lib.auxiliumlib.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.domisum.lib.auxiliumlib.util.StringReportUtil.report;

public class StringReportUtilTest
{
	
	@Test
	public void testEmptyCollections()
	{
		Assertions.assertEquals("", report(Map.of()));
		Assertions.assertEquals("", report(List.of()));
		Assertions.assertEquals("", report(Set.of()));
	}
	
	@Test
	public void testSimple()
	{
		Assertions.assertEquals(" - abc", report(List.of("abc")));
		Assertions.assertEquals(" - abc\n - jjj", report(List.of("abc", "jjj")));
	}
	
	@Test
	public void testIndent()
	{
		Assertions.assertEquals(" - abc\n   bar\n - ok\n   test", report(List.of("abc\nbar", "ok\ntest")));
	}
	
}
