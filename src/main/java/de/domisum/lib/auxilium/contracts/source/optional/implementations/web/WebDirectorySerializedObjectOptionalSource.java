package de.domisum.lib.auxilium.contracts.source.optional.implementations.web;

import de.domisum.lib.auxilium.contracts.Converter;
import de.domisum.lib.auxilium.contracts.serialization.ToStringSerializer;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.mattp.util.MattpGetUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.Optional;

@API
public class WebDirectorySerializedObjectOptionalSource<KeyT, T> extends WebDirectoryOptionalSource<KeyT, T>
{

	private final ToStringSerializer<T> serializer;


	// INIT
	public WebDirectorySerializedObjectOptionalSource(
			AbstractURL webDirectory, Converter<KeyT, String> keyToInDirectoryPathConverter, ToStringSerializer<T> serializer)
	{
		super(webDirectory, keyToInDirectoryPathConverter);

		this.serializer = serializer;
	}


	// FETCH
	@Override protected Optional<T> fetch(AbstractURL url)
	{
		return MattpGetUtil.getString(url).map(serializer::deserialize);
	}

}
