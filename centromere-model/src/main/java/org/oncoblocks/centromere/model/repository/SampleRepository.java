/*
 * Copyright 2015 William Oemler, Blueprint Medicines
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

package org.oncoblocks.centromere.model.repository;

import org.oncoblocks.centromere.model.common.Sample;

import java.io.Serializable;
import java.util.List;

/**
 * Required repository operations for Samples.
 * 
 * @author woemler
 */
public interface SampleRepository<T extends Sample> {
	<S extends Serializable> T findBySampleId(S sampleId);
	<S extends Serializable> List<T> findBySubjectId(S subjectId);
	<S extends Serializable> List<T> findByDataSetId(S dataSetId);
	List<T> findByName(String name);
	<S extends Serializable> List<T> findByStudyId(S studyId);
}
