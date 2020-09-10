package io.domisum.lib.auxiliumlib.thread.multithreader;

import java.util.Optional;

public interface MultithreaderQueue<T>
{
	
	boolean areThereMoreElements();
	
	Optional<T> poll();
	
}
