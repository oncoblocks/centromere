# Centromere MongoDB

Repository class implementations for MongoDB.

## Quick Start

### Maven

You can get the latest release build of the Centromere MongoDb module from Maven Central Repository:

```xml
<dependency>
    <groupId>org.oncoblocks.centromere</groupId>
    <artifactId>centromere-mongodb</artifactId>
    <version>0.3.0</version>
</dependency>
```

### Creating Repositories

The `GenericMongoRepository` is the MongoDB implementation of `RepositoryOperations`, utilizing Spring Data MongoDB's `MongoTemplate` for query execution and object mapping.  Before creating repository classes, you should configure your database connection:

```java
/* Example configuration for a MongoDB 3.x instance */
@Configuration
@PropertySource({ "classpath:mongodb-data-source.properties" })
public class MongoConfig extends AbstractMongoConfiguration {

	@Autowired private Environment env;

	@Override
	public String getDatabaseName(){
		return env.getRequiredProperty("mongo.name");
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		ServerAddress serverAddress = new ServerAddress(env.getRequiredProperty("mongo.host"));
		List<MongoCredential> credentials = new ArrayList<>();
		credentials.add(MongoCredential.createScramSha1Credential(
				env.getRequiredProperty("mongo.username"),
				env.getRequiredProperty("mongo.name"),
				env.getRequiredProperty("mongo.password").toCharArray()
		));
		return new MongoClient(serverAddress, credentials);
	}

}
```

A simple implementation of a MongoDB repository class looks like this:

```java
@Repository
public class GeneRepository extends GenericMongoRepository<Gene, String> {
	@Autowired
	public GeneRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate, Gene.class);
	}
}
```

You can also expand on the basic operation set of the repository class by defining your own methods:

```java

@Repository
public class GeneRepository extends GenericMongoRepository<Gene, String> {

    @Autowired
	public GeneRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate, Gene.class);
	}

    /* Using the MongoDB driver `Query` class and API */

    public List<Gene> findByEntrezGeneId(Long entrezGeneId){
        return this.getMongoOperations()
            .find(new Query(Criteria.where("entrezGeneId").is(entrezGeneId));
    }

    /* Using `QueryCriteria` and the Centromere repository API */

    public List<Gene> findByGeneSymbolAlias(String alias){
        return this.find(new QueryCriteria("aliases", alias, Evaluation.EQUALS));
    }

}



```
