package org.molgenis.ui.genenetwork;

import org.molgenis.data.DataService;
import org.molgenis.data.meta.model.Package;
import org.molgenis.data.meta.model.PackageFactory;
import org.molgenis.ui.MolgenisPluginController;
import org.molgenis.ui.menu.MenuReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.requireNonNull;
import static org.molgenis.security.core.utils.SecurityUtils.getCurrentUsername;
import static org.molgenis.ui.genenetwork.GeneNetworkController.URI;

@Controller
@RequestMapping(URI + "/**")
public class GeneNetworkController extends MolgenisPluginController
{
	public static final String GN_APP = "gene-network";
	public static final String URI = PLUGIN_URI_PREFIX + GN_APP;
	private static final String API_URI = "/api/";

	private final MenuReaderService menuReaderService;
	private final DataService dataService;
	private final PackageFactory packageFactory;

	private static String DIAGNOSTICS_PACKAGE = "diagnostics";

	@Autowired
	public GeneNetworkController(MenuReaderService menuReaderService, DataService dataService,
			PackageFactory packageFactory)
	{
		super(URI);
		this.menuReaderService = requireNonNull(menuReaderService);
		this.dataService = requireNonNull(dataService);
		this.packageFactory = requireNonNull(packageFactory);
	}

	private static String getApiUrl(HttpServletRequest request)
	{
		String apiUrl;
		if (StringUtils.isEmpty(request.getHeader("X-Forwarded-Host")))
		{
			apiUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + API_URI;
		}
		else
		{
			apiUrl = request.getScheme() + "://" + request.getHeader("X-Forwarded-Host") + API_URI;
		}
		return apiUrl;
	}

	private String getBaseUrl()
	{
		return menuReaderService.getMenu().findMenuItemPath(GeneNetworkController.GN_APP);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String init(HttpServletRequest request, Model model)
	{
		model.addAttribute("username", getCurrentUsername());
		model.addAttribute("apiUrl", getApiUrl(request));
		model.addAttribute("baseUrl", getBaseUrl());

		Package diagnosticsPackage = dataService.getMeta().getPackage(DIAGNOSTICS_PACKAGE);
		if (diagnosticsPackage == null)
		{
			diagnosticsPackage = packageFactory
					.create(DIAGNOSTICS_PACKAGE, "Diagnostics package for storing patient VCF data");
			diagnosticsPackage.setLabel(DIAGNOSTICS_PACKAGE);
			dataService.getMeta().addPackage(diagnosticsPackage);
		}
		model.addAttribute("diagnosticsPackageId", diagnosticsPackage.getIdValue());
		return "view-gn-app";
	}
}
