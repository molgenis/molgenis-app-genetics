package org.molgenis.ui.genenetwork.matrix.impl;

import org.molgenis.data.MolgenisDataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MatrixMapper
{
	private Map<String, String> mapping;

	MatrixMapper(HashMap<String, String> mapping)
	{
		this.mapping = Objects.requireNonNull(mapping);
	}

	public String map(String input)
	{
		String result = mapping.get(input);
		if (result == null)
		{
			throw new MolgenisDataException("the specified value [" + input + "] was not found in the mappingfile");
		}
		return result;
	}
}
