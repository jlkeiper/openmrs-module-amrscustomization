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

import org.openmrs.annotation.Authorized;
import org.openmrs.hl7.HL7InQueue;
import org.openmrs.hl7.HL7Source;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * AMRS Customization service
 */
@Transactional
public interface AMRSCustomizationService {

    /**
     * creates a mrn generator log entry
     *
     * @param site
     * @param start
     * @param count
     */
    @Authorized({PrivilegeConstants.EDIT_PATIENTS})
    public void addMRNGeneratorLogEntry(String site, Integer start, Integer count);

    /**
     * retrieves the mrn generator log
     */
    @Transactional(readOnly = true)
    @Authorized({PrivilegeConstants.EDIT_PATIENTS})
    public List<MRNGeneratorLogEntry> getMRNGeneratorLogEntries();

    public void setMaxUploadSize(Integer maxUploadSize);

    public void reloadMaxUploadSize();

    /**
     * retrieves the next HL7 in the queue, prioritized by the preferred source
     * as specified in a global property by name.
     *
     * @should return a preferred HL7 when it is added later than a non-preferred one
     * @should return the first HL7 in the queue if no source is specified
     * @should return the first HL7 in the queue if no HL7 matches preferred source
     */
    public HL7InQueue getNextPrioritizedHL7InQueue(HL7Source source);

    public HL7InQueue getNextPrioritizedHL7InQueue();
}
