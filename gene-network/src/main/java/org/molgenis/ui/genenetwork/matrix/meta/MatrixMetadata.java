package org.molgenis.ui.genenetwork.matrix.meta;

import org.molgenis.data.meta.AttributeType;
import org.molgenis.data.meta.SystemEntityType;
import org.molgenis.file.model.FileMetaMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.molgenis.data.meta.model.EntityType.AttributeRole.*;
import static org.molgenis.data.system.model.RootSystemPackage.PACKAGE_SYSTEM;

@Component
public class MatrixMetadata extends SystemEntityType
{
	public static final String FILE_LOCATION = "matrixFileLocation";
	public static final String ID = "id";
	public static final String SEPARATOR = "separator";
	public static final String COLUMN_MAPPING_FILE = "columnMappingFile";
	public static final String ROW_MAPPING_FILE = "rowMappingFile";
	public static final String SIMPLE_NAME = "Matrix";
	public static final String PACKAGE = PACKAGE_SYSTEM;
	private static final String COMMA = "COMMA";
	private static final String TAB = "TAB";
	private static final String SEMICOLON = "SEMICOLON";
	private static final String PIPE = "PIPE";
	private final List<String> separators = asList(COMMA, TAB, SEMICOLON, PIPE);

	public static final String MATRIX_METADATA = PACKAGE + "_" + SIMPLE_NAME;

	private final FileMetaMetaData fileMetaMetaData;

	@Autowired
	public MatrixMetadata(FileMetaMetaData fileMetaMetaData)
	{
		super(SIMPLE_NAME, PACKAGE);

		this.fileMetaMetaData = requireNonNull(fileMetaMetaData);
	}

	@Override
	public void init()
	{
		setLabel("Matrix metadata");
		setDescription("metadata with information about the matrix file");
		addAttribute(ID, ROLE_ID);
		addAttribute(FILE_LOCATION, ROLE_LABEL, ROLE_LOOKUP).setLabel("Location of the matrix file")
				.setNillable(false);
		addAttribute(SEPARATOR).setLabel("The seperator used in the matrix file").setNillable(false)
				.setDataType(AttributeType.ENUM).setEnumOptions(separators).setDefaultValue(COMMA);
		addAttribute(COLUMN_MAPPING_FILE).setDescription(
				"Optional mapping file to map search parameters to columnheaders (format 'matrixValue TAB mappedValue', file should contain a header line)")
				.setNillable(true).setDataType(AttributeType.FILE).setRefEntity(fileMetaMetaData);
		addAttribute(ROW_MAPPING_FILE).setDescription(
				"Optional mapping file to map search parameters to rowheaders (format 'matrixValue TAB mappedValue', file should contain a header line)")
				.setNillable(true).setDataType(AttributeType.FILE).setRefEntity(fileMetaMetaData);
	}

	public static char getSeparatorValue(String separatorName)
	{
		char value = ',';
		switch (separatorName)
		{
			case TAB:
				value = '\t';
				break;
			case COMMA:
				value = ',';
				break;
			case SEMICOLON:
				value = ';';
				break;
			case PIPE:
				value = '|';
				break;
		}
		return value;
	}
}
