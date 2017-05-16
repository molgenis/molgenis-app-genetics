package org.molgenis.ui.genenetwork.job.meta;

import org.molgenis.data.AbstractSystemEntityFactory;
import org.molgenis.data.populate.EntityPopulator;
import org.molgenis.ui.genenetwork.job.GavinDiagJobExecution;
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