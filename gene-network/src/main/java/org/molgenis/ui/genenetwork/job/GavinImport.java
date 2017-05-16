package org.molgenis.ui.genenetwork.job;

import org.molgenis.data.DataService;
import org.molgenis.data.Entity;
import org.molgenis.data.meta.AttributeType;
import org.molgenis.data.meta.MetaDataService;
import org.molgenis.data.meta.model.*;
import org.molgenis.data.meta.model.Package;
import org.molgenis.data.support.DynamicEntity;
import org.molgenis.data.vcf.VcfRepository;
import org.molgenis.data.vcf.model.VcfAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class GavinImport
{
	public static final String DIAGNOSTICS = "diagnostics";
	public static final String GENE_NAME = "Gene_Name";
	public static final String GAVIN_REASON = "gavinReason";
	public static final String P_CHANGE = "pChange";
	public static final String C_DNA = "cDNA";
	public static final String CLASSIFICATION = "classification";
	private final VcfAttributes vcfAttributes;
	private final EntityTypeFactory entityTypeFactory;
	private final AttributeFactory attributeFactory;
	private final DataService dataService;
	private final MetaDataService metaDataService;

	@Autowired
	public GavinImport(VcfAttributes vcfAttributes, EntityTypeFactory entityTypeFactory,
			AttributeFactory attributeFactory, DataService dataService, MetaDataService metaDataService)
	{
		this.vcfAttributes = vcfAttributes;
		this.entityTypeFactory = entityTypeFactory;
		this.attributeFactory = attributeFactory;
		this.dataService = dataService;
		this.metaDataService = metaDataService;
	}

	public void importFile(File inputFile, String entityId, String label) throws IOException
	{
		VcfRepository repo = new VcfRepository(inputFile, entityId, vcfAttributes, entityTypeFactory, attributeFactory);

		Package diagPackage = metaDataService.getPackage(DIAGNOSTICS);
		Attribute geneAttr = attributeFactory.create().setName(GENE_NAME).setDataType(AttributeType.STRING);
		Attribute cDnaAttr = attributeFactory.create().setName(C_DNA).setDataType(AttributeType.STRING);
		Attribute pChangeAttr = attributeFactory.create().setName(P_CHANGE).setDataType(AttributeType.STRING);
		Attribute classAttr = attributeFactory.create().setName(CLASSIFICATION).setDataType(AttributeType.STRING);
		Attribute gavinAttr = attributeFactory.create().setName(GAVIN_REASON).setDataType(AttributeType.STRING);
		EntityType emd = repo.getEntityType();
		emd.setLabel(label);
		emd.addAttributes(Arrays.asList(geneAttr, gavinAttr, pChangeAttr, cDnaAttr, classAttr));
		emd.setPackage(diagPackage);
		metaDataService.addEntityType(emd);
		dataService.add(emd.getId(), StreamSupport.stream(repo.spliterator(), false).filter(entity -> hasEffect(entity))
				.flatMap(entity -> getVariantEntity(emd, entity)));
	}

	private Stream<Entity> getVariantEntity(EntityType emd, Entity entity)
	{
		String[] effectArray = getEffectArray(entity);
		String gene = effectArray[1];
		String cDNA = effectArray[9];
		String pChange = effectArray[10];
		String classification = effectArray[16];
		String reason = effectArray[18];

		Entity variant = new DynamicEntity(emd);
		variant.set(entity);
		variant.set(GENE_NAME, gene);
		variant.set(P_CHANGE, pChange);
		variant.set(C_DNA, cDNA);
		variant.set(GAVIN_REASON, reason);
		variant.set(CLASSIFICATION, classification);
		return Collections.singletonList(variant).stream();
	}

	private boolean hasEffect(Entity entity)
	{
		String[] effectArray = getEffectArray(entity);
		return effectArray.length > 17;
	}

	private String[] getEffectArray(Entity entity)
	{
		String effects = entity.getString("EFFECT");
		String effect = effects.split(",")[0];//FIXME: process multiple effects
		return effect.split("\\|");
	}
}
