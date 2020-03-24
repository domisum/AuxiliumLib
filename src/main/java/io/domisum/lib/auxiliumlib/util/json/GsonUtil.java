package io.domisum.lib.auxiliumlib.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GsonUtil
{

	// REFERENCES
	private static Gson gson = null;


	@API
	public static synchronized Gson get()
	{
		if(gson == null)
			gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().serializeNulls().create();

		return gson;
	}

}
