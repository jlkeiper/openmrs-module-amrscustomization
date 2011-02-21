package org.openmrs.module.amrscustomization.extension.html;

import java.util.Collections;
import java.util.List;

import org.openmrs.module.web.extension.HeaderIncludeExt;

public class FullHeaderInclude extends HeaderIncludeExt {

	@Override
	public List<String> getHeaderFiles() {
		return Collections.singletonList("/moduleResources/amrscustomization/customStyle.css");
	}

}
