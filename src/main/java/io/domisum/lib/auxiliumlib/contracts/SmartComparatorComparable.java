package io.domisum.lib.auxiliumlib.contracts;

import javax.annotation.Nonnull;
import java.util.Comparator;

public interface SmartComparatorComparable<T extends SmartComparatorComparable<T>>
	extends SmartComparable<T>
{
	
	Comparator<T> COMPARATOR();
	
	
	@SuppressWarnings("unchecked")
	@Override
	default int compareTo(@Nonnull T other)
	{
		return COMPARATOR().compare((T) this, other);
	}
	
}
