package org.molgenis.ui.genenetwork.matrix.impl;

import org.molgenis.data.MolgenisDataException;
import org.molgenis.util.ResourceUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.molgenis.ui.genenetwork.matrix.impl.MatrixMapperFactory.createMatrixMapper;
import static org.testng.Assert.assertEquals;

public class MatrixMapperTest
{
	private MatrixMapper matrixMapper;

	@BeforeTest
	public void setup()
	{
		matrixMapper = createMatrixMapper(ResourceUtils.getFile(getClass(), "/mapping.txt"));
	}

	@Test
	public void testMapping()
	{
		assertEquals(matrixMapper.map("mapping1"), "matrix1");
		assertEquals(matrixMapper.map("mapping4"), "matrix4");
	}

	@Test(expectedExceptions = MolgenisDataException.class, expectedExceptionsMessageRegExp = "the specified value \\[foo\\] was not found in the mappingfile")
	public void testMappingInvalid()
	{
		matrixMapper.map("foo");
	}
}
