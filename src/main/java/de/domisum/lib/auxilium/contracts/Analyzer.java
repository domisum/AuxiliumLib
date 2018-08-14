package de.domisum.lib.auxilium.contracts;

import de.domisum.lib.auxilium.util.java.annotations.API;

@API
public interface Analyzer<InputT, OutputT>
{

	OutputT analyze(InputT input);

}
