package de.domisum.lib.auxilium.mattp.response;

import de.domisum.lib.auxilium.mattp.MattpHeaders;
import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.IOException;
import java.util.Optional;

public interface RequestResponse<T>
{

	@API boolean isSuccess();


	@API Optional<StatusLine> getStatusLine();

	@API Optional<MattpHeaders> getHeaders();

	@API Optional<T> getContent();


	@API Optional<String> getErrorMessage();

	default void throwExceptionIfFailed(String message) throws IOException
	{
		if(isSuccess())
			return;

		throw new IOException(message+"; error: "+getErrorMessage().orElse("<no error message>"));
	}

}
