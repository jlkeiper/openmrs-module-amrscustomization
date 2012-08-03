package org.openmrs.module.amrscustomization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Nicholas Ingosi Magaja
 * Date: 8/3/12
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomPersonAddressProcessor {
    private static final Log log = LogFactory.getLog(CustomPersonAddressProcessor.class);

    private CustomPersonAddress customPersonAddress = null;

    public void processCustomPersonAddress() {

        log.debug("Processing custom person address");

        if (customPersonAddress == null)
            customPersonAddress = new CustomPersonAddress();
        customPersonAddress.processAddresses();

    }
}
