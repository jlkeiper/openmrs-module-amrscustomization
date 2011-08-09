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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.module.ModuleActivator;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class AMRSCustomizationActivator implements ModuleActivator {

        private Log log = LogFactory.getLog(this.getClass());

        /**
         * @see org.openmrs.module.ModuleActivator#startup()
         */
        public void startup() {
                log.info("Starting AMRS Customization");
        }

        /**
         * @see org.openmrs.module.ModuleActivator#shutdown()
         */
        public void shutdown() {
                log.info("Shutting down AMRS Customization");
        }

        /**
         * @see org.openmrs.module.ModuleActivator#willRefreshContext()
         */
        public void willRefreshContext() {
                // TODO Auto-generated method stub
        }

        /**
         * @see org.openmrs.module.ModuleActivator#contextRefreshed()
         */
        public void contextRefreshed() {
                Context.getService(AMRSCustomizationService.class).reloadMaxUploadSize();
        }

        /**
         * @see org.openmrs.module.ModuleActivator#willStart()
         */
        public void willStart() {
                // TODO Auto-generated method stub
        }

        /**
         * @see org.openmrs.module.ModuleActivator#started()
         */
        public void started() {
                // TODO Auto-generated method stub
        }

        /**
         * @see org.openmrs.module.ModuleActivator#willStop()
         */
        public void willStop() {
                // TODO Auto-generated method stub
        }

        /**
         * @see org.openmrs.module.ModuleActivator#stopped()
         */
        public void stopped() {
                // TODO Auto-generated method stub
        }
}
