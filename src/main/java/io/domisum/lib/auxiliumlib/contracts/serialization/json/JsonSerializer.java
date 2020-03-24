package io.domisum.lib.auxiliumlib.contracts.serialization.json;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.contracts.serialization.ToStringSerializer;

@API
public interface JsonSerializer<T>
		extends ToStringSerializer<T> {}
