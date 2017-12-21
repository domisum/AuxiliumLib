package de.domisum.lib.auxilium.contracts;

public interface Analyzer<I, O>
{

	O analyze(I input);

}
