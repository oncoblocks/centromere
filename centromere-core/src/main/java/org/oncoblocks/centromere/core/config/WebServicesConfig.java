package org.oncoblocks.centromere.core.config;

import org.oncoblocks.centromere.core.web.util.CorsFilter;
import org.oncoblocks.centromere.core.web.util.FilteringJackson2HttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Default web configuration file for Centromere web services.  Adds the following functionality: 
 *   - Field-filtering via {@link org.oncoblocks.centromere.core.web.util.FilteringJackson2HttpMessageConverter}.
 *   - Default media type handling
 *   - CORS filter support
 *
 * @author woemler
 */

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL })
public class WebServicesConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		FilteringJackson2HttpMessageConverter jsonConverter = new FilteringJackson2HttpMessageConverter();
		jsonConverter.setPrettyPrint(true);
		converters.add(jsonConverter);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer){
		configurer.defaultContentType(MediaType.APPLICATION_JSON);
		configurer.favorPathExtension(true);
		configurer.ignoreAcceptHeader(true);
		configurer.mediaType("txt", MediaType.TEXT_PLAIN);
		configurer.mediaType("csv", new MediaType("text", "csv"));
	}

	@Bean
	public CorsFilter corsFilter(){
		return new CorsFilter();
	}

}
