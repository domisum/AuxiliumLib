package io.domisum.lib.auxiliumlib.work;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

@API
public class InsertWorkDistributor<T>
	extends WorkDistributor<T>
{
	
	// STATE
	protected final Queue<T> insertWorkQueue = new LinkedList<>();
	
	
	// INSERT
	@API
	public synchronized void insert(T work)
	{
		insertWorkQueue.add(work);
	}
	
	@API
	public synchronized void insertAll(Collection<T> workCollection)
	{
		insertWorkQueue.addAll(workCollection);
	}
	
	@Override
	protected void onClose(ReservedWork<T> work)
	{
		super.onClose(work);
		if(!work.isSuccessful())
			insert(work.getSubject());
	}
	
	@Override
	public synchronized boolean isEmpty()
	{
		if(!insertWorkQueue.isEmpty())
			return false;
		return super.isEmpty();
	}
	
	
	// REFILL
	@Override
	protected boolean shouldRefill()
	{
		return insertWorkQueue.size() > 0;
	}
	
	@Override
	protected synchronized Collection<T> getMoreWork()
	{
		var moreWork = new ArrayList<>(insertWorkQueue);
		insertWorkQueue.clear();
		return moreWork;
	}
	
}
