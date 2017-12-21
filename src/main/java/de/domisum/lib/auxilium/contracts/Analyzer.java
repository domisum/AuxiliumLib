package de.domisum.lib.auxilium.contracts;

public interface Analyzer<InputT, OutputT>
{

	OutputT analyze(InputT input);

}
