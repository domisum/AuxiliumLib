package io.domisum.lib.auxiliumlib.datacontainers;

import java.io.File;

public record LastModifiedFile(
	File file,
	long lastModified
) implements Comparable<LastModifiedFile>
{
	
	public LastModifiedFile(File file)
	{
		this(file, file.lastModified());
	}
	
	@Override
	public int compareTo(LastModifiedFile o)
	{
		return Long.compare(lastModified, o.lastModified);
	}
	
}

