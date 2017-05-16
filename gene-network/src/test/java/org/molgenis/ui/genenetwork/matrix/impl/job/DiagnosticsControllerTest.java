package org.molgenis.ui.genenetwork.matrix.impl.job;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.molgenis.auth.User;
import org.molgenis.data.AbstractMolgenisSpringTest;
import org.molgenis.data.DataService;
import org.molgenis.data.annotation.core.EffectBasedAnnotator;
import org.molgenis.data.annotation.core.RepositoryAnnotator;
import org.molgenis.data.annotation.web.CrudRepositoryAnnotator;
import org.molgenis.data.index.meta.IndexPackage;
import org.molgenis.data.jobs.JobExecutionUpdater;
import org.molgenis.file.FileStore;
import org.molgenis.framework.ui.MolgenisPluginRegistry;
import org.molgenis.security.user.UserAccountService;
import org.molgenis.ui.controller.StaticContentService;
import org.molgenis.ui.genenetwork.job.*;
import org.molgenis.ui.menu.MenuReaderService;
import org.molgenis.util.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutorService;

import static java.io.File.separator;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@ContextConfiguration(classes = { DiagnosticsControllerTest.Config.class, DiagnosticsController.class })
public class DiagnosticsControllerTest extends AbstractMolgenisSpringTest
{
	@Autowired
	private DiagnosticsController diagnosticsController;

	@Autowired
	private ExecutorService executorService;

	@Autowired
	private GavinDiagJobFactory gavinJobFactory;

	@Autowired
	private FileStore fileStore;

	@Autowired
	private UserAccountService userAccountService;

	@Test
	public void testInitResourcesPresent() throws Exception
	{
		Model model = new ExtendedModelMap();
		when(gavinJobFactory.getAnnotatorsWithMissingResources()).thenReturn(emptyList());

		diagnosticsController.init(model);

		assertFalse(model.containsAttribute("annotatorsWithMissingResources"));
	}

	@Test
	public void testInitMissingResources() throws Exception
	{
		Model model = new ExtendedModelMap();
		when(gavinJobFactory.getAnnotatorsWithMissingResources()).thenReturn(singletonList("cadd"));

		diagnosticsController.init(model);

		assertEquals(model.asMap().get("annotatorsWithMissingResources"), singletonList("cadd"));
	}

	@Test
	public void testAnnotateFile() throws Exception
	{
		MultipartFile vcf = mock(MultipartFile.class);
		GavinDiagJob job = mock(GavinDiagJob.class);
		File inputFile = mock(File.class);
		File parentDir = mock(File.class);
		User user = mock(User.class);
		when(user.getUsername()).thenReturn("tommy");

		// Job Factory sets the Identifier in the JobExecution object.
		ArgumentCaptor<GavinDiagJobExecution> captor = ArgumentCaptor.forClass(GavinDiagJobExecution.class);
		when(userAccountService.getCurrentUser()).thenReturn(user);
		doAnswer(invocation ->
		{
			Object[] args = invocation.getArguments();
			((GavinDiagJobExecution) args[0]).setIdentifier("qwerty");
			return job;
		}).when(gavinJobFactory).createJob(captor.capture());

		when(inputFile.getParentFile()).thenReturn(parentDir);
		when(vcf.getOriginalFilename()).thenReturn(".vcf");

		assertEquals(diagnosticsController.annotateFile(vcf, "annotate-file"),
				ResponseEntity.created(URI.create("/plugin/gn-app/job/qwerty")).body("/plugin/gn-app/job/qwerty"));

		verify(fileStore).createDirectory("diagnostics");
		verify(fileStore).createDirectory("diagnostics" + separator + "qwerty");
		verify(fileStore).writeToFile(Mockito.any(InputStream.class),
				Mockito.eq("diagnostics" + separator + "qwerty" + separator + "input.tsv"));

		verify(executorService).submit(job);
		GavinDiagJobExecution jobExecution = captor.getValue();
		assertEquals(jobExecution.getFilename(), "annotate-file");
		assertEquals(jobExecution.getUser(), "tommy");
	}

	@Test
	@Configuration
	@ComponentScan({ "org.molgenis.ui.genenetwork.job.meta", "org.molgenis.data.jobs.model" })
	public static class Config
	{
		@Bean
		IndexPackage indexPackage()
		{
			return mock(IndexPackage.class);
		}

		@Bean
		ExecutorService gavinExecutors()
		{
			return mock(ExecutorService.class);
		}

		@Bean
		GavinDiagJobFactory gavinJobFactory()
		{
			return mock(GavinDiagJobFactory.class);
		}

		@Bean
		UserAccountService userAccountService()
		{
			return mock(UserAccountService.class);
		}

		@Bean
		MolgenisPluginRegistry pluginRegistry()
		{
			return mock(MolgenisPluginRegistry.class);
		}

		@Bean
		PlatformTransactionManager platformTransactionManager()
		{
			return mock(PlatformTransactionManager.class);
		}

		@Bean
		CrudRepositoryAnnotator crudRepositoryAnnotator()
		{
			return mock(CrudRepositoryAnnotator.class);
		}

		@Bean
		DataService dataService()
		{
			return mock(DataService.class);
		}

		@Bean
		PlatformTransactionManager transactionManager()
		{
			return mock(PlatformTransactionManager.class);
		}

		@Bean
		UserDetailsService userDetailsService()
		{
			return mock(UserDetailsService.class);
		}

		@Bean
		JobExecutionUpdater jobExecutionUpdater()
		{
			return mock(JobExecutionUpdater.class);
		}

		@Bean
		MailSender mailSender()
		{
			return mock(MailSender.class);
		}

		@Bean
		FileStore fileStore()
		{
			return mock(FileStore.class);
		}

		@Bean
		RepositoryAnnotator cadd()
		{
			return mock(RepositoryAnnotator.class);
		}

		@Bean
		RepositoryAnnotator exac()
		{
			return mock(RepositoryAnnotator.class);
		}

		@Bean
		RepositoryAnnotator snpEff()
		{
			return mock(RepositoryAnnotator.class);
		}

		@Bean
		EffectBasedAnnotator gavin()
		{
			return mock(EffectBasedAnnotator.class);
		}

		@Bean
		StaticContentService staticContentService()
		{
			return mock(StaticContentService.class);
		}

		@Bean
		MenuReaderService menuReaderService()
		{
			return mock(MenuReaderService.class);
		}
	}
}