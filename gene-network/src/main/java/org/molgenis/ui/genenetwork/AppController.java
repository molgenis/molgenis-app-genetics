package org.molgenis.ui.genenetwork;

import org.molgenis.ui.MolgenisPluginController;
import org.molgenis.ui.menu.MenuReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static org.molgenis.ui.genenetwork.AppController.URI;
import static org.molgenis.security.core.utils.SecurityUtils.getCurrentUsername;

@Controller
@RequestMapping(URI + "/**")
public class AppController extends MolgenisPluginController
{
	public static final String GN_APP = "gene-network";
	public static final String URI = PLUGIN_URI_PREFIX + GN_APP;
	private static final String API_URI = "/api/";

	private final MenuReaderService menuReaderService;

	@Autowired
	public AppController(MenuReaderService menuReaderService)
	{
		super(URI);
		System.out.println("$$$$$$$$$$$$$$$ ADDING PLUGIN APP GNEETIC ####");
		this.menuReaderService = menuReaderService;
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
		return menuReaderService.getMenu().findMenuItemPath(AppController.GN_APP);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String init(HttpServletRequest request, Model model)
	{
		model.addAttribute("username", getCurrentUsername());
		model.addAttribute("apiUrl", getApiUrl(request));
		model.addAttribute("baseUrl", getBaseUrl());
		return "view-gn-app";
	}
}
