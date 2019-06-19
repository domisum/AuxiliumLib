package de.domisum.lib.auxilium.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GsonUtil
{

	// REFERENCES
	private static Gson gson = null;
	private static Gson prettyGson = null;


	@API
	public static synchronized Gson get()
	{
		if(gson == null)
			gson = new GsonBuilder().enableComplexMapKeySerialization().create();

		return gson;
	}

	@API
	public static synchronized Gson getPretty()
	{
		if(prettyGson == null)
			prettyGson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

		return prettyGson;
	}

}
