package org.molgenis.ui.genenetwork.matrix.impl;

import org.apache.commons.lang3.StringUtils;
import org.molgenis.data.MolgenisDataException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Create {@link MatrixMapper} objects.
 * Globally Stores a Map<{@link String}, {@link MatrixMapper}> containing MatrixMapper object as value, with an absolute file path as key
 */
class MatrixMapperFactory
{
	private static Map<String, MatrixMapper> matrices = newHashMap();

	static MatrixMapper createMatrixMapper(File file)
	{
		String path = file.getAbsolutePath();
		if (matrices.containsKey(path))
		{
			return matrices.get(path);
		}
		else
		{
			HashMap<String, String> mapping = newHashMap();
			try
			{
				Scanner mappingScanner = new Scanner(file, "UTF-8");
				mappingScanner.nextLine(); // skip header
				while (mappingScanner.hasNext())
				{
					String from;
					String to;
					Scanner lineScanner = new Scanner(mappingScanner.nextLine());
					if (lineScanner.hasNext())
					{
						to = lineScanner.next();
						if (StringUtils.isNotEmpty(to))
						{
							if (lineScanner.hasNext())
							{
								from = lineScanner.next();
								if (StringUtils.isNotEmpty(from)) mapping.put(from, to);
							}
						}
					}
				}
			}
			catch (FileNotFoundException e)
			{
				throw new MolgenisDataException(e);
			}

			MatrixMapper matrixMapper = new MatrixMapper(mapping);

			matrices.put(path, matrixMapper);
			return matrixMapper;
		}
	}
}
