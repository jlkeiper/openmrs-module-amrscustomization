/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.amrscustomization.extension.html;

import java.io.File;
import java.io.IOException;
import org.openmrs.module.Extension;
import org.openmrs.util.OpenmrsUtil;

/**
 *
 * @author jkeiper
 */
public class ConceptFormFooterExtras extends Extension {

	private static final String filePath = "/org/openmrs/module/amrscustomization/conceptFormExtension.jsp";
	
	@Override
	public MEDIA_TYPE getMediaType() {
		return MEDIA_TYPE.html;
	}

	@Override
	public String getOverrideContent(String bodyContent) {
		File conceptFormExtras = OpenmrsUtil.url2file(this.getClass().getResource(filePath));
		try {
			return OpenmrsUtil.getFileAsString(conceptFormExtras);
		} catch (IOException ex) {
			return "oops";
		}
	}
}
