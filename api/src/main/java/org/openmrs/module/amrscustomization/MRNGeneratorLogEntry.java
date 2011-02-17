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

import java.util.Date;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.User;

/**
 * MRN Generator Log Entry
 */
public class MRNGeneratorLogEntry extends BaseOpenmrsObject {
	
	private Integer entryId;
	
	private Date dateGenerated;
	
	private User generatedBy;
	
	private String site;
	
	private Integer first;
	
	private Integer count;
	
	/**
	 * @return the entryId
	 */
	public Integer getEntryId() {
		return entryId;
	}
	
	/**
	 * @param entryId the entryId to set
	 */
	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}
	
	/**
	 * @return the dateGenerated
	 */
	public Date getDateGenerated() {
		return dateGenerated;
	}
	
	/**
	 * @param dateGenerated the dateGenerated to set
	 */
	public void setDateGenerated(Date dateGenerated) {
		this.dateGenerated = dateGenerated;
	}
	
	/**
	 * @return the generatedBy
	 */
	public User getGeneratedBy() {
		return generatedBy;
	}
	
	/**
	 * @param generatedBy the generatedBy to set
	 */
	public void setGeneratedBy(User generatedBy) {
		this.generatedBy = generatedBy;
	}
	
	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}
	
	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}
	
	/**
	 * @return the first
	 */
	public Integer getFirst() {
		return first;
	}
	
	/**
	 * @param first the first to set
	 */
	public void setFirst(Integer first) {
		this.first = first;
	}
	
	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}
	
	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	public Integer getId() {
		return getEntryId();
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		setEntryId(id);
	}
	
}
