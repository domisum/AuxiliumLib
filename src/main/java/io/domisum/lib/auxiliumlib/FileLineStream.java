package io.domisum.lib.auxiliumlib;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FileLineStream
	implements Closeable
{
	
	private final Deque<String> alreadyReadLines = new ArrayDeque<>();
	
	private final BufferedReader reader;
	private boolean reachedEndOfReader = false;
	
	
	// INIT
	@API
	public FileLineStream(File file)
	{
		this(file, 0);
	}
	
	@API
	public FileLineStream(File file, long offset)
	{
		try
		{
			var fis = new FileInputStream(file);
			fis.getChannel().position(offset);
			
			reader = new BufferedReader(new InputStreamReader(fis));
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	@Override
	public void close()
		throws IOException
	{
		reader.close();
	}
	
	
	// STREAM
	@API
	public String discardLinesUntilContains(String marker)
		throws IOException
	{
		while(alreadyReadLines.size() > 0)
		{
			String line = alreadyReadLines.removeFirst();
			if(line.contains(marker))
				return line;
		}
		
		return findLineInNextLines(marker, false);
	}
	
	@API
	public String findLine(String marker)
		throws IOException
	{
		for(String line : alreadyReadLines)
			if(line.contains(marker))
				return line;
		
		return findLineInNextLines(marker, true);
	}
	
	private String findLineInNextLines(String marker, boolean addLinesToAlreadyRead)
		throws IOException
	{
		String line;
		while(true)
		{
			line = reader.readLine();
			if(line == null)
			{
				reachedEndOfReader = true;
				break;
			}
			
			if(addLinesToAlreadyRead)
				alreadyReadLines.add(line);
			if(line.contains(marker))
				return line;
		}
		
		throw new IOException(PHR.r("Could not find line containing '{}'", marker));
	}
	
	@API
	public Iterator<String> iterateAndDiscard()
	{
		return new DiscardingIterator();
	}
	
	private class DiscardingIterator
		implements Iterator<String>
	{
		
		@SneakyThrows
		@Override
		public boolean hasNext()
		{
			if(alreadyReadLines.size() > 0)
				return true;
			
			if(reachedEndOfReader)
				return false;
			
			String nextLine = reader.readLine();
			boolean hasNext = nextLine != null;
			if(hasNext)
				alreadyReadLines.add(nextLine);
			else
				reachedEndOfReader = true;
			
			return hasNext;
		}
		
		@SneakyThrows
		@Override
		public String next()
		{
			if(alreadyReadLines.size() > 0)
				return alreadyReadLines.removeFirst();
			
			String nextLine = reader.readLine();
			if(nextLine == null)
			{
				reachedEndOfReader = true;
				throw new NoSuchElementException("There is no next line");
			}
			return nextLine;
		}
		
	}
	
}
