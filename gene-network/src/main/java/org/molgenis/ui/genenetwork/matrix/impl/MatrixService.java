package org.molgenis.ui.genenetwork.matrix.impl;

import org.molgenis.data.DataService;
import org.molgenis.data.Entity;
import org.molgenis.data.MolgenisDataException;
import org.molgenis.file.FileStore;
import org.molgenis.file.model.FileMeta;
import org.molgenis.ui.genenetwork.matrix.meta.MatrixMetadata;
import org.molgenis.ui.genenetwork.matrix.model.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.molgenis.ui.genenetwork.matrix.impl.DoubleMatrixFactory.createDoubleMatrix;
import static org.molgenis.ui.genenetwork.matrix.impl.MatrixMapperFactory.createMatrixMapper;
import static org.molgenis.ui.genenetwork.matrix.meta.MatrixMetadata.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Service
@RequestMapping("/api/matrix")
public class MatrixService
{
	private DataService dataService;
	private FileStore fileStore;

	@Autowired
	public MatrixService(DataService dataService, FileStore fileStore)
	{
		this.dataService = requireNonNull(dataService);
		this.fileStore = requireNonNull(fileStore);
	}

	/**
	 * Retrieve one {@link Score}
	 *
	 * @param entityName The id of the entity in the Matrix Metadata
	 * @param row        One row id
	 * @param column     One column id
	 * @return A GeneNetwork score as a {@link Double} value
	 */
	@RequestMapping(value = "/{entityId}/valueByIndex", method = GET)
	@ResponseBody
	public Object getValueByIndex(@PathVariable("entityId") String entityName, @RequestParam("row") int row,
			@RequestParam("column") int column) throws MolgenisDataException
	{
		DoubleMatrix matrix = getMatrixByEntityTypeId(entityName);
		return matrix.getValueByIndex(row, column);
	}

	/**
	 * Retrieve a list of {@link Score}s based on one column name and a comma separated string of row names
	 *
	 * @param entityName The id of the entity in the Matrix Metadata
	 * @param rows       A comma separated string containing row id's
	 * @param columns    A column id
	 * @return A list of {@link Score}s
	 */
	@RequestMapping(value = "/{entityId}/valueByNames", method = GET)
	@ResponseBody
	public List<Score> getValueByNames(@PathVariable("entityId") String entityName, @RequestParam("rows") String rows,
			@RequestParam("columns") String columns) throws MolgenisDataException
	{
		List<Score> results = new ArrayList<>();
		DoubleMatrix matrix = getMatrixByEntityTypeId(entityName);

		for (String row : rows.split(","))
		{
			MatrixMapper rowMapper = matrix.getRowMapper();
			String translatedRow;
			if (rowMapper != null) translatedRow = rowMapper.map(row);
			else translatedRow = row;
			for (String column : columns.split(","))
			{
				MatrixMapper columnMapper = matrix.getColumnMapper();
				if (columnMapper != null) column = columnMapper.map(column);
				results.add(Score.createScore(column, row, matrix.getValueByName(translatedRow, column)));
			}
		}
		return results;
	}

	private DoubleMatrix getMatrixByEntityTypeId(String entityId)
	{
		Entity entity = dataService.findOneById(MATRIX_METADATA, entityId);
		if (entity != null)
		{
			return getMatrix(entity);
		}
		throw new MolgenisDataException("Unknown Matrix Metadata EntityID [" + entityId + "]");
	}

	private DoubleMatrix getMatrix(Entity entity)
	{
		String fileLocation = entity.getString(MatrixMetadata.FILE_LOCATION);
		char separator = getSeparatorValue(entity.getString(MatrixMetadata.SEPARATOR));

		DoubleMatrix doubleMatrix = createDoubleMatrix(new File(fileLocation), separator);

		if (entity.getEntity(COLUMN_MAPPING_FILE) != null) doubleMatrix.setRowMapper(getMapper(entity, COLUMN_MAPPING_FILE));
		if (entity.getEntity(ROW_MAPPING_FILE) != null) doubleMatrix.setRowMapper(getMapper(entity, ROW_MAPPING_FILE));

		return doubleMatrix;
	}

	private MatrixMapper getMapper(Entity entity, String mapping)
	{
		FileMeta meta = entity.getEntity(mapping, FileMeta.class);
		File file = fileStore.getFile(meta.getId());
		return createMatrixMapper(file);
	}
}
