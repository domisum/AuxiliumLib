package io.domisum.lib.auxiliumlib.contracts.iosource.ioaction;

import java.io.IOException;

public interface IoAction<O>
{
	
	O execute()
			throws IOException;
	
}
