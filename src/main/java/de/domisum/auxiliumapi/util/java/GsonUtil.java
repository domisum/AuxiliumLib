package de.domisum.auxiliumapi.util.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.domisum.auxiliumapi.util.java.annotations.APIUsage;

@APIUsage
public class GsonUtil
{

	// REFERENCES
	private static Gson gson;
	private static Gson prettyGson;


	@APIUsage
	public static Gson get()
	{
		if(gson == null)
			gson = new GsonBuilder().create();

		return gson;
	}

	@APIUsage
	public static Gson getPretty()
	{
		if(prettyGson == null)
			prettyGson = new GsonBuilder().setPrettyPrinting().create();

		return prettyGson;
	}

}
