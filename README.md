# Feedzai OpenML API

Feedzai's extensible Machine Learning API to integrate ML platforms with Feedzai's data science and runtime environment.

## Usage
The `openml-example` shows how a new provider can be implemented.

When building your OpenML Provider using Maven you can add dependencies on the artifacts in this repository. They are:

### OpenML API
These are the main concepts that must extended/implemented to allow interaction with a new platform.

```xml
<dependency>
  <groupId>com.feedzai</groupId>
  <artifactId>openml-api</artifactId>
  <version>1.0.0</version>
</dependency>
```

### OpenML Utils
Library of helpful code to ease the manipulation of some of the core concepts.

```xml
<dependency>
  <groupId>com.feedzai</groupId>
  <artifactId>openml-utils</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Building
This is a maven project which you can build using
```bash
mvn clean install
```
