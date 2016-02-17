# Centromere MongoDB

Repository class implementations for MongoDB.

## Quick Start

### Creating Repositories

The `GenericMongoRepository` is the MongoDB implementation of `RepositoryOperations`, utilizing Spring Data MongoDB's `MongoTemplate` for query execution and object mapping.  Before creating repository classes, you should configure your database connection:

```java
@Configuration
public class MongoConfig {

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
