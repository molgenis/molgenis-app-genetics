package org.molgenis.ui.genenetwork.matrix.service;

import java.util.HashMap;

public interface MatrixMapper
{
	/**
	 * @param input {@link String} used as key to retrieve a value from a MatrixMapper {@link HashMap}
	 * @return a value {@link String} matching the input key
	 */
	String map(String input);
}
