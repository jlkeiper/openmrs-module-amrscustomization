/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.amrscustomization;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.ConceptProposal;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.hl7.HL7Constants;
import org.openmrs.hl7.HL7InQueue;
import org.openmrs.hl7.HL7Source;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.OpenmrsConstants;

import java.util.List;

/**
 *
 * @author jkeiper
 */
public class AMRSCustomizationServiceTest extends BaseModuleContextSensitiveTest {

	protected ConceptService conceptService = null;
	protected AMRSCustomizationService amrsCustomizationService = null;

	/**
	 * Run this before each unit test in this class. The "@Before" method in
	 * {@link BaseContextSensitiveTest} is run right before this method.
	 *
	 * @throws Exception
	 */
	@Before()
	public void runBeforeAllTests() throws Exception {
		conceptService = Context.getConceptService();
		amrsCustomizationService = Context.getService(AMRSCustomizationService.class);
	}

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

	/**
	 * @verifies not set value coded name when add concept is selected
	 * @see AMRSCustomizationService#mapConceptProposalToConcept(org.openmrs.ConceptProposal, org.openmrs.Concept, java.util.Locale)
	 */
	@Test
	public void mapConceptProposalToConcept_shouldNotSetValueCodedNameWhenAddConceptIsSelected() throws Exception {
		ConceptProposal cp = conceptService.getConceptProposal(2);
		Assert.assertEquals(OpenmrsConstants.CONCEPT_PROPOSAL_UNMAPPED, cp.getState());
		final Concept civilStatusConcept = conceptService.getConcept(4);
		final int mappedConceptId = 6;
		Assert.assertTrue(Context.getObsService().getObservationsByPersonAndConcept(cp.getEncounter().getPatient(),
				civilStatusConcept).isEmpty());
		Concept mappedConcept = conceptService.getConcept(mappedConceptId);

		cp.setObsConcept(civilStatusConcept);
		cp.setState(OpenmrsConstants.CONCEPT_PROPOSAL_CONCEPT);
		amrsCustomizationService.mapConceptProposalToConcept(cp, mappedConcept, null);
		mappedConcept = conceptService.getConcept(mappedConceptId);
		List<Obs> observations = Context.getObsService().getObservationsByPersonAndConcept(cp.getEncounter().getPatient(),
				civilStatusConcept);
		Assert.assertEquals(1, observations.size());
		Obs obs = observations.get(0);
		Assert.assertNull(obs.getValueCodedName());
	}

	/**
	 * @verifies set value coded name when add synonym is selected
	 * @see AMRSCustomizationService#mapConceptProposalToConcept(org.openmrs.ConceptProposal, org.openmrs.Concept, java.util.Locale)
	 */
	@Test
	public void mapConceptProposalToConcept_shouldSetValueCodedNameWhenAddSynonymIsSelected() throws Exception {
		ConceptProposal cp = conceptService.getConceptProposal(2);
		Assert.assertEquals(OpenmrsConstants.CONCEPT_PROPOSAL_UNMAPPED, cp.getState());
		final Concept civilStatusConcept = conceptService.getConcept(4);
		final int mappedConceptId = 6;
		final String finalText = "Weight synonym";
		Assert.assertTrue(Context.getObsService().getObservationsByPersonAndConcept(cp.getEncounter().getPatient(),
				civilStatusConcept).isEmpty());
		Concept mappedConcept = conceptService.getConcept(mappedConceptId);

		cp.setFinalText(finalText);
		cp.setObsConcept(civilStatusConcept);
		cp.setState(OpenmrsConstants.CONCEPT_PROPOSAL_SYNONYM);
		amrsCustomizationService.mapConceptProposalToConcept(cp, mappedConcept, null);
		mappedConcept = conceptService.getConcept(mappedConceptId);
		List<Obs> observations = Context.getObsService().getObservationsByPersonAndConcept(cp.getEncounter().getPatient(),
				civilStatusConcept);
		Assert.assertEquals(1, observations.size());
		Obs obs = observations.get(0);
		Assert.assertNotNull(obs.getValueCodedName());
		Assert.assertEquals(finalText, obs.getValueCodedName().getName());
	}
}
