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
package org.openmrs.module.amrscustomization.db;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.amrscustomization.AMRSCustomizationDAO;
import org.openmrs.module.amrscustomization.MRNGeneratorLogEntry;

/**
 * Hibernate implementation of the AMRS Customization DAO
 */
public class HibernateAMRSCustomizationDAO implements AMRSCustomizationDAO {
	
	protected Log log = LogFactory.getLog(getClass());
	
	private SessionFactory sessionFactory;
	
	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.amrscustomization.AMRSCustomizationDAO#saveMRNGeneratorLogEntry(java.lang.String,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	public void addMRNGeneratorLogEntry(MRNGeneratorLogEntry entry) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(entry);
	}
	
	/**
	 * @see org.openmrs.module.amrscustomization.AMRSCustomizationDAO#getMRNGeneratorLogEntries()
	 */
	@SuppressWarnings("unchecked")
    public List<MRNGeneratorLogEntry> getMRNGeneratorLogEntries() throws DAOException {
		return sessionFactory.getCurrentSession().createCriteria(MRNGeneratorLogEntry.class)
		        .addOrder(Order.desc("dateGenerated")).list();
	}
	
}
