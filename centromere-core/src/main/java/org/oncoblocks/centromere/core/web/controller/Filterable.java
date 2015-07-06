package org.oncoblocks.centromere.core.web.controller;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows field filtering during JSON serialization, XML marshalling, etc.
 * 
 * @author woemler
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@JacksonAnnotationsInside
@JsonFilter("fieldFilter")
public @interface Filterable {
}
