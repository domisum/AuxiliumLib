package io.domisum.lib.auxiliumlib.contracts.source.io.ioaction;

import java.io.IOException;

public interface IoAction<O>
{

	O execute() throws IOException;

}
