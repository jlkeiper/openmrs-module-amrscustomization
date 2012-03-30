/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.amrscustomization;

/**
 *
 * @author jkeiper
 */
public class AMRSCustomizationConstants {
	public static final String GP_MAX_UPLOAD_SIZE = "amrscustomization.maxUploadSize";
	public static final String GP_PREFERRED_HL7_SOURCE = "amrscustomization.preferredHL7Source";
	public static final String GP_RECENT_FORMS_INTERVAL = "amrscustomization.recentFormsInterval";

	public static final Integer DEFAULT_MAX_UPLOAD_SIZE = 75000000;
	public static final Integer DEFAULT_RECENT_FORMS_INTERVAL = 1;
	
	public static final String VELOCITY_TEMPLATE_FOLDER = "/org/openmrs/module/amrscustomization/templates/";
}
