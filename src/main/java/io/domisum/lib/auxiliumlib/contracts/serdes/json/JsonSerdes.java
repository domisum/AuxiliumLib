package io.domisum.lib.auxiliumlib.contracts.serdes.json;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.contracts.serdes.StringSerdes;

@API
public interface JsonSerdes<T>
	extends StringSerdes<T> {}
