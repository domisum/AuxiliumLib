package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@API
public class MultiException
		extends Exception
{
	
	@Getter
	private final Collection<Throwable> causes;
	
	
	// INIT
	@API
	public MultiException(String message, Collection<? extends Throwable> causes)
	{
		super(message);
		this.causes = Set.copyOf(causes);
	}
	
	@API
	public MultiException(Collection<? extends Throwable> causes)
	{
		this.causes = Set.copyOf(causes);
	}
	
	
	// GETTERS
	@Override
	public String getMessage()
	{
		var messageParts = new ArrayList<String>();
		for(var cause : causes)
			messageParts.add(cause.toString());
		
		String message = StringUtil.listToString(messageParts, "\n\n");
		
		return message;
	}
	
}
