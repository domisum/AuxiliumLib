package de.domisum.lib.auxilium.contracts.iosource.ioaction;

import java.io.IOException;

public interface IoAction<O>
{

	O execute() throws IOException;

}
