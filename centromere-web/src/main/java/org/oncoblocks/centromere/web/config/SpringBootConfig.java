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

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

/**
 * Default configuration for Spring Boot web service applications.  Adds the following features:
 *   - GZIP compression of request responses using the 'Accept-Encoding: gzip,deflate' header.
 *
 * @author woemler
 */

@Configuration
public class SpringBootConfig {

	@Bean
	public EmbeddedServletContainerCustomizer servletContainerCustomizer(){
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
				((TomcatEmbeddedServletContainerFactory) configurableEmbeddedServletContainer).addConnectorCustomizers(
						new TomcatConnectorCustomizer() {
							@Override
							public void customize(Connector connector) {
								AbstractHttp11Protocol httpProtocol = (AbstractHttp11Protocol) connector.getProtocolHandler();
								httpProtocol.setCompression("on");
								httpProtocol.setCompressionMinSize(256);
								String mimeTypes = httpProtocol.getCompressableMimeTypes();
								String mimeTypesWithJson = mimeTypes + "," + MediaType.APPLICATION_JSON_VALUE;
								httpProtocol.setCompressableMimeTypes(mimeTypesWithJson);
							}
						}
				);
			}
		};
	}

}
