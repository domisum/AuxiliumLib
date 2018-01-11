package de.domisum.lib.auxilium.http.response.readers;

import de.domisum.lib.auxilium.contracts.serialization.ToStringSerializer;
import de.domisum.lib.auxilium.http.response.HttpResponseBodyReader;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@API
@RequiredArgsConstructor
public class HttpSerializedObjectReader<T> implements HttpResponseBodyReader<T>
{

	private final HttpResponseBodyReader<String> stringReader;
	private final ToStringSerializer<T> toStringSerializer;


	// INIT
	public HttpSerializedObjectReader(ToStringSerializer<T> toStringSerializer)
	{
		stringReader = new HttpStringReader();
		this.toStringSerializer = toStringSerializer;
	}


	// READ
	@Override public T read(InputStream inputStream) throws IOException
	{
		String json = stringReader.read(inputStream);

		try
		{
			return toStringSerializer.deserialize(json);
		}
		catch(RuntimeException e)
		{
			throw new IOException("Failed to deserialize object", e);
		}
	}

}
