package org.molgenis.ui.genenetwork.matrix;

import org.molgenis.ui.genenetwork.matrix.model.Score;

import java.util.List;

public interface MatrixService
{
	/**
	 * Retrieve one {@link Score}
	 *
	 * @param entityName The id of the entity in the Matrix Metadata
	 * @param row        One row id
	 * @param column     One column id
	 * @return
	 */
	Object getValueByIndex(String entityName, int row, int column);

	/**
	 * Retrieve a list of {@link Score}s based on one column name and a comma separated string of row names
	 *
	 * @param entityName The id of the entity in the Matrix Metadata
	 * @param row        A comma separated string containing row id's
	 * @param column     A column id
	 * @return
	 */
	List<Score> getValueByNames(String entityName, String row, String column);
}
