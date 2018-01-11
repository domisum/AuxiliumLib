package de.domisum.lib.auxilium.http.request;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

public class HttpHeader
{

	@Getter private final String key;
	@Getter private final String value;


	// INIT
	public HttpHeader(String key, String value)
	{
		validateKey(key);
		validateValue(value);

		this.key = key;
		this.value = value;
	}

	private void validateKey(String key)
	{
		Validate.notNull(key);
		Validate.isTrue(!key.contains(":"), "header key can't contain colon (:)");
	}

	private void validateValue(String value)
	{
		Validate.notNull(value);
	}

}
