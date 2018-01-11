package de.domisum.lib.auxilium.mattp.request;

import java.io.InputStream;

public interface MattpRequestBody
{

	String getContentType();

	InputStream getAsInputStream();

}
