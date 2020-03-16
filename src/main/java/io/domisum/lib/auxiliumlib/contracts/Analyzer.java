package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

@API
public interface Analyzer<InputT, OutputT>
{

	OutputT analyze(InputT input);

}
