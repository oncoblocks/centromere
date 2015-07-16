package org.oncoblocks.centromere.core.test.web.controller.criteria;

import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.test.models.Subject;
import org.oncoblocks.centromere.core.test.web.service.remapping.SubjectService;
import org.oncoblocks.centromere.core.web.controller.FilterableResource;
import org.oncoblocks.centromere.core.web.controller.QueryCriteriaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author woemler
 */

@Controller
@RequestMapping(value = "/subjects")
public class SubjectQueryCriteriaController extends QueryCriteriaController<Subject,Long> {
	
	@Autowired
	public SubjectQueryCriteriaController(SubjectService service) {
		super(service, new SubjectAssembler());
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity find(
			@RequestParam(value = "fields", required = false) Set<String> fields,
			@RequestParam(value = "exclude", required = false) Set<String> exclude,
			@PageableDefault(size = 1000) Pageable pageable,
			PagedResourcesAssembler<Subject> pagedResourcesAssembler, 
			HttpServletRequest request,
			@RequestParam(required = false) Long subjectId,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String aliasName,
			@RequestParam(required = false) String attributeName,
			@RequestParam(required = false) String attributeValue
	) {
		List<QueryCriteria> criterias = new ArrayList<>();
		if (subjectId != null) criterias.add(new QueryCriteria("subjectId", subjectId));
		if (name != null) criterias.add(new QueryCriteria("name", name));
		if (aliasName != null) criterias.add(new QueryCriteria("aliasName", aliasName));
		if (attributeName != null) criterias.add(new QueryCriteria("attributeName", attributeName));
		if (attributeValue != null) criterias.add(new QueryCriteria("attributeValue", attributeValue));
		return doFind(criterias, fields, exclude, pageable, pagedResourcesAssembler, request);
	}

	public static class SubjectAssembler extends ResourceAssemblerSupport<Subject, FilterableResource> {

		public SubjectAssembler() {
			super(SubjectQueryCriteriaController.class, FilterableResource.class);
		}

		@Override 
		public FilterableResource toResource(Subject subject) {
			FilterableResource<Subject> resource = new FilterableResource<>(subject);
			resource.add(linkTo(SubjectQueryCriteriaController.class).slash(subject.getId()).withSelfRel());
			return resource;
		}
	}
	
}
