package org.sitenv.spring;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.sitenv.spring.configuration.AppConfig;
import org.sitenv.spring.model.DafQuestionnaireResponse;
import org.sitenv.spring.service.QuestionnaireResponseService;
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

public class QuestionnaireResponseResourceProvider  implements IResourceProvider {
	
	public static final String RESOURCE_TYPE = "QuestionnaireResponse";
    public static final String VERSION_ID = "4.0";
    AbstractApplicationContext context;
    QuestionnaireResponseService service;

    public QuestionnaireResponseResourceProvider() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = (QuestionnaireResponseService) context.getBean("QuestionnaireResponseService");
    }

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return QuestionnaireResponse.class;
	}
	
	
	
	@Create
	public MethodOutcome createPatient(@ResourceParam QuestionnaireResponse theQuestionnaireResponse) {
	 
	  // Save this Questionnaire to the database...
	  DafQuestionnaireResponse dafQuestionnaireResponse = service.createQuestionnaireResponse(theQuestionnaireResponse);
	 
	  // This method returns a MethodOutcome object which contains
	  // the ID (composed of the type Patient, the logical ID 3746, and the
	  // version ID 1)
	  MethodOutcome retVal = new MethodOutcome();
	  retVal.setId(new IdType(RESOURCE_TYPE, dafQuestionnaireResponse.getId().toString()));

	  return retVal;
	}
	
	
	@Search
	public List<QuestionnaireResponse> searchAllQuestionnaireResponse() {

		return service.getAllQuestionnaireResponse();

	}

	
 	/**
	 * The "@Read" annotation indicates that this method supports the read operation. 
	 * The vread operation retrieves a specific version of a resource with a given ID. To support vread, simply add "version=true" to your @Read annotation. 
	 * This means that the read method will support both "Read" and "VRead". 
	 * The IdDt may or may not have the version populated depending on the client request.
	 * This operation retrieves a resource by ID. It has a single parameter annotated with the @IdParam annotation.
	 * Example URL to invoke this method: http://<server name>/<context>/fhir/Questionnaire/1/_history/4
	 * @param theId : Id of the AllergyIntolerance
	 * @return : Object of AllergyIntolerance information
	 */
	@Read(version=true)
    public QuestionnaireResponse readOrVread(@IdParam IdType theId) {
		int id;
		QuestionnaireResponse questionnaireResponse;
		try {
		    id = theId.getIdPartAsLong().intValue();
		} catch (NumberFormatException e) {
		    /*
		     * If we can't parse the ID as a long, it's not valid so this is an unknown resource
			 */
		    throw new ResourceNotFoundException(theId);
		}
		if (theId.hasVersionIdPart()) {
		   // this is a vread  
			questionnaireResponse = service.getQuestionnaireResponseByVersionId(id, theId.getVersionIdPart());
		   
		} else {
		   // this is a read
			questionnaireResponse = service.getQuestionnaireResponseById(id);
		}
		
		if(questionnaireResponse==null) {
			throw new ResourceNotFoundException(theId);
		}
		return questionnaireResponse;
    }

}
