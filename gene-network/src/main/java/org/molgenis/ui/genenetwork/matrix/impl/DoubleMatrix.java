package org.molgenis.ui.genenetwork.matrix.impl;

import gnu.trove.map.hash.TObjectIntHashMap;
import org.molgenis.data.MolgenisDataException;
import org.ujmp.core.Matrix;

import static java.util.Objects.requireNonNull;

class DoubleMatrix
{
	private Matrix matrix;
	private TObjectIntHashMap columnMap;
	private TObjectIntHashMap rowMap;

	private MatrixMapper columnMapper;
	private MatrixMapper rowMapper;

	DoubleMatrix(Matrix matrix, TObjectIntHashMap columnMap, TObjectIntHashMap rowMap)
	{
		this.matrix = requireNonNull(matrix);
		this.columnMap = requireNonNull(columnMap);
		this.rowMap = requireNonNull(rowMap);
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
		Integer columnIndex = columnMap.get(column);
		if (columnIndex == null) throw new MolgenisDataException("Unknown column name [" + column + "]");

		Integer rowIndex = rowMap.get(row);
		if (rowIndex == null) throw new MolgenisDataException("Unknown row name [" + row + "]");

		return getValueByIndex(rowIndex, columnIndex);
	}

	MatrixMapper getColumnMapper()
	{
		return this.columnMapper;
	}

	void setColumnMapper(MatrixMapper columnMapper)
	{
		this.columnMapper = columnMapper;
	}

	MatrixMapper getRowMapper()
	{
		return this.rowMapper;
	}

	void setRowMapper(MatrixMapper rowMapper)
	{
		this.rowMapper = rowMapper;
	}
}
