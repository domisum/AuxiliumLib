package de.domisum.lib.auxilium.contracts.source.optional.implementations.web.image;

import de.domisum.lib.auxilium.contracts.Converter;
import de.domisum.lib.auxilium.contracts.source.optional.implementations.web.WebDirectoryOptionalSource;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.mattp.util.MattpGetUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;

import java.awt.image.BufferedImage;
import java.util.Optional;

@API
public class WebDirectoryImageOptionalSource<KeyT> extends WebDirectoryOptionalSource<KeyT, BufferedImage>
{

	// INIT
	public WebDirectoryImageOptionalSource(AbstractURL webDirectory, Converter<KeyT, String> keyToInDirectoryPathConverter)
	{
		super(webDirectory, keyToInDirectoryPathConverter);
	}


	// FETCH
	@Override protected Optional<BufferedImage> fetch(AbstractURL url)
	{
		return MattpGetUtil.getImage(url);
	}

}
