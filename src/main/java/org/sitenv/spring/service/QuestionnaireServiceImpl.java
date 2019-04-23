package org.sitenv.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Questionnaire;
import org.sitenv.spring.dao.QuestionnaireDao;
import org.sitenv.spring.model.DafQuestionnaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service("QuestionnaireService")
public class QuestionnaireServiceImpl implements QuestionnaireService {

	public static final String RESOURCE_TYPE = "Questionnaire";

	@Autowired
	private QuestionnaireDao questionnaireDao;

	@Autowired
	FhirContext fhirContext;

	@Override
	@Transactional
	public Questionnaire getQuestionnaireById(int id) {
		Questionnaire questionnaire= null;
		IParser jsonParser = fhirContext.newJsonParser();
		DafQuestionnaire dafQuestionnaire = questionnaireDao.getQuestionnaireById(id);
		if(dafQuestionnaire != null) {
			questionnaire = jsonParser.parseResource(Questionnaire.class, dafQuestionnaire.getData());
			questionnaire.setId(new IdType(RESOURCE_TYPE, dafQuestionnaire.getId().toString()));
		}
		return questionnaire;
	}

	@Override
	public Questionnaire getQuestionnaireByVersionId(int theId, String versionId) {

		IParser jsonParser = fhirContext.newJsonParser();
		DafQuestionnaire dafQuestionnaire = questionnaireDao.getQuestionnaireByVersionId(theId, versionId);
		Questionnaire questionnaire = jsonParser.parseResource(Questionnaire.class, dafQuestionnaire.getData());
		questionnaire.setId(new IdType(RESOURCE_TYPE, dafQuestionnaire.getId().toString()));
		return questionnaire;
	}

	@Override
	public DafQuestionnaire createQuestionnaire(Questionnaire theQuestionnaire) {
		return questionnaireDao.createQuestionnaire(theQuestionnaire);
	}

	@Override
	public List<Questionnaire> getAllQuestionnaire() {
		Questionnaire questionnaire= null;
		List<Questionnaire> questionnaireList = new ArrayList<Questionnaire>();
		IParser jsonParser = fhirContext.newJsonParser();
		List<DafQuestionnaire> dafQuestionnaireList = questionnaireDao.getAllQuestionnaire();
		if(dafQuestionnaireList != null && dafQuestionnaireList.size() > 0) {
			for(DafQuestionnaire dafQuestionnaire : dafQuestionnaireList) {
				questionnaire = jsonParser.parseResource(Questionnaire.class, dafQuestionnaire.getData());
				questionnaire.setId(new IdType(RESOURCE_TYPE, dafQuestionnaire.getId().toString()));
				questionnaireList.add(questionnaire);
			}
		}
		return questionnaireList;
	}

}
