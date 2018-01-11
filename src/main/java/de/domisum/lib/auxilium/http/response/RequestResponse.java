package de.domisum.lib.auxilium.http.response;

import java.util.Optional;

public interface RequestResponse<T>
{

	Optional<StatusLine> getStatusLine();

	Optional<T> getBody();

	Optional<String> getErrorMessage();

}
