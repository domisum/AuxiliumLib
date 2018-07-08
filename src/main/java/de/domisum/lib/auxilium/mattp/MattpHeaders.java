package de.domisum.lib.auxilium.mattp;

import de.domisum.lib.auxilium.util.StringUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MattpHeaders implements Iterable<MattpHeader>
{

	private final List<MattpHeader> headers;


	// INIT
	public MattpHeaders(List<MattpHeader> headers)
	{
		this.headers = new ArrayList<>(headers);
	}


	// OBJECT
	@Override public String toString()
	{
		return StringUtil.listToString(headers, "\n");
	}

	@Nonnull @Override public Iterator<MattpHeader> iterator()
	{
		return headers.iterator();
	}


	// GETTERS
	public List<MattpHeader> getHeaders(String key)
	{
		List<MattpHeader> keyHeaders = new ArrayList<>();

		for(MattpHeader h : headers)
			if(h.getKey().equalsIgnoreCase(key))
				keyHeaders.add(h);

		return keyHeaders;
	}

}
