package org.openmrs.module.amrscustomization.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("index.htm")
public class CustomIndexController {

	/**
	 * points requests to the custom index page
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getCustomIndex(ModelMap modelMap) {
		return "module/amrscustomization/customIndex";
	}

}
