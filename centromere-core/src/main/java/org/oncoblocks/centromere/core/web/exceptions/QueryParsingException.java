package org.oncoblocks.centromere.core.web.exceptions;

/**
 * Thrown when a web service query fails to correctly parse into a WebServiceQuery object.
 * 
 * @author woemler 
 */
public class QueryParsingException extends RuntimeException {
	public QueryParsingException(String message) {
		super(message);
	}
}
