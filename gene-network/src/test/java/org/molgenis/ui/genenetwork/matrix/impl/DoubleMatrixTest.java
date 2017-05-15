package org.molgenis.ui.genenetwork.matrix.impl;

import org.molgenis.util.ResourceUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.molgenis.ui.genenetwork.matrix.factory.DoubleMatrixFactory.createDoubleMatrix;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DoubleMatrixTest
{

	private DoubleMatrix doubleMatrix;
	private DoubleMatrix doubleMatrix2;

	@BeforeTest
	public void setup()
	{
		doubleMatrix = createDoubleMatrix(ResourceUtils.getFile(getClass(), "/testmatrix.txt"), '\t');
		doubleMatrix2 = createDoubleMatrix(ResourceUtils.getFile(getClass(), "/testmatrix2.txt"), '\t');
	}

	@Test
	public void matrixValueByIndex()
	{
		assertTrue(doubleMatrix.getValueByIndex(2, 1) == 2.123);
		assertTrue(doubleMatrix.getValueByIndex(1, 2) == 1.234);
		assertTrue(doubleMatrix.getValueByIndex(1, 3) == 1.345);
	}

	@Test
	public void matrixTest()
	{
		assertTrue(doubleMatrix.getValueByName("gene1", "hpo123") == 1.123);
		assertTrue(doubleMatrix.getValueByName("gene2", "hpo123") == 2.123);
		assertTrue(doubleMatrix.getValueByName("gene3", "hpo345") == 3.345);
	}

	@Test
	public void matrixTest2()
	{
		assertEquals(doubleMatrix2.getValueByName("BRCA1", "HP_0100280"), 1.123);
		assertEquals(doubleMatrix2.getValueByName("BRCA2", "HP_0001249"), 2.234);
	}

}
