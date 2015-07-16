package org.oncoblocks.centromere.core.test.web.controller.entity;

import org.oncoblocks.centromere.core.web.exceptions.RestExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author woemler
 */

@Configuration
@ComponentScan(basePackages = { "org.oncoblocks.centromere.core.test.web.controller.entity" })
public class EntityControllerConfig {
	
	@ControllerAdvice
	public static class ControllerExceptionHandler extends RestExceptionHandler {}
	
}
