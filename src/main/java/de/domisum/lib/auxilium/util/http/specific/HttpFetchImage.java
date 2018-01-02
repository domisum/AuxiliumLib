package de.domisum.lib.auxilium.util.http.specific;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.http.HttpFetch;
import de.domisum.lib.auxilium.util.java.annotations.API;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@API
public class HttpFetchImage extends HttpFetch<BufferedImage>
{

	// INIT
	public HttpFetchImage(AbstractURL url)
	{
		super(url);
	}


	// FETCH
	@Override protected Optional<BufferedImage> fetch(InputStream inputStream)
	{
		try
		{
			return Optional.ofNullable(ImageIO.read(inputStream));
		}
		catch(IOException e)
		{
			getOnFail().handle(e);
			return Optional.empty();
		}
	}

}
