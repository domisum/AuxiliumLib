package de.domisum.lib.auxilium.http.body;

import java.io.InputStream;

public interface HttpBody
{

	String getContentType();

	InputStream getAsInputStream();

}
