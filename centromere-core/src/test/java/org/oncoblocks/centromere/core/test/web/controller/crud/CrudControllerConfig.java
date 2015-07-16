package org.oncoblocks.centromere.core.test.web.controller.crud;

import org.oncoblocks.centromere.core.web.exceptions.RestExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author woemler
 */

@Configuration
@ComponentScan(basePackages = { "org.oncoblocks.centromere.core.test.web.controller.crud" })
public class CrudControllerConfig {
	
	@ControllerAdvice
	public static class ControllerExceptionHandler extends RestExceptionHandler {}
	
}
