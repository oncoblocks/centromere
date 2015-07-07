# Oncoblocks: API Design

## About
This document outlines the best practices and guidelines for creating Oncoblocks RESTful API implementations.  

## Terms
- **Model**: Abstract representation of a data type (eg. mutation data).
- **Entity**: An instance of a single model record.  Defined by of one or more attributes.
- **Attribute**: A key-value representation of of defining entity metadata.
- **Primary Key**: An attribute with a unique value (usually an integer) that identifies a single entity.
- **Foreign Key**: An entity attribue that represents the primary key of a related entity.
- **Resource**: A representation of one or more entities as an API response object.
- **[REST](http://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm)**: Representational State Transfer.
- **HATEOAS**: Hypermedia as the Engine of Application State.
- **[HAL](https://tools.ietf.org/html/draft-kelly-json-hal-06)**: Hypertext Application Language.  A JSON format for representing hypermedia links in web services.

## General guidelines

### URL Design

- A URL identifies a resource and maps to a single entity type (model).
- URLs should include nouns, not verbs.
- Use plural nouns only for consistency (no singular nouns).
  - Similar data types represented by different models should be placed in branching URLs.
    - eg. `/expression/microarray` vs `/expression/rnaseq`
  - URLs should branch as little as possible, and only to the earliest point at which different data types can understandably be represented by the same model.
    - eg. `/expression/microarray` could represent data from HGU133A chips, HGU133B chips, etc.
- URLs can include hyphens, but not underscores.
- URLs should use only lower-case letters and numbers.
- API should be behind a subdomain: `api.oncoblocks.org`.

### HTTP Methods

- HTTP verbs (GET, POST, PUT, DELETE, HEAD, and OPTIONS) are used to perform actions upon resources.
  - `GET` requests should be used to retrieve either an entity or a collection of entities.
  - `POST` requests should be used to create new instances of entity records.
  - `PUT` requests should be used to create or update existing records.
  - `DELETE` requests should be used to delete records.
  - `HEAD` requests should be used to retrieve response headers, mirroring `GET` requests.
  - `OPTIONS` requests should be used to retrieve metadata that describes the resource and its parameters.
- Every endpoint should support at least GET, HEAD, and OPTIONS  methods.

### Querying

- Querying (record filtering) is done using query string parameters in requests.
- Requesting a single entity by its primary key ID can be done using url parameters.
  - eg. `/samples/123`
  - If the requested record does not exist, a 404 error is returned.
- Requesting multiple entities by ID is possible using a comma-separated list of identifiers.
  - eg. `/samples?sampleId=123,456,789`
  - If the requested records do not exist, an empty set is returned.
- Filtering is done via URL query string: `/subjects?groups=xyz`
  - Filtering entities by related entities should be done using foreign key IDs, whereas filtering entities by defining attributes should be done using their discreet values.
- Membership queries (one-to-many and many-to-many relationships) should use the endpoint of the entity that is the desired return record.

#### Field Filtering

- All endpoints should support field filtering, allowing requests to specify which entity attributes will be returned.
- Field filtering can be performed with either the `fields` query string parameter, which specifies the attributes to be returned, or with the `exclude` query string parameter, which specifies which attributes should NOT be returned:

```
# Return only name, tissue and histology fields
GET /samples?fields=name,tissue,histology

# Exclude links and sampleId from the response object
GET /samples?exclude=sampleId,links
```

#### Paging and Record Skipping

- All endpoints should support record paging, allowing requests to retrieve records in batches.
- Paging should be performed using the query string parameters `limit` and `offset` or `page`.
  - `size` should specify the maximum number of records to be returned in the collection.
  - `offset` should specify the number of records to be skipped before the first record is returned.
  - `page` should specify which record page to be returned, should optionally be used in conjunction with `size`.
  - eg. `/expression/rnaseq?size=100&page=10`
- Paginated records should be enveloped and annotated with the number of pages, total records, and include links to next/previous pages.
  - eg.

```javascript
{}
```

#### Sorting

- API endpoints should support sorting of data by one or more column names.
  - eg. `/samples?sort=name`
- Records can be sorted in either ascending or descending order (defaults to ascending)
  - eg. `/samples?sort=tissue,desc`
  Multiple sorts can be applied to a single request:
  - eg `/samples?sort=name,asc&sort=tissue,desc`

### Responses

- URL endpoints should return either one entity or a collection of entities.
- The default response type should be `application/json`, but other media types can be supported.
- Headers for GET and HEAD requests should include the number of entities returned, as well as the maximum number of available records (for paged responses).

#### HTTP Status Codes

All requests should return the appropriate HTTP status code.
  - `200 OK` should be used to indicate request success.
  - `201 CREATED` should be used to indicate successful record creation from `POST` and `PUT` requests.
  - `400 BAD REQUEST` should be used for non-specific errors.
  - `401 UNAUTHORIZED` should be used for requests with problematic user credentials.
  - `403 FORBIDDEN` should be used to bar user access, regardless of authentication state.
  - `404 NOT FOUND` should be used when a URL cannot be mapped to a resource.
  - `405 METHOD NOT ALLOWED` should be used when a requested HTTP method is not supported by the endpoint.
  - `415 UNSUPPORTED MEDIA TYPE` should be used when a requested media type is not supported.
  - `500 INTERNAL SERVER ERROR` should be used when the API malfunctions.

#### Headers
- All responses should include XXX headers.

#### Exceptions

- All exceptions should return informative messages, as well as identifying error codes.

#### Hypermedia

- All endpoints should support HATEOAS links, but link retrieval should not be mandatory.
- Hypermedia links should be HAL formatted.
- Hypermedia-enriched entities and collections should always include `self` links.
- Links to related entities should be included where appropriate.
  - eg.
  ```
  {
      sampleId: 123,
      name: "SampleABC",
      subjectId: 456,
      studyId: 3,
      links: {
          self: { href: "http//api.oncoblocks.org/samples?id=123" },
          subject: { href: "http//api.oncoblocks.org/subjects?id=456" },
          study: { href: "http//api.oncoblocks.org/studies?id=3"}
      }
  }
  ```

### Updates
Full/partial updates using PUT
`PUT` should replace any parameters passed and ignore fields not submitted.

Example:

```
GET /items/id_123
{
"id": "id_123",
"meta": {
  "created": "date",
  "published": false
}
}
```

```
PUT /items/id_123 { "meta": { "published": true } }
{
"id": "id_123",
"meta": {
  "published": false
}
}
```


## Caching
Most responses return an ETag header. Many responses also return a Last-Modified header. You can use the values of these headers to make subsequent requests to those resources using the If-None-Match and If-Modified-Since headers, respectively. If the resource has not changed, the server will return a 304 Not Modified. Also note: making a conditional request and receiving a 304 response does not count against your rate limit, so we encourage you to use it whenever possible.

`Cache-Control: private, max-age=60`
`ETag: hash of contents`
`Last-Modified: updated_at`

Vary header
The following headers must be declared in Vary:
`Vary: Accept, Authorization, Cookie`

Any one of these headers can change the representation of the data and should invalidate a cached version. Users might be using different accounts to do admin, all with different privileges and resource visibility.
Accept can alter the returned representation.

Reference: https://www.mnot.net/cache_docs/

## Compression
All responses should support gzip.

## Security
- All API requests should be authenticated.
- Write, update, and delete operations should require increased privileges over read operations.
- Authentication should be stateless (per REST standard).
- API authentication should be done by token, which can be generated at a user authentication endpoint.
    - Token can be passed as request headers or query string parameters.
    - Tokens should contain enough information to validate the user without them having to pass full credentials every time.

```
# Header
X-Auth-Token: woemler:12345678:retg876tr8g6weg7erg8werg6

# Query String
GET http://api.oncoblocks.org/samples?token=woemler:12345678:retg876tr8g6weg7erg8werg6

# Token components
username = woemler
expiration = 12345678
hash = MD5(username:password:expiration:salt)
token = username:expiration:hash = woemler:12345678:retg876tr8g6weg7erg8werg6
```

## HTTP Rate Limiting
All endpoints should support rate limiting.

- eg. Check the returned HTTP headers of any API request to see your current rate limit status:

```
Rate-Limit-Limit: 5000
Rate-Limit-Remaining: 4994
Rate-Limit-Reset: Thu, 01 Dec 1994 16:00:00 GMT
Content-Type: application/json; charset=utf-8
Connection: keep-alive
Retry-After: Thu, 01 May 2014 16:00:00 GMT

RateLimit-Reset uses the HTTP header date format: RFC 1123 (Thu, 01 Dec 1994 16:00:00 GMT)
```

eg. Exceeding rate limit:
```
// 429 Too Many Requests
{
  "message": "API rate limit exceeded.",
  "type": "rate_limit_exceeded",
  "documentation_url": "http://developer.gocardless.com/#rate_limit_exceeded"
}
```

## Versioning
API versions should be represented in the HTTP headers.

## Time zone / dates
Explicitly provide an ISO8601 timestamp with timezone information (Date time in UTC).

