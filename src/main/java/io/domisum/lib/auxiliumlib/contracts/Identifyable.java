package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;

public interface Identifyable
		extends Keyable<String>
{
	
	String getId();
	
	@Override
	default String getKey()
	{
		return getId();
	}
	
	
	// UTIL
	@API
	static String getIdList(Collection<? extends Identifyable> identifyables)
	{
		var strings = new ArrayList<String>();
		for(var identifyable : identifyables)
			strings.add(identifyable.getId());
		
		return "["+StringUtil.listToString(strings, ", ")+"]";
	}
	
}
