# Centromere Core

Components for creating database repositories for the Centromere genomic data warehouse.  These components are data-model-agnostic and can be used to fit any data type.  

## Quick Start

### Note

This project is still in early development.  Components can and will change, and the documentation below may become out of sync with the latest build of the project.  I will do my best to keep the documentation up-to-date.

### Data Model

Centromere is designed for use with the work-in-progress Oncoblocks data model, but is not limited to this specification.  The first step in implementing a data warehouse with Centromere is to design a data model and create representational `Model` classes for each resource.  This class will serve to represent the resource in the web services and transfer data in and out of the repository layer:

```java
/* Simple MongoDB representation of an Entrez Gene record  */
@Filterable
public class Gene implements Model<String> {

	private String id;
	private Long entrezGeneId;
	private String primaryGeneSymbol;
	private Integer taxId;
	private String locusTag;
	private String chromosome;
	private String chromosomeLocation;
	private String description;
	private String geneType;
	private Set<String> aliases;

	public Gene() { }

	/* Getters and Setters */
}
```

The `Model` interface ensures that the entity has a defined primary ID, of the specified type (in this case, a `String` representation of a MongoDB `ObjectID`).  The `@Filterable` annotation identifies the entity class as being a candidate for field-filtering operations in the web services layer.

### Repository

Choose the appropriate `RepositoryOperations` implementation, based upon your database technology, and define a class to handle repository operations of your entity classes:

```java
@Repository
public class GeneRepository extends GenericMongoRepository<Gene, String> {
	@Autowired
	public GeneRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate, Gene.class);
	}
}
```

The default repository class implementations include most of the same method signatures as Spring Data repository classes, supporting standard CRUD operations, plus some additional methods for dynamic queries.  Custom methods can also be added to extend the base repository functionality.
