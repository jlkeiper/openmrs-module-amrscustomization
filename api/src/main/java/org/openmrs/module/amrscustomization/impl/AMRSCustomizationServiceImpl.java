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
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.hl7.HL7InQueue;
import org.openmrs.hl7.HL7Source;
import org.openmrs.module.amrscustomization.AMRSCustomizationConstants;
import org.openmrs.module.amrscustomization.AMRSCustomizationDAO;
import org.openmrs.module.amrscustomization.AMRSCustomizationService;
import org.openmrs.module.amrscustomization.MRNGeneratorLogEntry;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

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

    private void saveMaxUploadSize(Integer maxUploadSize) {
        GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(AMRSCustomizationConstants.GP_MAX_UPLOAD_SIZE);
        gp.setPropertyValue(Integer.toString(maxUploadSize));
        Context.getAdministrationService().saveGlobalProperty(gp);
    }

    /**
     *  set the maximum upload size, for Remote Form Entry and Clinical Summary Modules
     */
    public void setMaxUploadSize(Integer maxUploadSize) {
        List<CommonsMultipartResolver> resolvers =
                ServiceContext.getInstance().getRegisteredComponents(CommonsMultipartResolver.class);
        for (CommonsMultipartResolver resolver : resolvers) {
            resolver.setMaxUploadSize(maxUploadSize);
        }
        this.saveMaxUploadSize(maxUploadSize);
    }

    public void reloadMaxUploadSize() {
        String maxSizeString = Context.getAdministrationService().getGlobalProperty(AMRSCustomizationConstants.GP_MAX_UPLOAD_SIZE);
        Integer maxSize;
        try {
            maxSize = Integer.parseInt(maxSizeString);
        } catch (NumberFormatException e) {
            log.error("could not interpret \""
                    + maxSizeString
                    + "\" as an integer for "
                    + AMRSCustomizationConstants.GP_MAX_UPLOAD_SIZE);
            maxSize = AMRSCustomizationConstants.DEFAULT_MAX_UPLOAD_SIZE;
        }
        this.setMaxUploadSize(maxSize);
    }

    /**
     * @see AMRSCustomizationService#getNextPrioritizedHL7(HL7Source)
     */
    public HL7InQueue getNextPrioritizedHL7InQueue(HL7Source source) {
        return dao.getNextPrioritizedHL7InQueue(source);
    }

    /**
     * @see AMRSCustomizationService#getNextPrioritizedHL7(HL7Source)
     */
    public HL7InQueue getNextPrioritizedHL7InQueue() {
        return dao.getNextPrioritizedHL7InQueue();
    }

    /**
     * @see AMRSCustomizationService#getPopularRecentFormsForUser(User)
     */
	public List<Form> getPopularRecentFormsForUser(User user) {
		return dao.getPopularRecentFormsForUser(user);
	}

	/**
	 * @see AMRSCustomzationService#mapConceptProposalToConcept(org.openmrs.ConceptProposal,
	 *      org.openmrs.Concept, java.util.locale)
	 */
	public Concept mapConceptProposalToConcept(ConceptProposal cp, Concept mappedConcept, Locale locale) throws APIException {
		ConceptService cs = Context.getConceptService();
		
		if (cp.getState().equals(OpenmrsConstants.CONCEPT_PROPOSAL_REJECT)) {
			cp.rejectConceptProposal();
			cs.saveConceptProposal(cp);
			return null;
		}
		
		if (mappedConcept == null)
			throw new APIException("Illegal Mapped Concept");

		ConceptName conceptName = null;
		if (cp.getState().equals(OpenmrsConstants.CONCEPT_PROPOSAL_CONCEPT) || !StringUtils.hasText(cp.getFinalText())) {
			cp.setState(OpenmrsConstants.CONCEPT_PROPOSAL_CONCEPT);
			cp.setFinalText("");
		} else if (cp.getState().equals(OpenmrsConstants.CONCEPT_PROPOSAL_SYNONYM)) {
			
			cs.checkIfLocked();
			
			String finalText = cp.getFinalText();
			conceptName = new ConceptName(finalText, null);
			//If this is pre 1.9
			if(conceptName.getUuid() == null)
				conceptName.setUuid(UUID.randomUUID().toString());
			conceptName.setConcept(mappedConcept);
			conceptName.setLocale(locale == null ? Context.getLocale() : locale);
			conceptName.setDateCreated(new Date());
			conceptName.setCreator(Context.getAuthenticatedUser());
			mappedConcept.addName(conceptName);
			mappedConcept.setChangedBy(Context.getAuthenticatedUser());
			mappedConcept.setDateChanged(new Date());
			cs.updateConceptWord(mappedConcept);
		}
		
		cp.setMappedConcept(mappedConcept);
		
		if (cp.getObsConcept() != null) {
			Obs ob = new Obs();
			//If this is pre 1.9
			if(ob.getUuid() == null)
				ob.setUuid(UUID.randomUUID().toString());
			ob.setEncounter(cp.getEncounter());
			ob.setConcept(cp.getObsConcept());
			ob.setValueCoded(cp.getMappedConcept());
			if (cp.getState().equals(OpenmrsConstants.CONCEPT_PROPOSAL_SYNONYM))
				ob.setValueCodedName(conceptName);
			ob.setCreator(Context.getAuthenticatedUser());
			ob.setDateCreated(new Date());
			ob.setObsDatetime(cp.getEncounter().getEncounterDatetime());
			ob.setLocation(cp.getEncounter().getLocation());
			ob.setPerson(cp.getEncounter().getPatient());
			cp.setObs(ob);
		}
		
		return mappedConcept;
	}
    public Cohort getPersonIds() {

        return dao.getPersonIds();
    }
}
