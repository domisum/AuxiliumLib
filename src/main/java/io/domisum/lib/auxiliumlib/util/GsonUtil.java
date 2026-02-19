package io.domisum.lib.auxiliumlib.util;

import com.google.gson.*;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
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
		addProperty(base, path, (o, k) -> o.addProperty(k, value));
	}
	
	public static void addProperty(JsonObject base, String path, Number value)
	{
		addProperty(base, path, (o, k) -> o.addProperty(k, value));
	}
	
	public static void addProperty(JsonObject base, String path, Boolean value)
	{
		addProperty(base, path, (o, k) -> o.addProperty(k, value));
	}
	
	public static void addProperty(JsonObject base, String path, Object[] value)
	{
		addProperty(base, path, Arrays.asList(value));
	}
	
	public static void addProperty(JsonObject base, String path, Collection<?> value)
	{
		addProperty(base, path, (o, k) ->
		{
			var array = new JsonArray();
			for(Object v : value)
				if(v instanceof String s)
					array.add(s);
				else if(v instanceof Number n)
					array.add(n);
				else if(v instanceof Boolean b)
					array.add(b);
				else if(v instanceof Character c)
					array.add(c);
				else if(v instanceof JsonObject jo)
					array.add(jo);
				else
					throw new IllegalArgumentException("Unsupported value type: " + v.getClass());
			o.add(k, array);
		});
	}
	
	
	public static void addProperty(JsonObject base, String path, JsonElement value)
	{
		addProperty(base, path, (o, k) -> o.add(k, value));
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
		String remainingPathPart = path.substring(dotIndex + 1);
		
		var container = base.has(frontPathPart) ? base.getAsJsonObject(frontPathPart) : new JsonObject();
		if(!base.has(frontPathPart))
			base.add(frontPathPart, container);
		
		addProperty(container, remainingPathPart, addProperty);
	}
	
	
	@Nullable
	public static JsonElement getProperty(JsonElement baseElement, String path)
	{
		if(baseElement == null)
			return null;
		var segments = StringUtil.splitByLiteral(path, ".");
		if(segments.isEmpty())
			return baseElement;
		
		var e = baseElement;
		for(String segment : segments)
		{
			if(e == null || !e.isJsonObject())
				return null;
			e = e.getAsJsonObject().get(segment);
		}
		return e;
	}
	
}
