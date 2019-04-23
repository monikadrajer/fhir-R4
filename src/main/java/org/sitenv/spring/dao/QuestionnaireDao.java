package org.sitenv.spring.dao;


import java.util.List;

import org.hl7.fhir.r4.model.Questionnaire;
import org.sitenv.spring.model.DafQuestionnaire;

public interface QuestionnaireDao {
	
	 public DafQuestionnaire getQuestionnaireById(int id);
	 
	 public List<DafQuestionnaire> getAllQuestionnaire();
	 
	 public DafQuestionnaire getQuestionnaireByVersionId(int theId, String versionId);
	 
	 public DafQuestionnaire createQuestionnaire(Questionnaire theQuestionnaire);

}
