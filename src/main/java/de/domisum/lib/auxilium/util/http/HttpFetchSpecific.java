package de.domisum.lib.auxilium.util.http;

import de.domisum.lib.auxilium.util.java.ExceptionHandler;

import java.io.InputStream;
import java.util.Optional;

public interface HttpFetchSpecific<T>
{

	Optional<T> fetch(InputStream inputStream, ExceptionHandler<Exception> onFail);

}
