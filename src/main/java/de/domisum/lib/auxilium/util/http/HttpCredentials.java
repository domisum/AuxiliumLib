package de.domisum.lib.auxilium.util.http;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// CREDENTIALS
@API
@RequiredArgsConstructor
public class HttpCredentials
{

	@Getter
	private final String username;
	@Getter
	private final String password;

}
