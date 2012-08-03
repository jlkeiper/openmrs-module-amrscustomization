package org.openmrs.module.amrscustomization.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrscustomization.CustomPersonAddressProcessor;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Created with IntelliJ IDEA.
 * User: Nicholas Ingosi Magaja
 * Date: 8/3/12
 * Time: 9:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomPersonAddressTask extends AbstractTask {

    // Logger
    private static Log log = LogFactory.getLog(CustomPersonAddressTask.class);



    private static CustomPersonAddressProcessor processor = null;

    /**
     * Default Constructor (Uses SchedulerConstants.username and SchedulerConstants.password
     */
    public CustomPersonAddressTask() {
        if (processor == null) {
            processor = new CustomPersonAddressProcessor();
        }
    }
    /**
     * Process the next form entry in the database and then remove the form entry from the database.
     */
    @Override
    public void execute() {
        Context.openSession();
        try {
            log.debug("Processing custom person address task ... ");
            if (!Context.isAuthenticated()) {
                authenticate();
            }
            processor.processCustomPersonAddress();
        }
        catch (Exception e) {
            log.error("Error running custom person address task", e);
            throw new APIException("Error running custom person address task", e);
        }
        finally {
            Context.closeSession();
        }
    }
    /*
          * Resources clean up
          */
    public void shutdown() {
        processor = null;
        super.shutdown();
        log.debug("Shutting down CustomPersonAddressTask task ...");
    }


}
