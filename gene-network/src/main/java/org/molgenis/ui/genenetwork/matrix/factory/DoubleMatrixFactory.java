package org.molgenis.ui.genenetwork.matrix.factory;

import gnu.trove.map.hash.TObjectIntHashMap;
import org.molgenis.data.MolgenisDataException;
import org.molgenis.ui.genenetwork.matrix.impl.DoubleMatrix;
import org.ujmp.core.Matrix;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.ujmp.core.Matrix.Factory;

public class DoubleMatrixFactory
{
	private static Map<String, DoubleMatrix> matrices = newHashMap();

	public static DoubleMatrix createDoubleMatrix(File file, char separator)
	{
		String path = file.getAbsolutePath();
		if (matrices.containsKey(path))
		{
			return matrices.get(path);
		}
		else
		{
			try
			{
				Matrix matrix = Factory.linkTo().file(path).asDenseCSV(separator);

				TObjectIntHashMap columnMap = setColumnIndicesMap(matrix);
				TObjectIntHashMap rowMap = setRowIndicesMap(matrix);

				DoubleMatrix doubleMatrix = new DoubleMatrix(matrix, columnMap, rowMap);
				matrices.put(path, doubleMatrix);
				return doubleMatrix;
			}
			catch (IOException e)
			{
				throw new MolgenisDataException(e);
			}
		}
	}

	private static TObjectIntHashMap setRowIndicesMap(Matrix matrix)
	{
		TObjectIntHashMap map = new TObjectIntHashMap();
		int i = 0;
		String gene;
		while (i < matrix.getRowCount())
		{
			gene = matrix.getAsString(i, 0);
			map.put(gene, i);
			i++;
		}
		return map;
	}

	private static TObjectIntHashMap setColumnIndicesMap(Matrix matrix)
	{
		TObjectIntHashMap map = new TObjectIntHashMap();
		int i = 0;
		String hpo;
		while (i < matrix.getColumnCount())
		{
			hpo = matrix.getAsString(0, i);
			map.put(hpo, i);
			i++;
		}
		return map;
	}
}
