/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.amrscustomization.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.amrscustomization.AMRSCustomizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jkeiper
 */
@Controller
@RequestMapping("module/amrscustomization/settings.htm")
public class SettingsController {

	@RequestMapping(method = RequestMethod.GET)
	public String getSettingsPage(ModelMap modelMap) {
		return "module/amrscustomization/settings";
	}

        @RequestMapping(method = RequestMethod.POST)
        public void updateSetting(@RequestParam Integer maxUploadSize) {
                Context.getService(AMRSCustomizationService.class).setMaxUploadSize(maxUploadSize);
        }
        
}
