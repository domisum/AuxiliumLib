package io.domisum.lib.auxiliumlib;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.contracts.IoIterator;
import io.domisum.lib.auxiliumlib.contracts.source.io.IoOptional;
import io.domisum.lib.auxiliumlib.util.StringListUtil;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class FileLineStream
	implements Closeable
{
	
	private Deque<String> linesBeforePointer = new ArrayDeque<>();
	private Deque<String> linesAfterPointer = new ArrayDeque<>();
	
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
			//noinspection resource
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
		reachedEndOfReader = true;
		reader.close();
		linesBeforePointer.clear();
	}
	
	
	// STREAM
	@API
	public String pointToLineContaining(String... markers)
		throws IOException
	{
		return pointToLineContainingOptional(markers).getOrThrowWrapped(e ->
		{
			String markersDisplay = StringListUtil.list(Arrays.asList((Object[]) markers), m -> "'" + m + "'", " or ");
			String message = PHR.r("Could not find line containing {}", markersDisplay);
			return new IOException(message, e);
		});
	}
	
	@API
	public IoOptional<String> pointToLineContainingOptional(String... markers)
	{
		var iterator = iterateAndMovePointer();
		while(true)
			try
			{
				String line = iterator.next();
				for(String m : markers)
					if(line.contains(m))
						return IoOptional.of(line);
			}
			catch(IOException e)
			{
				return IoOptional.ofException(e);
			}
	}
	
	@API
	public void resetPointer()
	{
		linesBeforePointer.addAll(linesAfterPointer);
		linesAfterPointer = linesBeforePointer;
		linesBeforePointer = new ArrayDeque<>();
	}
	
	@API
	public void discardLinesBeforePointer()
	{
		linesBeforePointer.clear();
	}
	
	@API
	public void discardSingle()
		throws IOException
	{
		iterateAndMovePointer().next();
		discardLinesBeforePointer();
	}
	
	
	@API
	public IoIterator<String> iterateAndMovePointer()
	{
		return new MovePointerIterator();
	}
	
	private class MovePointerIterator
		implements IoIterator<String>
	{
		
		@Override
		public boolean hasNext()
			throws IOException
		{
			if(linesAfterPointer.size() > 0)
				return true;
			if(reachedEndOfReader)
				return false;
			
			readNextLine();
			return linesAfterPointer.size() > 0;
		}
		
		@Override
		public String next()
			throws IOException
		{
			if(linesAfterPointer.isEmpty())
				readNextLine();
			
			String line = linesAfterPointer.removeFirst();
			linesBeforePointer.add(line);
			return line;
		}
		
		private void readNextLine()
			throws IOException
		{
			String nextLine = reader.readLine();
			if(nextLine == null)
			{
				close();
				throw new IOException("There is no next line");
			}
			
			linesAfterPointer.add(nextLine);
		}
		
	}
	
}
