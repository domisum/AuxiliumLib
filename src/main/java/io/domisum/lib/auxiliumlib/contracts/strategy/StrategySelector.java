package io.domisum.lib.auxiliumlib.contracts.strategy;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@API
public class StrategySelector<T, StrategyT extends Strategy<T>>
{

	// ATTRIBUTES
	private final List<StrategyT> strategies;
	private final StrategyT fallbackStrategy;


	// INIT
	@API
	public StrategySelector(List<StrategyT> strategies)
	{
		this.strategies = Collections.unmodifiableList(strategies);
		fallbackStrategy = null;
	}

	@API
	public StrategySelector(List<StrategyT> strategies, StrategyT fallbackStrategy)
	{
		this.strategies = Collections.unmodifiableList(strategies);
		this.fallbackStrategy = fallbackStrategy;
	}


	// SELECT
	@API
	public Optional<StrategyT> selectFirstApplicable(T strategizedObject)
	{
		for(StrategyT s : strategies)
			if(s.doesApplyTo(strategizedObject))
				return Optional.of(s);

		return Optional.ofNullable(fallbackStrategy);
	}

	@API
	public List<StrategyT> selectAllApplicableFor(T strategizedObject)
	{
		List<StrategyT> applicable = new ArrayList<>();
		for(StrategyT strategy : strategies)
			if(strategy.doesApplyTo(strategizedObject))
				applicable.add(strategy);

		return applicable;
	}

}
