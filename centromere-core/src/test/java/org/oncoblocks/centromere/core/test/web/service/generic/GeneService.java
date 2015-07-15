package org.oncoblocks.centromere.core.test.web.service.generic;

import org.oncoblocks.centromere.core.test.models.Gene;
import org.oncoblocks.centromere.core.test.repository.mongo.GeneRepository;
import org.oncoblocks.centromere.core.web.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author woemler
 */

@Service
public class GeneService extends GenericService<Gene, Long> {
	@Autowired
	public GeneService(GeneRepository repository) {
		super(repository);
	}
}
