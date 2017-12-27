package de.domisum.lib.auxilium.contracts.source.specific;

import de.domisum.lib.auxilium.contracts.source.SingleItemSource;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.HttpFetchUtil;
import lombok.RequiredArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.Optional;

@RequiredArgsConstructor
public class WebSingleImageSource implements SingleItemSource<BufferedImage>
{

	private final AbstractURL url;


	// SOURCE
	@Override public Optional<BufferedImage> fetch()
	{
		return HttpFetchUtil.fetchImage(url);
	}

}
