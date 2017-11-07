package de.domisum.lib.auxilium.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.domisum.lib.auxilium.util.java.annotations.API;

@API
public class GsonUtil
{

	// REFERENCES
	private static Gson gson;
	private static Gson prettyGson;


	@API public static Gson get()
	{
		if(gson == null)
			gson = new GsonBuilder().create();

		return gson;
	}

	@API public static Gson getPretty()
	{
		if(prettyGson == null)
			prettyGson = new GsonBuilder().setPrettyPrinting().create();

		return prettyGson;
	}

}
