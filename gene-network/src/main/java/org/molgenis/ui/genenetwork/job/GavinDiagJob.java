package org.molgenis.ui.genenetwork.job;

import com.google.common.collect.Multiset;
import org.molgenis.data.MolgenisDataException;
import org.molgenis.data.annotation.core.RepositoryAnnotator;
import org.molgenis.data.jobs.Job;
import org.molgenis.data.jobs.Progress;
import org.molgenis.file.FileStore;
import org.molgenis.gavin.job.AnnotatorRunner;
import org.molgenis.gavin.job.input.Parser;
import org.molgenis.gavin.job.input.model.LineType;
import org.molgenis.ui.menu.MenuReaderService;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;

import static java.io.File.separator;
import static java.text.MessageFormat.format;

public class GavinDiagJob extends Job<Void>
{
	private final String jobIdentifier;
	private final MenuReaderService menuReaderService;
	private final FileStore fileStore;

	private final RepositoryAnnotator cadd;
	private final RepositoryAnnotator exac;
	private final RepositoryAnnotator snpeff;
	private final RepositoryAnnotator gavin;

	private final File inputFile;
	private final File processedInputFile;
	private final File errorFile;
	private final File caddOutputFile;
	private final File exacOutputFile;
	private final File snpeffOutputFile;
	private final File gavinOutputFile;

	private final Parser parser;
	private final AnnotatorRunner annotatorRunner;
	private final GavinDiagJobExecution gavinJobExecution;
	private final GavinImport gavinImport;

	public GavinDiagJob(Progress progress, TransactionTemplate transactionTemplate, Authentication authentication,
			String jobIdentifier, FileStore fileStore, MenuReaderService menuReaderService, RepositoryAnnotator cadd,
			RepositoryAnnotator exac, RepositoryAnnotator snpeff, RepositoryAnnotator gavin, Parser parser,
			AnnotatorRunner annotatorRunner, GavinDiagJobExecution gavinJobExecution, GavinImport gavinImport)
	{
		super(progress, transactionTemplate, authentication);
		this.fileStore = fileStore;
		this.jobIdentifier = jobIdentifier;
		this.menuReaderService = menuReaderService;
		this.cadd = cadd;
		this.exac = exac;
		this.snpeff = snpeff;
		this.gavin = gavin;
		this.annotatorRunner = annotatorRunner;
		this.parser = parser;
		this.gavinJobExecution = gavinJobExecution;
		this.gavinImport = gavinImport;

		inputFile = getFile("input", gavinJobExecution.getInputFileExtension());
		processedInputFile = getFile("temp-processed-input");
		errorFile = getFile("error", "txt");
		caddOutputFile = getFile("temp-cadd");
		exacOutputFile = getFile("temp-exac");
		snpeffOutputFile = getFile("temp-snpeff");
		gavinOutputFile = getFile("gavin-result");
	}

	private File getFile(String name)
	{
		return getFile(name, "vcf");
	}

	private File getFile(String name, String extension)
	{
		return fileStore.getFile(
				format("{0}{1}{2}{3}{4}.{5}", DiagnosticsController.DIAGNOSTICS, separator, jobIdentifier, separator,
						name, extension));
	}

	@Override
	public Void call(Progress progress) throws Exception
	{
		progress.setProgressMax(5);

		progress.progress(0, "Preprocessing input file...");
		Multiset<LineType> lineTypes = parser.tryTransform(inputFile, processedInputFile, errorFile);
		progress.status(format("Parsed input file. Found {0} lines ({1} comments, {2} valid VCF, {3} valid CADD, "
						+ "{4} errors, {5} indels without CADD score, {6} skipped)", lineTypes.size(),
				lineTypes.count(LineType.COMMENT), lineTypes.count(LineType.VCF), lineTypes.count(LineType.CADD),
				lineTypes.count(LineType.ERROR), lineTypes.count(LineType.INDEL_NOCADD),
				lineTypes.count(LineType.SKIPPED)));
		gavinJobExecution.setLineTypes(lineTypes);
		if (lineTypes.contains(LineType.SKIPPED))
		{
			throw new MolgenisDataException(
					MessageFormat.format("Input file contains too many lines. Maximum is {0}.", Parser.MAX_LINES));
		}
		if (lineTypes.containsAll(Arrays.asList(LineType.CADD, LineType.VCF)))
		{
			throw new MolgenisDataException(
					"Input file contains mixed line types. Please use one type only, either VCF or CADD.");
		}

		if (!lineTypes.contains(LineType.CADD) && !lineTypes.contains(LineType.VCF))
		{
			throw new MolgenisDataException("Not a single valid variant line found.");
		}

		File exacInputFile = processedInputFile;
		if (!lineTypes.contains(LineType.CADD))
		{
			progress.progress(1, "Annotating with cadd...");
			annotatorRunner.runAnnotator(cadd, processedInputFile, caddOutputFile, true);
			exacInputFile = caddOutputFile;
		}
		else
		{
			progress.progress(1, "File already annotated by cadd, skipping cadd annotation.");
		}

		progress.progress(2, "Annotating with exac...");
		annotatorRunner.runAnnotator(exac, exacInputFile, exacOutputFile, true);

		progress.progress(3, "Annotating with snpEff...");
		annotatorRunner.runAnnotator(snpeff, exacOutputFile, snpeffOutputFile, false);

		progress.progress(4, "Annotating with gavin...");
		annotatorRunner.runAnnotator(gavin, snpeffOutputFile, gavinOutputFile, false);

		progress.progress(5, "Result is copied to the database.");
		gavinImport.importFile(gavinOutputFile, gavinJobExecution.getIdentifier(), gavinJobExecution.getFilename());

		progress.progress(6, format("The variants that were uploaded to the database."));
		progress.progress(6, format("Result is ready to view in the diagnostics plugin."));
		String path = menuReaderService.getMenu().findMenuItemPath("gene-network");
		progress.setResultUrl(format("{0}/{1}/{2}", path, "patients", gavinJobExecution.getIdentifier()));
		return null;
	}

}
