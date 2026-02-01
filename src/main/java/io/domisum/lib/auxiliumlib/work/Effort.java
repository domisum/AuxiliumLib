package io.domisum.lib.auxiliumlib.work;

public enum Effort
{
	
	SOME,
	NONE;
	
	
	public static Effort merge(Effort a, Effort b) {return a == SOME || b == SOME ? SOME : NONE;}
	
}
