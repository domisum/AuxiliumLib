package io.domisum.lib.auxiliumlib.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.BiConsumer;

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
			gson = new GsonBuilder()
				.enableComplexMapKeySerialization()
				.setPrettyPrinting()
				.serializeNulls()
				.disableHtmlEscaping()
				.create();
		
		return gson;
	}
	
	
	public static void addProperty(JsonObject base, String path, String value)
	{
		addProperty(base, path, (o, k)->o.addProperty(k, value));
	}
	
	public static void addProperty(JsonObject base, String path, Number value)
	{
		addProperty(base, path, (o, k)->o.addProperty(k, value));
	}
	
	public static void addProperty(JsonObject base, String path, Boolean value)
	{
		addProperty(base, path, (o, k)->o.addProperty(k, value));
	}
	
	public static void addProperty(JsonObject base, String path, BiConsumer<JsonObject, String> addProperty)
	{
		int dotIndex = path.indexOf('.');
		if(dotIndex == -1)
		{
			addProperty.accept(base, path);
			return;
		}
		
		String frontPathPart = path.substring(0, dotIndex);
		String remainingPathPart = path.substring(dotIndex+1);
		
		var container = base.has(frontPathPart) ? base.getAsJsonObject(frontPathPart) : new JsonObject();
		if(!base.has(frontPathPart))
			base.add(frontPathPart, container);
		
		addProperty(container, remainingPathPart, addProperty);
	}
	
}
