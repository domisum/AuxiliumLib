package de.domisum.lib.auxilium.contracts.serialization;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.File;
import java.io.IOException;

@API
public interface ToFromFileSerializer<T>
{

	File serializeToFile(T object) throws IOException;

	T deserializeFromFile(File file) throws IOException;

}
