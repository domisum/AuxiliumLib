package de.domisum.lib.auxilium.contracts.strategy;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class StrategySelector<T, StrategyT extends Strategy<T>>
{

	// ATTRIBUTES
	private final List<StrategyT> strategies;
	private final StrategyT fallbackStrategy;


	// INIT
	public StrategySelector(List<StrategyT> strategies)
	{
		fallbackStrategy = null;
		this.strategies = Collections.unmodifiableList(strategies);
	}


	// SELECT
	public Optional<StrategyT> selectFirstApplicable(T strategizedObject)
	{
		for(StrategyT s : strategies)
			if(s.doesApplyTo(strategizedObject))
				return Optional.of(s);

		return Optional.ofNullable(fallbackStrategy);
	}

	public List<StrategyT> selectAllApplicableFor(T strategizedObject)
	{
		List<StrategyT> applicable = new ArrayList<>();
		for(StrategyT strategy : strategies)
			if(strategy.doesApplyTo(strategizedObject))
				applicable.add(strategy);

		return applicable;
	}

}
