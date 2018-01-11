package de.domisum.lib.auxilium.http.request;

import java.io.InputStream;

public interface HttpRequestBody
{

	String getContentType();

	InputStream getAsInputStream();

}
