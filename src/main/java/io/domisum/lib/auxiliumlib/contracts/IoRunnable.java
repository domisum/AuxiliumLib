package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface IoRunnable
{
	
	@API
	void run()
		throws IOException;
	
}
