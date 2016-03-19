# Centromere Web

Components for creating REST web services that sit on top of a Centromere data warehouse.  

## Quick Start

### Maven

You can get the latest release build of the Centromere Web module from Maven Central Repository:

```xml
<dependency>
    <groupId>org.oncoblocks.centromere</groupId>
    <artifactId>centromere-web</artifactId>
    <version>0.3.0</version>
</dependency>
```

### Configuration

The easiest way to configure Centromere is to use the available auto-configuration annotations in a Spring Boot application class or supporting configuration class: `@AutoConfigureWebServices`, `@AutoConfigureWebSecurity`, and `@AutoConfigureApiDocumentation`.  This will handle all of the required web context configuration and bean registration for the web services, API documentation, and security features:

```java
@Configuration
@AutoConfigureWebServices
@AutoConfigureWebSecurity
@AutoConfigureApiDocumentation
@ComponentScan(basePackages = { "me.woemler.myapp" })
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(Application.class);
	}

	public static void main(String[] args){
		SpringApplication.run(Application.class, args);
	}

}
```

### Creating Web Service Controllers

The web services controller layer handles HTTP requests and routes them to the appropriate repository implementation.  Web service controllers support standard CRUD operations via `GET`, `POST`, `PUT`, and `DELETE` methods. `HEAD` and `OPTIONS` methods are also supported for the purpose of exposing additional resource endpoint information.

```java
@Controller
@ExposesResourcesFor(Gene.class)
@RequestMapping(value = "/api/genes")
public class GeneController extends CrudApiController<Gene, String> {

	@Autowired
	public GeneController(GeneRepository repository, EntityLinks entityLinks) {
		super(repository, Gene.class,
		new ModelResourceAssembler(GeneController.class, Gene.class, entityLinks));
	}

}
```

## RESTful API

Once your application is up-and-running, you can reach your data using the relative root URLs specified in your controller classes.  For example:

Method | URI | Description
-------|-----|------------
`GET` | `/genes` | Fetches all Genes
`GET` | `/genes/{id}` | Fetch a single Gene by ID
`POST` | `/genes` | Creates a new Gene record
`PUT` | `/genes/{id}` | Updates an existing Gene
`DELETE` | `/genes/{id}` | Deletes an existing Gene
`OPTIONS` | `/genes` | Fetches info about the available Gene operations

#### Media Types

Centromere supports the following media types for `GET` requests: `application/json`, `application/hal+json`, `application/xml`, `application/hal+xml`, and `plain/text`.  If no return type is specified, `application/json` will be used by default.  The media type `plain/text` returns the response data as a tab-delimited text table.  Only requests for the media types `application/hal+json` and `application/hal+xml` will return HATEOAS links.

#### Searching

Centromere supports dynamic query operations using query string parameters.  The available query parameters for each resource are defined in the model class (see the documentation for the `centromere-core` module). You can perform queries using one or more entity attributes in the standard way:

```
GET /genes?alias=akt,mtor,braf
GET /cnv?valueBetween=-0.5,0.5
GET /samples?tissue=lung&type=cellLine
```

#### Paging and Sorting

Requests can return results that are both paginated and sorted.  Centromere uses the default Spring Data URI query parameter syntax:

```
GET /genes?size=100&page=2&sort=entrezGeneId,asc
```

Page numbering starts from zero, and the default page size is 1000 records.  If sorting by multiple fields, use multiple `sort` parameters in your request.  The sorts will be applied in the order given:

```
# Will first sort by `type`, then by `entrezGeneId`
GET /genes?sort=type,desc&sort=entrezGeneId,asc
```

#### Field Filtering

Requests can specify which entity fields will be returned, or excluded:

```
GET /genes?fields=entrezGeneId,primaryGeneSymbol
GET /genes?exclude=description,links
```

#### Hypermedia

For hypermedia support, use the `application/hal+json` or `application/hal+xml` media types to include embedded HAL-formatted links to related entities, allowing for easy resource discovery:

```
Request:
GET /hgu133/rma

Response:
200 OK
[
	{
		sampleId: 123,
		entrezGeneId: 207,
		dataFileId: 43,
		value: 103.12
		links: [
			{ rel: "self", href: "http://myapp/hgu133/rma?sampleId=123&entrezGeneId=207&dataFileId=43"  },
			{ rel: "sample", href: "http://myapp/samples/123"  },
			{ rel: "gene", href: "http://myapp/genes/207"  },
			{ rel: "data_file", href: "http://myapp/datafiles/43"  }
		]
	}, ...
]
```

#### Compression

All responses can be GZIP compressed by including the `Accept-Encoding: gzip,deflate` header:

```
curl -s -H "Accept-Encoding: gzip,deflate" http://myserver/api/genes > genes.gz
```
