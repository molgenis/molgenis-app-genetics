package org.molgenis.ui.genenetwork.matrix.impl;

import org.molgenis.data.MolgenisDataException;
import org.molgenis.ui.genenetwork.matrix.service.MatrixMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MatrixMapperImpl implements MatrixMapper
{
	private Map<String, String> mapping;

	public MatrixMapperImpl(HashMap<String, String> mapping)
	{
		this.mapping = Objects.requireNonNull(mapping);
	}

	@Override
	public String map(String input)
	{
		String result = mapping.get(input);
		if (result == null)
		{
			throw new MolgenisDataException("the specified key [" + input + "] was not found in the mapping file");
		}
		return result;
	}
}
