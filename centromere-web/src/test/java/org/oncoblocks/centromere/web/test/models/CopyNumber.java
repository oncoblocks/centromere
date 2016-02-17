/*
 * Copyright 2016 William Oemler, Blueprint Medicines
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.oncoblocks.centromere.web.test.models;

import org.oncoblocks.centromere.core.model.*;
import org.oncoblocks.centromere.core.repository.Evaluation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author woemler
 */
@Document(collection = "copy_number")
public class CopyNumber implements Model<String> {

	@Id private String id;

	@ForeignKey(model = EntrezGene.class, relationship = ForeignKey.Relationship.MANY_TO_ONE, 
			rel = "gene", field = "entrezGeneId")
	private String geneId;

	@Alias("gene")
	private String geneSymbol;

	@Aliases({
			@Alias(value = "signalGreaterThan", evaluation = Evaluation.GREATER_THAN),
			@Alias(value = "signalLessThan", evaluation = Evaluation.LESS_THAN),
			@Alias(value = "signalBetween", evaluation = Evaluation.BETWEEN),
			@Alias(value = "signalOutside", evaluation = Evaluation.OUTSIDE_INCLUSIVE)
	})
	private Double signal;

	@Ignored
	private String flag;

	public CopyNumber() {
	}

	public CopyNumber(String id, String geneId, String geneSymbol, Double signal, String flag) {
		this.id = id;
		this.geneId = geneId;
		this.geneSymbol = geneSymbol;
		this.signal = signal;
		this.flag = flag;
	}

	@Override public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGeneId() {
		return geneId;
	}

	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}

	public String getGeneSymbol() {
		return geneSymbol;
	}

	public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	}

	public Double getSignal() {
		return signal;
	}

	public void setSignal(Double signal) {
		this.signal = signal;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String description) {
		this.flag = description;
	}
}
