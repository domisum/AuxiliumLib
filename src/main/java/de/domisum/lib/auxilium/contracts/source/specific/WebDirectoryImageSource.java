package de.domisum.lib.auxilium.contracts.source.specific;

import de.domisum.lib.auxilium.contracts.Converter;
import de.domisum.lib.auxilium.contracts.source.implementations.WebDirectorySource;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.HttpFetchUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;

import java.awt.image.BufferedImage;
import java.util.Optional;

@API
public class WebDirectoryImageSource<KeyT> extends WebDirectorySource<KeyT, BufferedImage> implements ImageSource<KeyT>
{

	// INIT
	public WebDirectoryImageSource(AbstractURL webDirectory, Converter<KeyT, String> keyToInDirectoryPathConverter)
	{
		super(webDirectory, keyToInDirectoryPathConverter);
	}


	// FETCH
	@Override protected Optional<BufferedImage> fetch(AbstractURL url)
	{
		return HttpFetchUtil.fetchImage(url);
	}

}