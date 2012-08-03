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

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.openmrs.Cohort;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.hl7.HL7Constants;
import org.openmrs.hl7.HL7InQueue;
import org.openmrs.hl7.HL7Source;
import org.openmrs.module.amrscustomization.AMRSCustomizationConstants;
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

   	/**
	 * @see 
	 */
	public HL7InQueue getNextPrioritizedHL7InQueue(HL7Source preferredSource) throws DAOException {
        // look for items in prioritized source first
        if (preferredSource != null) {
            Criteria crit = sessionFactory.getCurrentSession().createCriteria(HL7InQueue.class)
                    .add(Restrictions.and(
                        Restrictions.eq("messageState", HL7Constants.HL7_STATUS_PENDING),
                        Restrictions.eq("HL7Source", preferredSource)))
                    .addOrder(Order.asc("HL7InQueueId"))
                    .setMaxResults(1);

            HL7InQueue result = (HL7InQueue) crit.uniqueResult();
            if (result != null)
                return result;
        }

		Query query = sessionFactory.getCurrentSession().createQuery(
		    "from HL7InQueue as hiq where hiq.messageState = ? order by HL7InQueueId")
                .setParameter(0, HL7Constants.HL7_STATUS_PENDING, Hibernate.INTEGER)
                .setMaxResults(1);
		if (query == null)
			return null;
		return (HL7InQueue) query.uniqueResult();
	}

   	/**
	 * @see
	 */
	public HL7InQueue getNextPrioritizedHL7InQueue() throws DAOException {
        // look for items in prioritized source first
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(HL7InQueue.class)
                .add(Restrictions.and(
                    Restrictions.eq("messageState", HL7Constants.HL7_STATUS_PENDING),
                    Restrictions.or(
                        Restrictions.like("HL7Data", "97^AMRS.ELD.FORMID", MatchMode.ANYWHERE),
                        Restrictions.like("HL7Data", "125^AMRS.ELD.FORMID", MatchMode.ANYWHERE))))
                .addOrder(Order.asc("HL7InQueueId"))
                .setMaxResults(1);

        HL7InQueue result = (HL7InQueue) crit.uniqueResult();

        if (result != null) {
            log.warn("Processing prioritized ANC HL7 before others (" + result.getHL7SourceKey() + ")");
            return result;
        }

		Query query = sessionFactory.getCurrentSession().createQuery(
		    "from HL7InQueue as hiq where hiq.messageState = ? order by HL7InQueueId")
                .setParameter(0, HL7Constants.HL7_STATUS_PENDING, Hibernate.INTEGER)
                .setMaxResults(1);
		if (query == null)
			return null;
		return (HL7InQueue) query.uniqueResult();
	}

	public List<Form> getPopularRecentFormsForUser(User user) {
		String monthsStr = Context.getAdministrationService().getGlobalProperty(AMRSCustomizationConstants.GP_RECENT_FORMS_INTERVAL);
		Integer months = AMRSCustomizationConstants.DEFAULT_RECENT_FORMS_INTERVAL;
		try {
			months = Integer.parseInt(monthsStr);
		} catch (NumberFormatException ex) {
			log.warn("could not interpret " + monthsStr + " interval as an integer.");
		}
		
		Calendar monthsAgo = Calendar.getInstance();
		monthsAgo.add(Calendar.MONTH, -1 * months);

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.groupProperty("form"));
        projectionList.add(Projections.alias(Projections.rowCount(), "total"));
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Encounter.class)
				.add(Restrictions.and(
					Restrictions.eq("creator", user), 
					Restrictions.gt("dateCreated", monthsAgo.getTime())))
				.setProjection(projectionList)
				.addOrder(Order.desc("total"))
				.setMaxResults(5);

		List<Form> forms = new ArrayList<Form>();
		List<Object[]> foo = crit.list();
		for(Object[] result: foo) {
			log.warn(result[0] + ": " + result[1]);
			forms.add((Form)result[0]);
		}
		
		return forms;
	}

    @SuppressWarnings("unchecked")
    public Cohort getPersonIds(){
        Set<Integer> patientids = new HashSet<Integer>();
        try{
            Query queryPersonIds = sessionFactory.getCurrentSession().createQuery
                    (
                            " SELECT p.personId "
                                    +" FROM Person p, PersonAddress pa"
                                    +" WHERE "
                                    +"p.personId=pa.person "
                                    +"and "
                                    + "(pa.latitude LIKE 'N%' or pa.latitude LIKE 'S%' ) "
                                    +"and "
                                    +"pa.latitude is not null "
                                    +"and "
                                    +"pa.longitude  is not null  "
                                    +"and "
                                    +"pa.latitude != '' "
                                    +"and "
                                    +"pa.longitude  != ''  "
                                    +"and "
                                    +" p.voided =0"
                    );



            patientids.addAll(queryPersonIds.list());
        }
        catch (HibernateException hibernateException) {

            hibernateException.printStackTrace();
        }

        return new Cohort("All person ids", "", patientids);
    }


}
