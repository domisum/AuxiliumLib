package io.domisum.lib.auxiliumlib.contracts.generator;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

import java.util.List;

@API
public interface MultiGenerator<I, O> extends Generator<I, List<O>> {}
