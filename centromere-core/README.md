# Centromere Core

Components for creating database repositories for Centromere data warehouses.  These components are data-model-agnostic and can be used to fit any data type.  

## Quick Start

### Creating a Data Model

The first step in implementing a data warehouse with Centromere is to design a data model and create representational Java classes for each entity.  Once implemented, a single model class will be used for each web service endpoint, so model classes should be considered to be atomic, normalized entities.  All data model entity classes should implement the `Model` interface:

```java
/* Simple representation of an Entrez Gene record.  */
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
	private Map<String,String> attributes;

	public String getId(){ return id; }

	/* Getters and Setters */
}

```

The `Model` interface ensures that the entity has an identifying attribute of a specified type (in this case, a `String` representation of a MongoDB `ObjectID`).  It is possible to use composite primary key ids, in which case the implemented `getId()` method would return the required fields:

```java
public String getId(){
	return this.entrezGeneId.toString() + "-" + this.primaryGeneSymbol;
}

/* OR */

public Collection<String> getId(){
	return Arrays.asList(this.entrezGeneId, this.primaryGeneSymbol);
}
```

Each implementation of the `Model` interface inherits the `@Filterable` annotation, which identifies the entity class as being a candidate for field-filtering operations in the web services layer.

For MongoDB databases, you can customize how the model classes are persisted by using [Spring Data annotations](http://docs.spring.io/spring-data/data-document/docs/current/reference/html/#mapping-usage-annotations) in your model class.  By default, persisted models will be created in a collection with the same name as your class, and with the document ID assigned to an attribute with the name `id`, if one exists.

For SQL databases, mapping to and from the repository is handled manually with several user-defined helper classes, adding extra work in development, but also a greater amount of customization in how your data is persisted and represented.

### Repositories

All Centromere repository classes implement the base `RepositoryOperations` interface, which defines all of the basic CRUD operations that all Centromere repositories should support.  This interface is based on Spring Data's `PagingAndSortingRepository`, but with some additional method definitions for dynamic query operations.  The database-specific implementations also include several methods specific to those data stores.  A MongoDB repository implementation for the above `Gene` model class might look like this:

```java
@ModelRepository(Gene.class)
public class GeneRepository extends GenericMongoRepository<Gene, String> {
	@Autowired
	public GeneRepository(MongoTemplate mongoTemplate){
		super(mongoTemplate, Gene.class);
	}
}
```

Dynamic repository queries in Centromere are created by chaining a series of query operations, represented by the `QueryCriteria` class.  These operations are defined by the field to be queried, the value of the field, and the operator to be used to make the evaluation.  For example:

```java
QueryCriteria criteria = new QueryCriteria("entrezGeneId", 1L, Evaluation.EQUALS)

/*
Will be translated based upon the database implementation to:
   WHERE entrezGeneId = 1 # for SQL
   or
   {"entrezGeneId": 1} # for MongoDB
*/

```

### Data Import

The `centromere-core` module also contains a number of classes intended to aid in the development of data import pipelines.  The core of this are four basic interfaces: `RecordReader`, `RecordWriter`, `RecordImporter`, and Spring's `Validator`.  When combined with a `RecordProcessor`, these components create a utility for importing a specific data type input into database records.  For example, components for importing Entrez Gene records into a MongoDB database might look like this:

```java
/* RecordReaders take an input data source and return Model objects.*/
public class GeneInfoReader extends AbstractRecordFileReader<Gene> {

	@Override
	public Gene readRecord() throws DataImportException {
		Gene gene = null;
		String line;
		try {
			boolean flag = true;
			while(flag) {
				line = this.getReader().readLine();
				if (line == null || !line.startsWith("#Format: tax_id GeneID")) {
					flag = false;
					if (line != null && !line.equals("")) gene = getRecordFromLine(line);
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return gene;
	}

	private Gene getRecordFromLine(String line){
		String[] bits = line.split("\\t");
		Gene gene = new Gene();
		gene.setTaxId(Integer.parseInt(bits[0]));
		gene.setEntrezGeneId(Long.parseLong(bits[1]));
		gene.setPrimaryGeneSymbol(bits[2]);
		gene.setAliases(new HashSet<>(Arrays.asList(bits[3].split("\\|"))));
		gene.setChromosome(bits[5]);
		gene.setChromosomeLocation(bits[6]);
		gene.setDescription(bits[7]);
		gene.setGeneType(bits[8]);
		return gene;
	}

}

/* Validators asses whether a Model object has been correctly constructed.*/
public class GeneValidator implements Validator {

	public boolean supports(Class<?> aClass) {
		return aClass.equals(Gene.class);
	}

	public void validate(Object o, Errors errors) {
		Gene gene = (Gene) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,  "primaryGeneSymbol", "symbol.empty");
	}
}

/* RecordWriters take Model objects and write them to temporary files or directly to a database.*/
@Component
public class GeneRepositoryWriter extends RepositoryRecordWriter<Gene> {
	@Autowired
	public GeneRepositoryWriter(GeneRepository repository){
		super(repository);
	}
}

/* RecordProcessors tie all of these components together to import a specific data type. */
@Component
public class GeneInfoProcessor extends GenericRecordProcessor<Gene> {
	@Autowired
	public GeneInfoProcessor(GeneRepositoryWriter writer){
		super(new GeneInfoReader(), new GeneValidator(), writer, null, new BasicImportOptions());
	}
}
```

## Advanced Configuration

### Customizing Model Classes

By default, all fields in classes that implement `Model` are exposed as valid query string parameters in the web services layer.  You can further customize a resource's query parameters by applying several annotations:

```java
public class Gene implements Model<String> {

	private String id;
	private Long entrezGeneId;
	@Alias("symbol") private String primaryGeneSymbol;
	private Integer taxId;
	@Ignored private String locusTag;
	private String chromosome;
	@Ignored private String chromosomeLocation;
	@Ignored private String description;
	private String geneType;
	@Alias("alias") private Set<String> aliases;
	@Aliases({
		@Alias(value = "isKinase", fieldName = "attributes.kinase"),
		@Alias(value = "isCgcGene", fieldName = "attributes.cgcGene")
	})
	private Map<String,String> attributes;

	/* Getters and Setters */
}
```

The `Alias` annotation allows query string parameters to map to entity fields of a different name, or to nested fields, with an optional value of `Evaluation`, different from the standard equality test.

It is also possible to customize HATEOAS link generation in the web service layer by using the `ForeignKey` annotation:

```java
public class CopyNumber implements Model<String> {

	private String id;

	@ForeignKey(model = Sample.class, relationship = ForeignKey.Relationship.MANY_TO_ONE, rel = "sample")
	private String sampleId;

	@ForeignKey(model = Gene.class, relationship = ForeignKey.Relationship.MANY_TO_ONE,
			rel = "gene", field = "entrezGeneId")
	private String geneId;

	@Aliases({
			@Alias(value = "signalGreaterThan", evaluation = Evaluation.GREATER_THAN),
			@Alias(value = "signalLessThan", evaluation = Evaluation.LESS_THAN),
			@Alias(value = "signalBetween", evaluation = Evaluation.BETWEEN),
			@Alias(value = "signalOutside", evaluation = Evaluation.OUTSIDE_INCLUSIVE)
	})
	private Double signal;

	/* Getters and Setters */

}
```

All model classes will have `self` links created when HATEOAS-supported media types are requested in the web services layer.  Classes with `ForeignKey`-annotated field will also get links generated based upon the relationship described in the annotation parameters:

```javascript
{
	"id": "123",
	"sampleId": "456",
	"geneId": 789,
	"signal": 2.45,
	"links": [
		{ "rel": "self", "href": "/api/cnv/123" },
		{ "rel": "sample", "href": "/api/samples/456" },
		{ "rel": "gene", "href": "/api/genes?entrezGeneId=789" }
	]
}
```

### Creating a Data Import Pipeline

The data import quick start section above illustrates how to create basic data import components using the helper classes of the `centromere-core` library.  
