package de.domisum.lib.auxilium.util.http;

import de.domisum.lib.auxilium.util.StringUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@API
public class CookieHeaderValue implements CharSequence
{

	private final String asString;


	// INIT
	public CookieHeaderValue(Map<String, String> cookies)
	{
		List<String> cookieKeyValuePairs = new ArrayList<>();
		for(Entry<String, String> entry : cookies.entrySet())
			cookieKeyValuePairs.add(entry.getKey()+"="+entry.getValue());

		asString = StringUtil.listToString(cookieKeyValuePairs, "; ");
	}

	public static Builder builder()
	{
		return new Builder();
	}

	@API
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Builder
	{

		private final Map<String, String> cookies = new HashMap<>();


		public Builder set(CharSequence key, CharSequence value)
		{
			cookies.put(key.toString(), value.toString());
			return this;
		}

		public CookieHeaderValue build()
		{
			return new CookieHeaderValue(cookies);
		}

	}


	// STRING
	@Override public int length()
	{
		return asString.length();
	}

	@Override public char charAt(int index)
	{
		return asString.charAt(index);
	}

	@Override public CharSequence subSequence(int start, int end)
	{
		return asString.subSequence(start, end);
	}

	@Nonnull @Override public String toString()
	{
		return asString;
	}

}
