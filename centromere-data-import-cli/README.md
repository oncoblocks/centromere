# Centromere Data Import Command Line Interface

Utility for building and running modular command line import tools for Centromere data warehouses.

## Quick Start

### Maven

You can get the latest release build of the Centromere Data Import CLI module from Maven Central Repository:

```xml
<dependency>
    <groupId>org.oncoblocks.centromere</groupId>
    <artifactId>centromere-data-import-cli</artifactId>
    <version>0.X.0</version>
</dependency>
```

### Configuration

The `centromere-data-import-cli` module makes use of the component classes defined in the `centromere-core` module.  To create a command line import tool for your data import components, you must first define a configuration class that extends `DataImportConfigurer`:

```java
@Configuration
@ComponentScan(basePackages = { "me.woemler.dataimport" })
public class ImportConfig extends DataImportConfigurer {

}
```

The `DataImportConfigurer` will initialize several beans that help manage the import process, including the `DataImportManager`, which handles mapping your implemented `RecordProcessor` classes to user-inputted data files on the command line.  By default, the `DataImportManager` will pick up on all `RecordProcessor` instances that are annotated with the `@DataTypes` annotation and create an association between the provided data type labels and their processor classes.  You can append-to or overwrite the default data type and data set mapping behavior by overriding the xxx methods:

```java
@Configuration
@ComponentScan(basePackages = { "me.woemler.dataimport" })
public class ImportConfig extends DataImportConfigurer {

    @Autowired private ApplicationContext context;

    @Override
    public Map<String, DataSetMetadata> configureDataSetMappings(Map<String, DataSetMetadata> dataSetMap){
        dataSetMap.put("test", new BasicDataSetMetadata(xxx));
        return dataSetMap;
    }

    @Override
    public Map<String, RecordProcessor> configureDataTypeMappings(Map<String, RecordProcessor> dataTypeMap){
        dataTypeMap.put("mutations", context.getBean(MutationProcessor.class));
        return dataTypeMap;
    }

}
```

The `DataImportConfigurer` also creates an instance of a `CommandLineRunner` bean, which will accept and parse command line arguments, and then execute the appropriate actions.  This module utilizes [JCommander](http://jcommander.org/) to define and parse command line arguments, and the command line arguments are defined within the `ImportCommandArguments` and `AddCommandArguments` classes.  To utilize the default command line behavior, make use of the `CommandLineRunner` instance in your main class:

```java
public class Main {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ImportConfig.class);
        CommandLineRunner runner = context.getBean(CommandLineRunner.class);
        runner.run(args);
    }
}
```

## Running

Data import tools tools built with `centromere-data-import-cli` can run as executable JAR files on command line.  When run with the default arguments and configuration, such as is described above, the command line tool syntax is as follows:

```
# Import command
Usage: import [options]
  Options:
    -d, --data-set
       Data set label or JSON representation.  If not provided, no data set will
       be associated with the file.
  * -t, --data-type
       Data type label for the target file.  Required.
  * -i, --input
       File to be imported.  Required.
    --skip-invalid-data-sets
       When true, records and files associated with invalid or existing data
       sets will be skipped, rather than throw an exception.
       Default: false
    --skip-invalid-genes
       When true, records that do not match a valid gene will be skipped, rather
       than throw an exception.
       Default: false
    --skip-invalid-records
       When true, records that fail validation will be skipped, rather than
       throwing an exception.
       Default: false
    --skip-invalid-samples
       When true, records that do not match valid samples will be skipped,
       rather than throw an exception.
       Default: false
    -T, --temp-dir
       Directory to write temporary files to.  Defaults to '/tmp'.
       Default: /tmp
       
# Add command
Usage: add category label body
  Positional arguments:
    category
      Used to specify the record type to be added.  Currently supports 'data_type'
      and 'data_set'.
    label
      Unique string identifier to associate with the record to be added.  Used
      for identification in the import command.
    body
      Content of the record.  For the 'data_type' category, this should be the 
      name of a RecordProcessor class or existing bean.  For the 'data_set'
      category, this should be a JSON representation of the data set metadata.
```
