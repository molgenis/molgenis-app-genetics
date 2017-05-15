package org.molgenis.ui.genenetwork.matrix.impl;

import gnu.trove.map.hash.TObjectIntHashMap;
import org.molgenis.data.MolgenisDataException;
import org.ujmp.core.Matrix;

import static java.util.Objects.requireNonNull;

public class DoubleMatrix
{
	private Matrix matrix;

	private TObjectIntHashMap columnIndices;
	private TObjectIntHashMap rowIndices;

	private MatrixMapperImpl columnMapper;
	private MatrixMapperImpl rowMapper;

	public DoubleMatrix(Matrix matrix, TObjectIntHashMap columnIndices, TObjectIntHashMap rowIndices)
	{
		this.matrix = requireNonNull(matrix);
		this.columnIndices = requireNonNull(columnIndices);
		this.rowIndices = requireNonNull(rowIndices);
	}

	double getValueByIndex(int row, int column)
	{
		if (row > matrix.getRowCount())
		{
			throw new IndexOutOfBoundsException(
					"Index [" + row + "] is greater than the number of columns in the matrix [" + matrix.getRowCount()
							+ "]");
		}

		if (column > matrix.getColumnCount())
		{
			throw new IndexOutOfBoundsException(
					"Index [" + column + "] is greater than the number of columns in the matrix [" + matrix
							.getColumnCount() + "]");
		}

		Double result = matrix.getAsDouble(row, column);
		if (result.isNaN())
		{
			// Throw an exception. Our GSON parser does not like NaN in a double field
			throw new MolgenisDataException("No score found");
		}
		return result;
	}

	double getValueByName(String row, String column)
	{
		Integer columnIndex = columnIndices.get(column);
		if (columnIndex == null) throw new MolgenisDataException("Unknown column name [" + column + "]");

		Integer rowIndex = rowIndices.get(row);
		if (rowIndex == null) throw new MolgenisDataException("Unknown row name [" + row + "]");

		return getValueByIndex(rowIndex, columnIndex);
	}

	MatrixMapperImpl getColumnMapper()
	{
		return this.columnMapper;
	}

	void setColumnMapper(MatrixMapperImpl columnMapper)
	{
		this.columnMapper = columnMapper;
	}

	MatrixMapperImpl getRowMapper()
	{
		return this.rowMapper;
	}

	void setRowMapper(MatrixMapperImpl rowMapper)
	{
		this.rowMapper = rowMapper;
	}
}
