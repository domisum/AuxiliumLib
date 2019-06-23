package de.domisum.lib.auxilium.contracts;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.IOException;

public interface IoConsumer<T>
{

	@API
	void accept(T input) throws IOException;

}
