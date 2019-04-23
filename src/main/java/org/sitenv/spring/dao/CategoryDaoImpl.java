package org.sitenv.spring.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.sitenv.spring.model.DafCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("CategoryDao")
public class CategoryDaoImpl implements CategoryDao {
	
	@Autowired
    private SessionFactory sessionFactory;

	@Override
	public List<DafCategory> getAllCategories() {
		Session session = sessionFactory.openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();

	    // UPDATED: Create CriteriaQuery
	    CriteriaQuery<DafCategory> criteria = builder.createQuery(DafCategory.class);

	    // UPDATED: Specify criteria root
	    criteria.from(DafCategory.class);

	    // UPDATED: Execute query
	    List<DafCategory> categoryList = session.createQuery(criteria).getResultList();
	    
	    session.close();
	    
	    return categoryList;
	}

}
