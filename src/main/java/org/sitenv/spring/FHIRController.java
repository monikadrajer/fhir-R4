package org.sitenv.spring;

import java.util.List;

import org.sitenv.spring.model.Category;
import org.sitenv.spring.model.Categories;
import org.sitenv.spring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FHIRController {
	
	@Autowired
	CategoryService categoryService;
	
	@RequestMapping(value="/getcategories", method= RequestMethod.GET)
	public @ResponseBody Categories ccdascorecardservice(){
		return categoryService.getAllCategories();
	}

}
