package org.molgenis.ui.genenetwork.job;

import org.molgenis.data.AbstractSystemEntityFactory;
import org.molgenis.data.populate.EntityPopulator;
import org.molgenis.gavin.job.GavinJobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GavinDiagJobExecutionFactory
		extends AbstractSystemEntityFactory<GavinDiagJobExecution, GavinDiagJobExecutionMetaData, String>
{
	@Autowired
	GavinDiagJobExecutionFactory(GavinDiagJobExecutionMetaData gavinJobExecutionMetaData,
			EntityPopulator entityPopulator)
	{
		super(GavinDiagJobExecution.class, gavinJobExecutionMetaData, entityPopulator);
	}
}