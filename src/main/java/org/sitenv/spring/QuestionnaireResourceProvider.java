package org.sitenv.spring;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Questionnaire;
import org.sitenv.spring.configuration.AppConfig;
import org.sitenv.spring.model.DafQuestionnaire;
import org.sitenv.spring.service.QuestionnaireService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class QuestionnaireResourceProvider implements IResourceProvider {

	public static final String RESOURCE_TYPE = "Questionnaire";
	public static final String VERSION_ID = "4.0";
	AbstractApplicationContext context;
	QuestionnaireService service;

	public QuestionnaireResourceProvider() {
		context = new AnnotationConfigApplicationContext(AppConfig.class);
		service = (QuestionnaireService) context.getBean("QuestionnaireService");
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Questionnaire.class;
	}

	@Create
	public MethodOutcome createQuestionnaire(@ResourceParam Questionnaire theQuestionnaire) {

		// Save this Questionnaire to the database...
		DafQuestionnaire dafQuestionnaire = service.createQuestionnaire(theQuestionnaire);

		// This method returns a MethodOutcome object which contains
		// the ID (composed of the type Patient, the logical ID 3746, and the
		// version ID 1)
		MethodOutcome retVal = new MethodOutcome();
		retVal.setId(new IdType(RESOURCE_TYPE, dafQuestionnaire.getId().toString()));

		return retVal;
	}

	@Search
	public List<Questionnaire> searchAllQuestionnaire() {

		return service.getAllQuestionnaire();

	}

	/**
	 * The "@Read" annotation indicates that this method supports the read
	 * operation. The vread operation retrieves a specific version of a resource
	 * with a given ID. To support vread, simply add "version=true" to your @Read
	 * annotation. This means that the read method will support both "Read" and
	 * "VRead". The IdDt may or may not have the version populated depending on the
	 * client request. This operation retrieves a resource by ID. It has a single
	 * parameter annotated with the @IdParam annotation. Example URL to invoke this
	 * method: http://<server name>/<context>/fhir/Questionnaire/1/_history/4
	 * 
	 * @param theId : Id of the AllergyIntolerance
	 * @return : Object of AllergyIntolerance information
	 */
	@Read(version = true)
	public Questionnaire readOrVread(@IdParam IdType theId) {
		int id;
		Questionnaire questionnaire;
		try {
			id = theId.getIdPartAsLong().intValue();
		} catch (NumberFormatException e) {
			/*
			 * If we can't parse the ID as a long, it's not valid so this is an unknown
			 * resource
			 */
			throw new ResourceNotFoundException(theId);
		}
		if (theId.hasVersionIdPart()) {
			// this is a vread
			questionnaire = service.getQuestionnaireByVersionId(id, theId.getVersionIdPart());

		} else {
			// this is a read
			questionnaire = service.getQuestionnaireById(id);
		}

		if (questionnaire == null) {
			throw new ResourceNotFoundException(theId);
		}
		return questionnaire;
	}
}
