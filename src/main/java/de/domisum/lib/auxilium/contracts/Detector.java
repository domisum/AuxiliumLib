package de.domisum.lib.auxilium.contracts;

import de.domisum.lib.auxilium.util.java.annotations.API;

@API
public interface Detector<InputT, OutputT>
{

	@API OutputT detect(InputT input);

}
