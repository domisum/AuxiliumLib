package io.domisum.lib.auxiliumlib.config;

import io.domisum.lib.auxiliumlib.annotations.API;

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
	public abstract Object getId();
	
	public String getStringId()
	{
		return getId().toString();
	}
	
}
