/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.amrscustomization.extension.html;

import java.util.HashMap;
import java.util.Map;
import org.openmrs.module.Extension;
import org.openmrs.module.Extension.MEDIA_TYPE;

/**
 *
 * @author jkeiper
 */
public class ConceptHeaderLinks extends Extension {

    @Override
    public MEDIA_TYPE getMediaType() {
        return Extension.MEDIA_TYPE.html;
    }

    /**
	 * @see org.openmrs.module.web.extension.AdministrationSectionExt#getLinks()
	 */
	public Map<String, String> getLinks() {

		Map<String, String> map = new HashMap<String, String>();
		map.put("module/amrscustomization/conceptNameEditor.htm", "amrscustomization.ConceptName.link");
		return map;
	}

}
