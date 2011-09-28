/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.amrscustomization;

import junit.framework.Assert;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.hl7.HL7Constants;
import org.openmrs.hl7.HL7InQueue;
import org.openmrs.hl7.HL7Source;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 *
 * @author jkeiper
 */
public class AMRSCustomizationServiceTest extends BaseModuleContextSensitiveTest {

    @Test
    public void getNextPrioritizedHL7InQueue_shouldReturnAPreferredHL7WhenItIsAddedLaterThanANonPreferredOne() {
        HL7Source ackSource = saveNewHL7Source("ack");
        HL7Source fooSource = saveNewHL7Source("foo");
        saveNewHL7(ackSource, "ACK001");
        saveNewHL7(ackSource, "ACK002");
        saveNewHL7(fooSource, "FOO001");
        HL7InQueue actual = Context.getService(AMRSCustomizationService.class).getNextPrioritizedHL7InQueue(fooSource);
        Assert.assertEquals("FOO001", actual.getHL7SourceKey());
    }

    @Test
    public void getNextPrioritizedHL7InQueue_shouldReturnTheFirstHL7InTheQueueIfNoSourceIsSpecified() {
        HL7Source ackSource = saveNewHL7Source("ack");
        HL7Source fooSource = saveNewHL7Source("foo");
        saveNewHL7(ackSource, "ACK001");
        saveNewHL7(ackSource, "ACK002");
        saveNewHL7(fooSource, "FOO001");
        HL7InQueue actual = Context.getService(AMRSCustomizationService.class).getNextPrioritizedHL7InQueue(null);
        Assert.assertEquals("ACK001", actual.getHL7SourceKey());
    }

    @Test
    public void getNextPrioritizedHL7InQueue_shouldReturnTheFirstHL7InTheQueueIfNoHL7MatchesPreferredSource() {
        HL7Source ackSource = saveNewHL7Source("ack");
        HL7Source fooSource = saveNewHL7Source("foo");
        saveNewHL7(ackSource, "ACK001");
        saveNewHL7(ackSource, "ACK002");
        HL7InQueue actual = Context.getService(AMRSCustomizationService.class).getNextPrioritizedHL7InQueue(fooSource);
        Assert.assertEquals("ACK001", actual.getHL7SourceKey());
    }

    @Test
    public void getNextPrioritizedHL7InQueue_shouldReturnTheFirstANCHL7Message() {
        HL7Source ackSource = saveNewHL7Source("ack");
        HL7Source fooSource = saveNewHL7Source("foo");
        saveNewHL7(ackSource, "ACK001", "blahblahblah|55^AMRS.ELD.FORMID|FOOOOOOOOO");
        saveNewHL7(ackSource, "ACK002", "blahblahblah|124^AMRS.ELD.FORMID|FOOOOOOOOO");
        saveNewHL7(ackSource, "FOO001", "blahblahblah|97^AMRS.ELD.FORMID|FOOOOOOOOO");
        HL7InQueue actual = Context.getService(AMRSCustomizationService.class).getNextPrioritizedHL7InQueue();
        Assert.assertEquals("FOO001", actual.getHL7SourceKey());
    }

    private HL7Source saveNewHL7Source(String name) {
        HL7Source source = new HL7Source();
        source.setName(name);
        source.setDescription(name);
        return Context.getHL7Service().saveHL7Source(source);
    }

    private void saveNewHL7(HL7Source source, String sourceKey) {
        saveNewHL7(source, sourceKey, "");
    }

    private void saveNewHL7(HL7Source source, String sourceKey, String data) {
        HL7InQueue q = new HL7InQueue();
        q.setHL7Source(source);
        q.setHL7SourceKey(sourceKey);
        q.setHL7Data(data);
        q.setMessageState(HL7Constants.HL7_STATUS_PENDING);
        Context.getHL7Service().saveHL7InQueue(q);
    }
}
