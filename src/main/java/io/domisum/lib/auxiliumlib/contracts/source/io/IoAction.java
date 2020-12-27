package io.domisum.lib.auxiliumlib.contracts.source.io;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

@API
public interface IoAction<O>
{
	
	@API
	O execute()
		throws IOException;
	
}
