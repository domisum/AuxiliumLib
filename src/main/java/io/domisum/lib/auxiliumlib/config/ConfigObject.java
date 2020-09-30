package io.domisum.lib.auxiliumlib.config;

import io.domisum.lib.auxiliumlib.annotations.API;

import javax.annotation.Nonnull;

@API
public abstract class ConfigObject
{
	
	// INIT
	protected abstract void validate()
		throws ConfigException;
	
	
	// OBJECT
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(!(o instanceof ConfigObject))
			return false;
		
		var other = (ConfigObject) o;
		return getId().equals(other.getId());
	}
	
	@Override
	public int hashCode()
	{
		return getId().hashCode();
	}
	
	
	// GETTERS
	@Nonnull
	public abstract String getId();
	
}
