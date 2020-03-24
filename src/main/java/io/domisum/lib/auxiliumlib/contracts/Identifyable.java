package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.util.StringUtil;
import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Identifyable extends Keyable<String>
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
		List<String> strings = new ArrayList<>();
		for(Identifyable identifyable : identifyables)
			strings.add(identifyable.getId());

		return "["+StringUtil.listToString(strings, ", ")+"]";
	}

}
