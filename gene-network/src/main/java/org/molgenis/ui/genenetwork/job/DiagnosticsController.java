package org.molgenis.ui.genenetwork.job;

import org.molgenis.data.populate.IdGenerator;
import org.molgenis.file.FileStore;
import org.molgenis.gavin.job.JobNotFoundException;
import org.molgenis.security.user.UserAccountService;
import org.molgenis.ui.MolgenisPluginController;
import org.molgenis.ui.genenetwork.job.meta.GavinDiagJobExecutionFactory;
import org.molgenis.ui.menu.MenuReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.io.File.separator;
import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;
import static org.molgenis.ui.genenetwork.job.DiagnosticsController.URI;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(URI)
public class DiagnosticsController extends MolgenisPluginController
{
	public static final String DIAGNOSTICS = "diagnostics";
	public static final String URI = PLUGIN_URI_PREFIX + DIAGNOSTICS;

	public static final String TSV_GZ = "tsv.gz";
	public static final String TSV = "tsv";
	public static final String GZ = "gz";

	private final ExecutorService executorService;
	private final GavinDiagJobFactory gavinJobFactory;
	private final GavinDiagJobExecutionFactory gavinJobExecutionFactory;
	private final FileStore fileStore;
	private final UserAccountService userAccountService;
	private final IdGenerator idGenerator;
	private final MenuReaderService menuReaderService;

	@Autowired
	public DiagnosticsController(@Qualifier("gavinExecutors") ExecutorService executorService,
			GavinDiagJobFactory gavinJobFactory, GavinDiagJobExecutionFactory gavinJobExecutionFactory,
			FileStore fileStore, UserAccountService userAccountService, MenuReaderService menuReaderService,
			IdGenerator idGenerator)
	{
		super(URI);
		this.menuReaderService = menuReaderService;
		this.executorService = requireNonNull(executorService);
		this.gavinJobFactory = requireNonNull(gavinJobFactory);
		this.gavinJobExecutionFactory = requireNonNull(gavinJobExecutionFactory);
		this.fileStore = requireNonNull(fileStore);
		this.userAccountService = requireNonNull(userAccountService);
		this.idGenerator = idGenerator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String init(Model model)
	{
		List<String> annotatorsWithMissingResources = gavinJobFactory.getAnnotatorsWithMissingResources();
		if (!annotatorsWithMissingResources.isEmpty())
		{
			model.addAttribute("annotatorsWithMissingResources", annotatorsWithMissingResources);
		}
		return "view-gavin-diag";
	}

	/**
	 * Starts a job to annotate a VCF file
	 *
	 * @param inputFile    the uploaded input file
	 * @param entityTypeId the name of the file
	 * @return the ID of the created {@link GavinDiagJobExecution}
	 * @throws IOException if interaction with the file store fails
	 */
	@RequestMapping(value = "/annotate-file", method = POST)
	public ResponseEntity<String> annotateFile(@RequestParam(value = "file") MultipartFile inputFile,
			@RequestParam String entityTypeId) throws IOException
	{
		String extension = TSV;
		if (inputFile.getOriginalFilename().endsWith(GZ))
		{
			extension = TSV_GZ;
		}

		final GavinDiagJobExecution gavinJobExecution = gavinJobExecutionFactory.create(idGenerator.generateId());
		gavinJobExecution.setFilename(entityTypeId);
		gavinJobExecution.setUser(userAccountService.getCurrentUser().getUsername());
		gavinJobExecution.setInputFileExtension(extension);
		final GavinDiagJob gavinJob = gavinJobFactory.createJob(gavinJobExecution);

		final String gavinJobIdentifier = gavinJobExecution.getIdentifier();
		fileStore.createDirectory(DIAGNOSTICS);
		final String jobDir = format("{0}{1}{2}", DIAGNOSTICS, separator, gavinJobIdentifier);
		fileStore.createDirectory(jobDir);

		final String fileName = format("{0}{1}input.{2}", jobDir, separator, extension);
		fileStore.writeToFile(inputFile.getInputStream(), fileName);

		executorService.submit(gavinJob);

		String location = "/plugin/gn-app/job/" + gavinJobIdentifier;
		return ResponseEntity.created(java.net.URI.create(location)).body(location);
	}

	@RequestMapping(value = "/result/{jobIdentifier}", method = GET)
	public String result(@PathVariable(value = "jobIdentifier") String jobIdentifier, Model model,
			HttpServletRequest request) throws JobNotFoundException
	{
		model.addAttribute("jobExecution", gavinJobFactory.findGavinJobExecution(jobIdentifier));
		model.addAttribute("downloadFileExists", getDownloadFileForJob(jobIdentifier).exists());
		model.addAttribute("errorFileExists", getErrorFileForJob(jobIdentifier).exists());
		model.addAttribute("pageUrl", getPageUrl(jobIdentifier, request));
		String path = menuReaderService.getMenu().findMenuItemPath("gene-network");
		model.addAttribute("result", format("{0}/{1}/{2}", path, "patients", jobIdentifier));
		return "view-gavin-diag-result";
	}

	private String getPageUrl(String jobIdentifier, HttpServletRequest request)
	{
		String host;
		if (StringUtils.isEmpty(request.getHeader("X-Forwarded-Host")))
		{
			host = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort();
		}
		else
		{
			host = request.getScheme() + "://" + request.getHeader("X-Forwarded-Host");
		}
		return format("{0}{1}/result/{2}", host, menuReaderService.getMenu().findMenuItemPath(DIAGNOSTICS),
				jobIdentifier);
	}

	private File getDownloadFileForJob(String jobIdentifier)
	{
		return fileStore.getFile(DIAGNOSTICS + separator + jobIdentifier + separator + "gavin-result.vcf");
	}

	private File getErrorFileForJob(String jobIdentifier)
	{
		return fileStore.getFile(DIAGNOSTICS + separator + jobIdentifier + separator + "error.txt");
	}

	@RequestMapping(value = "/job/{jobIdentifier}", method = GET, produces = APPLICATION_JSON_VALUE)
	public @ResponseBody
	GavinDiagJobExecution getGavinJobExecution(@PathVariable(value = "jobIdentifier") String jobIdentifier)
			throws JobNotFoundException
	{
		return gavinJobFactory.findGavinJobExecution(jobIdentifier);
	}
}
