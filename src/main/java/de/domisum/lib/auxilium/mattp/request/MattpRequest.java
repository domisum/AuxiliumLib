package de.domisum.lib.auxilium.mattp.request;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class MattpRequest
{

	@Getter private final AbstractURL url;
	@Getter private final MattpMethod mattpMethod;

	private final List<MattpHeader> headers = new ArrayList<>();
	@Getter @Setter private MattpRequestBody body; // TODO check if body is allowed with method


	// INIT
	@API public static MattpRequest get(AbstractURL url)
	{
		return new MattpRequest(url, MattpMethod.GET);
	}

	@API public void addHeader(String key, String value)
	{
		addHeader(new MattpHeader(key, value));
	}

	@API public void addHeader(MattpHeader header)
	{
		headers.add(header);
	}


	// GETTERS
	public List<MattpHeader> getHeaders()
	{
		return Collections.unmodifiableList(headers);
	}

}
