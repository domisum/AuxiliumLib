package de.domisum.lib.auxilium.contracts.generator;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.List;

@API
public interface MultiGenerator<I, O> extends Generator<I, List<O>> {}
