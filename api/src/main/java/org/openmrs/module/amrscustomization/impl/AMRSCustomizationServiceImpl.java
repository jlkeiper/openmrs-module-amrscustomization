/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.amrscustomization.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrscustomization.AMRSCustomizationDAO;
import org.openmrs.module.amrscustomization.AMRSCustomizationService;
import org.openmrs.module.amrscustomization.MRNGeneratorLogEntry;

/**
 * implementation of the AMRS Customization service
 */
public class AMRSCustomizationServiceImpl implements AMRSCustomizationService {
	
	protected Log log = LogFactory.getLog(getClass());
	
	protected AMRSCustomizationDAO dao;
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(AMRSCustomizationDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @see org.openmrs.module.amrscustomization.AMRSCustomizationService#addMRNGeneratorLogEntry(java.lang.String,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	public void addMRNGeneratorLogEntry(String site, Integer start, Integer count) {
		MRNGeneratorLogEntry entry = new MRNGeneratorLogEntry();
		entry.setDateGenerated(new Date());
		entry.setGeneratedBy(Context.getAuthenticatedUser());
		entry.setSite(site);
		entry.setFirst(start);
		entry.setCount(count);
		entry.setUuid(UUID.randomUUID().toString());
		
		dao.addMRNGeneratorLogEntry(entry);
	}
	
	/**
	 * @see org.openmrs.module.amrscustomization.AMRSCustomizationService#getMRNGeneratorLogEntries()
	 */
	public List<MRNGeneratorLogEntry> getMRNGeneratorLogEntries() throws APIException {
		return dao.getMRNGeneratorLogEntries();
	}
	
}
