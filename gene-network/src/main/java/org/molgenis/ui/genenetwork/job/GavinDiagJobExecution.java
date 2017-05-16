package org.molgenis.ui.genenetwork.job;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import org.molgenis.data.Entity;
import org.molgenis.data.jobs.model.JobExecution;
import org.molgenis.data.meta.model.EntityType;
import org.molgenis.gavin.job.input.model.LineType;
import org.molgenis.gavin.job.meta.GavinJobExecutionMetaData;

public class GavinDiagJobExecution extends JobExecution
{
	private static final long serialVersionUID = 1L;
	private static final String GAVIN_DIAG = "gavin-diag";

	public GavinDiagJobExecution(Entity entity)
	{
		super(entity);
		setType(GAVIN_DIAG);
	}

	public GavinDiagJobExecution(EntityType entityType)
	{
		super(entityType);
		setType(GAVIN_DIAG);
	}

	public GavinDiagJobExecution(String identifier, EntityType entityType)
	{
		super(identifier, entityType);
		setType(GAVIN_DIAG);
	}

	public String getFilename()
	{
		return getString(GavinJobExecutionMetaData.FILENAME);
	}

	public void setFilename(String filename)
	{
		set(GavinJobExecutionMetaData.FILENAME, filename);
	}

	public String getInputFileExtension()
	{
		return getString(GavinJobExecutionMetaData.INPUT_FILE_EXTENSION);
	}

	public void setInputFileExtension(String extension)
	{
		set(GavinJobExecutionMetaData.INPUT_FILE_EXTENSION, extension);
	}

	public void setLineTypes(Multiset<LineType> lineTypes)
	{
		set(GavinJobExecutionMetaData.COMMENTS, lineTypes.count(LineType.COMMENT));
		set(GavinJobExecutionMetaData.VCFS, lineTypes.count(LineType.VCF));
		set(GavinJobExecutionMetaData.CADDS, lineTypes.count(LineType.CADD));
		set(GavinJobExecutionMetaData.ERRORS, lineTypes.count(LineType.ERROR));
		set(GavinJobExecutionMetaData.INDELS_NOCADD, lineTypes.count(LineType.INDEL_NOCADD));
		set(GavinJobExecutionMetaData.SKIPPEDS, lineTypes.count(LineType.SKIPPED));
	}

	public Multiset<LineType> getLineTypes()
	{
		ImmutableMultiset.Builder<LineType> builder = ImmutableMultiset.builder();
		builder.addCopies(LineType.COMMENT, getInt(GavinJobExecutionMetaData.COMMENTS));
		builder.addCopies(LineType.VCF, getInt(GavinJobExecutionMetaData.VCFS));
		builder.addCopies(LineType.CADD, getInt(GavinJobExecutionMetaData.CADDS));
		builder.addCopies(LineType.ERROR, getInt(GavinJobExecutionMetaData.ERRORS));
		builder.addCopies(LineType.INDEL_NOCADD, getInt(GavinJobExecutionMetaData.INDELS_NOCADD));
		builder.addCopies(LineType.SKIPPED, getInt(GavinJobExecutionMetaData.SKIPPEDS));
		return builder.build();
	}

}
