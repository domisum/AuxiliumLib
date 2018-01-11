package de.domisum.lib.auxilium.http.response;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.Optional;

public interface RequestResponse<T>
{

	@API default boolean isSuccess()
	{
		return getContent().isPresent();
	}


	@API Optional<StatusLine> getStatusLine();

	@API Optional<T> getContent();

	@API Optional<String> getErrorMessage();

}
