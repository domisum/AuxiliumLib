package io.domisum.lib.auxiliumlib.thread.multithreader.queues;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.thread.multithreader.MultithreaderQueue;

import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@API
public class MultithreaderQueue_Collection<T>
	implements MultithreaderQueue<T>
{
	
	// STATE
	private final Queue<T> queue = new ConcurrentLinkedQueue<>();
	
	
	// INIT
	@API
	public MultithreaderQueue_Collection(Collection<T> collection)
	{
		queue.addAll(collection);
	}
	
	
	// QUEUE
	@Override
	public boolean areThereMoreElements()
	{
		return queue.size() > 0;
	}
	
	@Override
	public Optional<T> poll()
	{
		var calcNullable = queue.poll();
		return Optional.ofNullable(calcNullable);
	}
	
}
