package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public interface Detector<InputT, OutputT>
{

	@API
	OutputT detect(InputT input);

}
