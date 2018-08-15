package de.domisum.lib.auxilium.contracts.source.optional.implementations.web.image;

import de.domisum.lib.auxilium.contracts.source.optional.implementations.web.WebSingleItemOptionalSource;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.mattp.util.MattpGetUtil;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class WebSingleImageOptionalSource extends WebSingleItemOptionalSource<BufferedImage>
{

	// INIT
	public WebSingleImageOptionalSource(AbstractURL url)
	{
		super(url);
	}


	// SOURCE
	@Override public Optional<BufferedImage> fetch()
	{
		return MattpGetUtil.getImage(url);
	}

}
