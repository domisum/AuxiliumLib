package de.domisum.lib.auxilium.contracts.source.implementations.web.image;

import de.domisum.lib.auxilium.contracts.source.implementations.web.WebSingleItemSource;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.mattp.util.MattpGetUtil;

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
		return MattpGetUtil.getImage(url);
	}

}
