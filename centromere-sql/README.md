# Centromere SQL

Repository implementations for SQL databases.

## Quick Start

### Maven

You can get the latest release build of the Centromere SQL module from Maven Central Repository:

```xml
<dependency>
    <groupId>org.oncoblocks.centromere</groupId>
    <artifactId>centromere-sql</artifactId>
    <version>0.3.0</version>
</dependency>
```

### Creating Repositories

The `GenericJdbcRepository` is the JDBC SQL database implementation of `RepositoryOperations`.  This repository implementation is based on  `com.nurkiewicz.jdbcrepository.JdbcRepository`, but uses a more complex version of the `TableDescription` class, and a custom SQL generation class, `SqlBuilder`.  Much like with `JdbcRepository`, you define a `GenericJdbcRepository` using a `ComplexTableDescription`, `RowMapper`, and optional `RowUnmapper`.  For example:

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
			super(
				"subjects", // table name
				Arrays.asList("subject_id") // primary key ID columns
			);
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
			super(
				"subjects", // table name
				Arrays.asList("s.subject_id"), // primary key IDs
				"s.*, GROUP_CONCAT(CONCAT(a.name, '::', a.value) SEPARATOR ':::') as attributes", // SELECT statement
				"subjects s left join subject_attributes a on s.subject_id = a.subject_id", // FROM statement
				"s.subject_id" // GROUP BY statement
			);
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
