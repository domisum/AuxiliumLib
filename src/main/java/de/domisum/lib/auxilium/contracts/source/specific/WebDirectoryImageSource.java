package de.domisum.lib.auxilium.contracts.source.specific;

import de.domisum.lib.auxilium.contracts.source.implementations.WebDirectorySource;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.HttpFetchUtil;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class WebDirectoryImageSource extends WebDirectorySource<BufferedImage>
{

	// INIT
	public WebDirectoryImageSource(AbstractURL webDirectory)
	{
		super(webDirectory);
	}


	// FETCH
	@Override protected Optional<BufferedImage> fetch(AbstractURL url)
	{
		return HttpFetchUtil.fetchImage(url);
	}

}
