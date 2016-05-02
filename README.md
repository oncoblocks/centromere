# Centromere [![Build Status](https://travis-ci.org/oncoblocks/centromere.svg?branch=master)](https://travis-ci.org/oncoblocks/centromere)  [![Codacy Badge](https://api.codacy.com/project/badge/grade/0de4199b97fa4d55aae439674e0c4879)](https://www.codacy.com/app/willoemler/centromere)
### Genomics Data Warehouse and REST API Framework

## About

Centromere is a set of tools for developing scalable data warehouses and RESTful web services for processed genomic data.  It is designed to be modular and flexible, allowing you to mold it to fit your data model and business needs. Centromere is developed using the open-source, enterprise-grade Spring Framework, and supports integration with multiple database technologies.  You can use Centromere to create a new data warehouse from scratch, or bootstrap one or more existing databases, and make your data available via a customizable REST API.

Centromere aims to help solve some common problems inherent in monolithic bioinformatics app development:
- Fragmentation of data across large organizations.
- Inconsistent annotation.
- Horizontal and vertical scalability.
- Repetition of work in software development.

What Centromere is _**not**_:
- A LIMS system.
- A repository for raw genomic data.
- An analysis platform.
- An end-user GUI application.

## Modules

#### Centromere Core

The core module, containing common dependencies classes for creating data model, repository, and data import component classes.

#### Centromere Data Import CLI

Utility classes for creating command line import tools.  Builds on the data import components in the `centromere-core` module and adds some default behaviors for a command line interface.

#### Centromere MongoDB and SQL

Database-specific implementations of the core repository interfaces and data import classes.  MongoDB is the preferred database technology, but MySQL is currently supported at an experimental level.

#### Centromere Web

The web module contains all components of the REST web services, including security and API documentation tools.

## Quick Start
Please see each individual module's README for instructions on getting started.  Artifacts for Centromere release builds are available from the Maven Central Repository:

```xml
<dependency>
    <groupId>org.oncoblocks.centromere</groupId>
    <artifactId>centromere-core</artifactId>
    <version>0.4.0</version>
</dependency>

<dependency>
    <groupId>org.oncoblocks.centromere</groupId>
    <artifactId>centromere-data-import-cli</artifactId>
    <version>0.4.0</version>
</dependency>

<dependency>
    <groupId>org.oncoblocks.centromere</groupId>
    <artifactId>centromere-mongodb</artifactId>
    <version>0.4.0</version>
</dependency>

<dependency>
    <groupId>org.oncoblocks.centromere</groupId>
    <artifactId>centromere-sql</artifactId>
    <version>0.4.0</version>
</dependency>

<dependency>
    <groupId>org.oncoblocks.centromere</groupId>
    <artifactId>centromere-web</artifactId>
    <version>0.4.0</version>
</dependency>
```

## Demo

A demo implementation of a Centromere data warehouse and web API is available as a [GitHub repository](https://github.com/oncoblocks/centromere-demo).  This demo utilizes a small data set of cancer genomic data from the TCGA to showcase the features of Centromere's web service and data import utilities.  A hosted instance of this demo is coming soon.

## Contact

For questions about Centromere, or if you are interested in contributing, please contact:
  - [Will Oemler](mailto:woemler@blueprintmedicines.com), Blueprint Medicines

## License

Copyright 2016 William Oemler, Blueprint Medicines

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
