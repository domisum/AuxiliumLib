package io.domisum.lib.auxiliumlib.datacontainers;

import java.io.File;
import java.time.Instant;
import java.util.Comparator;

public record LastModifiedFile(
	File file,
	long lastModified
) implements Comparable<LastModifiedFile>
{
	
	public static Comparator<LastModifiedFile> MOST_RECENT_FIRST = Comparator.<LastModifiedFile>naturalOrder().reversed();
	
	
	// OBJECT
	public LastModifiedFile(File file)
	{
		this(file, file.lastModified());
	}
	
	@Override
	public int compareTo(LastModifiedFile o)
	{
		return Long.compare(lastModified, o.lastModified);
	}
	
	
	// GETTERS
	public Instant getLastModifiedInstant()
	{
		return Instant.ofEpochMilli(lastModified);
	}
	
}

