package org.oncoblocks.centromere.core.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author woemler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Import({ WebServicesConfig.class, SpringBootConfig.class })
public @interface AutoConfigureCentromereWeb {
}
