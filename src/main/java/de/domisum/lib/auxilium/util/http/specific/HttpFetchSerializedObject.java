package de.domisum.lib.auxilium.util.http.specific;

import de.domisum.lib.auxilium.contracts.serialization.ToStringSerializer;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.http.HttpFetch;
import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.InputStream;
import java.util.Optional;

@API
public class HttpFetchSerializedObject<T> extends HttpFetch<T>
{

	// ATTRIBUTES
	private final ToStringSerializer<T> serializer;


	// INIT
	public HttpFetchSerializedObject(AbstractURL url, ToStringSerializer<T> serializer)
	{
		super(url);
		this.serializer = serializer;
	}


	// FETCH
	@Override
	protected Optional<T> convertToSpecific(InputStream inputStream)
	{
		Optional<String> serializedOptional = new HttpFetchString(null).convertToSpecific(inputStream);
		if(!serializedOptional.isPresent())
			return Optional.empty();

		String serialized = serializedOptional.get();
		T deserialized = serializer.deserialize(serialized);
		return Optional.ofNullable(deserialized);
	}

}
