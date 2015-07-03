package org.oncoblocks.centromere.core.model;

import java.util.Set;

/**
 * @author woemler
 */
public interface SimpleAliases {
	Set<String> getAliases();
	void setAliases(Set<String> aliases);
	void setAlias(String alias);
	boolean hasAlias(String alias);
}
