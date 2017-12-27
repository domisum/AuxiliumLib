package de.domisum.lib.auxilium.contracts.source.specific;

import de.domisum.lib.auxilium.contracts.source.implementations.WebSingleItemSource;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.HttpFetchUtil;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class WebSingleImageSource extends WebSingleItemSource<BufferedImage>
{

	// INIT
	public WebSingleImageSource(AbstractURL url)
	{
		super(url);
	}


	// SOURCE
	@Override public Optional<BufferedImage> fetch()
	{
		return HttpFetchUtil.fetchImage(url);
	}

}
