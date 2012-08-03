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
package org.openmrs.module.amrscustomization;

import java.util.List;

import org.openmrs.Cohort;
import org.openmrs.Form;
import org.openmrs.User;

import org.openmrs.api.db.DAOException;
import org.openmrs.hl7.HL7InQueue;
import org.openmrs.hl7.HL7Source;

/**
 * DAO for the AMRS Customization module
 */
public interface AMRSCustomizationDAO {

	/**
	 * @see org.openmrs.module.amrscustomization.AMRSCustomizationService#addMRNGeneratorLogEntry(String,
	 *      Integer, Integer)
	 */
	public void addMRNGeneratorLogEntry(MRNGeneratorLogEntry entry) throws DAOException;
	
	/**
	 * @see org.openmrs.module.amrscustomization.AMRSCustomizationService#getMRNGeneratorLogEntries()
	 */
	public List<MRNGeneratorLogEntry> getMRNGeneratorLogEntries() throws DAOException;

    /**
     * @see org.openmrs.module.amrscustomization.AMRSCustomizationService#getNextPrioritizedHL7InQueue() 
     */
    public HL7InQueue getNextPrioritizedHL7InQueue(HL7Source preferredSource) throws DAOException;

    /**
     * @see org.openmrs.module.amrscustomization.AMRSCustomizationService#getNextPrioritizedHL7InQueue()
     */
    public HL7InQueue getNextPrioritizedHL7InQueue() throws DAOException;

    /**
     * @see org.openmrs.module.amrscustomization.AMRSCustomizationService#getPopularRecentFormsForUser(User)
     */
	public List<Form> getPopularRecentFormsForUser(User user);

    public Cohort getPersonIds();
}
