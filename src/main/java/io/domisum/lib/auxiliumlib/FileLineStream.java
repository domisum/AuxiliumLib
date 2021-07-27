package io.domisum.lib.auxiliumlib;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class FileLineStream
{
	
	private final Deque<String> currentLines = new ArrayDeque<>();
	private final Iterator<String> iterator;
	
	
	// INIT
	public FileLineStream(File file)
	{
		try
		{
			iterator = Files.lines(file.toPath()).iterator();
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	
	// STREAM
	@API
	public String discardLinesUntil(String marker)
		throws IOException
	{
		while(currentLines.size() > 0)
		{
			String cl = currentLines.removeFirst();
			if(cl.contains(marker))
				return cl;
		}
		
		return findLineInIterator(marker, false);
	}
	
	@API
	public String findLine(String marker)
		throws IOException
	{
		for(String cl : currentLines)
			if(cl.contains(marker))
				return cl;
		
		return findLineInIterator(marker, true);
	}
	
	private String findLineInIterator(String marker, boolean addLinesToCurrent)
		throws IOException
	{
		while(iterator.hasNext())
		{
			String line = iterator.next();
			
			if(addLinesToCurrent)
				currentLines.add(line);
			if(line.contains(marker))
				return line;
		}
		
		throw new IOException(PHR.r("Could not find line containing '{}'", marker));
	}
	
	@API
	public Iterator<String> iterateAndDiscard()
	{
		return new LineIterator();
	}
	
	private class LineIterator
		implements Iterator<String>
	{
		
		@Override
		public boolean hasNext()
		{
			if(currentLines.size() > 0)
				return true;
			
			return iterator.hasNext();
		}
		
		@Override
		public String next()
		{
			if(currentLines.size() > 0)
				return currentLines.removeFirst();
			
			return iterator.next();
		}
		
	}
	
}
