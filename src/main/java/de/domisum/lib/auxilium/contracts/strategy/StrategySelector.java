package de.domisum.lib.auxilium.contracts.strategy;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class StrategySelector<T, StrategyT extends Strategy<T>>
{

	// ATTRIBUTES
	private final StrategyT defaultStrategy;
	private final List<StrategyT> strategies;


	// SELECT
	public Optional<StrategyT> selectFor(T strategizedObject)
	{
		for(StrategyT s : strategies)
			if(s.doesApplyTo(strategizedObject))
				return Optional.of(s);

		return Optional.ofNullable(defaultStrategy);
	}

}
