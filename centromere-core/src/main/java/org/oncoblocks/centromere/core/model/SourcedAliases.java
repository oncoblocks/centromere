package org.oncoblocks.centromere.core.model;

import java.util.Collection;

/**
 * Interface for adding controller query methods to entity classes with key-value aliases.  
 * 
 * @author woemler
 */
public interface SourcedAliases {
	Collection<SourcedAlias> getAliases();
	void setAliases(Collection<SourcedAlias> aliases);
	void setAliasName(String aliasName);
	void setAliasSource(String aliasSource);
	void setAlias(String alias);
	boolean hasAlias(String name);
}
