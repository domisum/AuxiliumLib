package de.domisum.lib.auxilium.http.response.readers;

import de.domisum.lib.auxilium.http.response.HttpResponseBodyReader;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@API
@RequiredArgsConstructor
public class HttpImageReader implements HttpResponseBodyReader<BufferedImage>
{

	// READ
	@Override public BufferedImage read(InputStream inputStream) throws IOException
	{
		return ImageIO.read(inputStream);
	}

}
