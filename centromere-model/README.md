# Centromere Model

Components for designing and implementing data models for Centromere genomics data warehouses.  These components are designed to be used with the Centromere Core module.

## Creating Data Models

The this module provides some standard data model entity definitions, but if you wish to develop your own data model, Centromere can support it, so long as you follow several guidelines.

#### Schema management should be handled outside Centromere

Though Spring Data and standard JDBC support allow for schema manipulation and administration, these tasks are best handled outside of the scope of a Centromere instance.  This will help ensure that tables and collections are generated correctly, indexes are proerly implemented, and constraints are enforced.  There may also be times when creating and running database functions to support Centromere functionality simply is not possible within the application framework.  In general, to ensure peak performance, it is always best to manage database instances the old fashioned way, anyways. 

#### Resources should map to a single entity class

As a general rule, resources that are to be represented in the web services application should map only to one entity class.  This does not mean that a web service DTO cannot contain data derived from more than one table or collection. It is perfectly acceptable to have a database view that combines multiple tables represented as a DTO, but subclassing of entity types should be avoided, to ensure proper read/write mapping.

#### Always use generated primary key IDs

It is good database design practice that primary key IDs never contain any meaning beyond being a unique identifier.  To this end, it is best to use generated ID values for each resource, and that these IDs not be compound field identifiers.  Additionally, the same field type should be used for primary key IDs, across all entities (for example, `String` for MongoDB, or `Long` for SQL).  Entity IDs are used for unique identification and reference in the web services layer, the simpler representation in a URL, the better.

#### Entity classes should be atomic

Entity classes and their web service representations should always be atomic, and not contain any embedded sub-entities.  Relationships to other entity types should be indicated by a foreign key identifier field, with a value corresponding to the related entity's primary key ID.
