package io.domisum.lib.auxiliumlib.contracts.serialization;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.File;
import java.io.IOException;

@API
public interface ToFromFileSerializer<T>
{

	File serializeToFile(T object)
			throws IOException;

	T deserializeFromFile(File file)
			throws IOException;

}
