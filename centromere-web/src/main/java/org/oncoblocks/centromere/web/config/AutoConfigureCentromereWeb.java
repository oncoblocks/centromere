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

package org.oncoblocks.centromere.web.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enables default configuration of many of the required web application components, including the
 *   dispatcher servlet, web request handling, CORS support, Swagger, and property mapping.
 * 
 * @author woemler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Import({ 
		WebServicesConfig.class, 
		SpringBootConfig.class, 
		CentromereWebPropertiesConfig.class, 
		SwaggerConfig.class 
})
public @interface AutoConfigureCentromereWeb {
}
