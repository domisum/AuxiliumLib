package de.domisum.lib.auxilium.contracts.serialization;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.File;
import java.io.IOException;

@API
public interface FileSerializer<T>
{

	File serialize(T object) throws IOException;

	T deserialize(File file) throws IOException;

}
