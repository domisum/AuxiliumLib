package de.domisum.lib.auxilium.contracts;

import de.domisum.lib.auxilium.contracts.storage.Keyable;
import de.domisum.lib.auxilium.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Identifyable extends Keyable<String>
{

	String getId();

	@Override default String getKey()
	{
		return getId();
	}


	// UTIL
	static String getIdList(Collection<? extends Identifyable> identifyables)
	{
		List<String> strings = new ArrayList<>();
		for(Identifyable identifyable : identifyables)
			strings.add(identifyable.getId());

		return "["+StringUtil.listToString(strings, ", ")+"]";
	}

}
