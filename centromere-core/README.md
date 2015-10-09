# Centromere Core

Components for creating database repositories for the Centromere genomic data warehouse.  These components are data-model-agnostic and can be used to fit any data type.  

## Quick Start

### Note

This project is still in early development.  Components can and will change, and the documentation below may become out of sync with the latest build of the project.  I will do my best to keep the documentation up-to-date.

### Creating a Data Model

Centromere is designed for use with the work-in-progress Oncoblocks data model, but is not limited to this specification.  The first step in implementing a data warehouse with Centromere is to design a data model and create representational Java classes for each one.  Once implemented, a single model class will be used for each web service resource endpoint, so model classes should be considered to be atomic, normalized entities.  It is possible to create model classes with nested attributes, but keep in mind that this will add complexity in the repository layer and could affect performance.  You should think of model classes as DTOs that implement the `Model` interface:

```java
/* Simple representation of an Entrez Gene record for a MongoDB database.  */
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

	public Gene() { };

	/* Getters and Setters */
}

/* Simple representation of a subject for a SQL database */
@Filterable
public class Subject implements Model<Integer> {

	private Integer id;
	private String name;
	private String gender;
	private Integer age;
	private List<Attributes> attributes;
	
	public Subject() { };
	
	/* Getters and Setters */

}

```

The `Model` interface ensures that the entity has a defined primary ID, of the specified type (in this case, a `String` representation of a MongoDB `ObjectID`, and an `Integer` representation of an auto-incremented SQL table ID).  The `@Filterable` annotation identifies the entity class as being a candidate for field-filtering operations in the web services layer.  The `Attributes` class is a helper class for creating additional sparse key-value pair attributes for the class. 

For MongoDB databases, you can customize how the model classes are persisted by using [Spring Data annotations](http://docs.spring.io/spring-data/data-document/docs/current/reference/html/#mapping-usage-annotations) in your model class.  By default, persisted models will be created in a collection with the same name as your class, and with the document ID assigned to an attribute with the name `id`, if one exists.

For SQL databases, mapping to and from the repository is handled manually with several user-defined helper classes, adding extra work in development, but also a greater amount of customization in how your data is persisted and represented.

### Creating Repositories

Whereas model class implementations are largely agnostic of the actual database technology that they will be used with, repository implementations will depend on the specific database technology being used.  In general, this will mean extending either `GenericMongoRepository` or `GenericJdbcRepository`.  Both of these classes implement the base `RepositoryOperations` interface, which is based on Spring Data's `PagingAndSortingRepository`, and defines all of the basic CRUD operations that all Centromere repositories should support. The database-specific implementations also include several methods specific to those data stores.

Beyond the standard query operations defined in a `PagingAndSortingRepository` are several methods that support dynamic query execution using the `QueryCriteria` class.  This class represents a single conditional criteria that database queries should meet in order to return results.  In practice, it effectively allows you to generate dynamic `WHERE` statements in your queries using predefined operation sets.  For example, in a SQL database, passing `new QueryCriteria('name', 'Joe', Evalutation.NOT_EQUALS)` to the `.find()` method would result in a statement of `WHERE name != 'Joe'`.  This same criteria used in a MongoDB repository would result in a query of `{ name: { $ne: 'Joe' } }`.  Multiple `QueryCriteria` can be passed into repository query methods to create complex, on-demand queries.

#### MongoDB

By far the simplest repository implementation to configure is the MongoDB implementation.  The `GenericMongoRepository` utilizes Spring Data MongoDB's `MongoTemplate`, which handles query execution and object mapping.  You can also expand on the basic operation set of the repository class by defining your own methods:

```java
@Repository
public class GeneRepository extends GenericMongoRepository<Gene, String> {
	
	@Autowired
	public GeneRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate, Gene.class);
	}
	
	public List<Gene> findByEntrezGeneId(Long entrezGeneId){
	    return this.getMongoOperations()
	        .find(new Query(Criteria.where("entrezGeneId").is(entrezGeneId));
	}
	
}
```

#### SQL Databases

Repository implementations for SQL databases are more complex, but still allow the same flexibility and query power that that the MongoDB repositories allow.  The `GenericJdbcRepository` is based on the class, `com.nurkiewicz.jdbcrepository.JdbcRepository`, but uses a more complex version of the `TableDescription` class, and a custom SQL generation class, `SqlBuilder`.  Much like with `JdbcRepository`, you define a `GenericJdbcRepository` using a `ComplexTableDescription`, `RowMapper`, and optional `RowUnmapper`.  The following repository class implementation supposes that the `Subject` class defined above is persisted in a signle table:
 
```java
/* Subject data stored in a single table */
@Repository
public SubjectRepository extends GenericJdbcRepository<Subject, Integer> {
	
	@Autowired
	public SubjectRepository(DataSource dataSource){
	    super(dataSource, new SubjectTableDescription(), new SubjectMapper(), new SubjectUnmapper());
	}
	
	public static class SubjectTableDescription extends ComplexTableDescription {
		public SubjectTableDescription(){
			super("subjects", Arrays.asList("subject_id"));
		}
	}
	
	public static class SubjectMapper implements RowMapper<Subject> {
		@Override
		public Subject mapRow(ResultSet rs, int i){
			Subject subject = new Subject();
			subject.setId(rs.getInt("subject_id"));
			subject.setName(rs.getString("name"));
			subject.setAge(rs.getInt("age"));
			subject.setGender(rs.getString("gender"));
			List<Attributes> attributes = new ArrayList();
			if (rs.getString("attributes") != null){
				for (String attribute: rs.getString("attributes").split(":::")){
					String[] bits = attributes.split("::");
					attributes.add(new Attribute(bits[0], bits[1]));
				}
			}
			subject.setAttributes(attributes);
			return subject;
		}
	}
	
	public static class SubjectUnmapper implements RowUnmapper<Subject> {
		@Override
		public Map<String,Object> mapColumns(Subject subject){
			Map<String,Object> map = new HashMap();
			map.put("subject_id", subject.getId());
			map.put("name", subject.getName());
			map.put("age", subject.getAge());
			map.put("gender", subject.getGender());
			boolean flag = false;
			StringBuilder sb = new StringBuilder();
			for (Attribute attribute: subject.getAttributes()){
				if (flag){
					sb.append(":::");
				}
				flag = true;
				sb.append(attribute.getName()).append("::").append(attribute.getValue());
			}
			map.put("attributes", sb.toString());
			return map;
		}
	}
	
}
```

If the `RowUnmapper` is left out, the repository will be read-only, and all `insert` or `update` method calls will result in an exception.

Storing `Subject` records with their `Attributes` in a single table is simple enough, but it is not ideal.  The better solution would be to create a separate table, `subject_attributes`, with a many-to-one relationship with the `subjects` table.  This would allow you to index the attribute names for easier queries:

```java
/* Subject data stored in two MySQL tables */
@Repository
public SubjectRepository extends GenericJdbcRepository<Subject, Integer> {
	
	@Autowired
	public SubjectRepository(DataSource dataSource){
	    super(dataSource, new SubjectTableDescription(), new SubjectMapper());
	}
	
	@Override
	public <S extends Subject> S insert(S entity) {
    
    		KeyHolder keyHolder = new GeneratedKeyHolder();
    		this.getJdbcTemplate().update(
    				new PreparedStatementCreator() {
    					@Override
    					@SuppressWarnings("JpaQueryApiInspection")
    					public PreparedStatement createPreparedStatement(Connection connection) throws
    							SQLException {
    						PreparedStatement ps = connection.prepareStatement(
    								"INSERT INTO `subjects` (name, gender, age) VALUES (?, ?, ?);",
    								new String[] {"id"}
    						);
    						ps.setString(1, entity.getName());
    						ps.setString(2, entity.getGender());
    						ps.setInt(3, entity.getAge());
    						return ps;
    					}
    				},
    				keyHolder
    		);
    		Integer subjectId = keyHolder.getKey().intValue();
    		entity.setId(Integer.toString(geneId));
    		
    		if (entity.getAttributes() != null) {
    			for (Attribute attribute : entity.getAttributes()) {
    				this.getJdbcTemplate().update(
    						"INSERT INTO `subject_attributes` (subject_id, name, value) VALUES (?, ?, ?)",
    						subjectId, attribute.getName(), attribute.getValue());
    			}
    		}
    
    		return entity;
    
    	} 
	
	public static class SubjectTableDescription extends ComplexTableDescription {
		public SubjectTableDescription(){
			super("subjects", Arrays.asList("s.subject_id"), 
			"s.*, GROUP_CONCAT(CONCAT(a.name, '::', a.value) SEPARATOR ':::') as attributes",
			"subjects s left join subject_attributes a on s.subject_id = a.subject_id",
			"s.subject_id");
		}
	}
	
	public static class SubjectMapper implements RowMapper<Subject> {
		@Override
		public Subject mapRow(ResultSet rs, int i){
			Subject subject = new Subject();
			subject.setId(rs.getInt("subject_id"));
			subject.setName(rs.getString("name"));
			subject.setAge(rs.getInt("age"));
			subject.setGender(rs.getString("gender"));
			List<Attributes> attributes = new ArrayList();
			if (rs.getString("attributes") != null){
				for (String attribute: rs.getString("attributes").split(":::")){
					String[] bits = attributes.split("::");
					attributes.add(new Attribute(bits[0], bits[1]));
				}
			}
			subject.setAttributes(attributes);
			return subject;
		}
	}
	
}
```

Now we have a CRUD repository that pulls data from two tables into a single model class.  
