# Feedzai OpenML API
[![Build Status](https://travis-ci.com/feedzai/feedzai-openml.svg?branch=hf-0.2.X)](https://travis-ci.com/feedzai/feedzai-openml)
[![codecov](https://codecov.io/gh/feedzai/feedzai-openml/branch/hf-0.2.X/graph/badge.svg)](https://codecov.io/gh/feedzai/feedzai-openml)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/052dc81a4434474da9a4f048c40a52eb?branch=hf-0.2.X)](https://www.codacy.com/app/feedzai/feedzai-openml?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=feedzai/feedzai-openml&amp;utm_campaign=Badge_Grade)

Feedzai's extensible Machine Learning API to integrate ML platforms with Feedzai's data science and runtime environment.

## Usage
See the [openml-example project](https://github.com/feedzai/feedzai-openml/tree/master/openml-example) as a trivial example of how to implement a new provider.

When building your OpenML Provider using Maven you can add dependencies on the artifacts in this repository. See the following sections to learn more about these.

### OpenML API
[![Maven metadata URI](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/feedzai/openml-api/maven-metadata.xml.svg)](https://mvnrepository.com/artifact/com.feedzai/openml-api)

The [OpenML API module](https://github.com/feedzai/feedzai-openml/tree/master/openml-api) contains the main concepts that allow interaction between Feedzai's platform and an external ML platform.

```xml
<dependency>
  <groupId>com.feedzai</groupId>
  <artifactId>openml-api</artifactId>
  <!-- See project tags for latest version -->
  <version>0.2.0</version>
</dependency>
```

### OpenML Utils
[![Maven metadata URI](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/feedzai/openml-utils/maven-metadata.xml.svg)](https://mvnrepository.com/artifact/com.feedzai/openml-utils)

The [openml-utils](https://github.com/feedzai/feedzai-openml/tree/master/openml-example) module helps you to manipulate some of the core concepts.

```xml
<dependency>
  <groupId>com.feedzai</groupId>
  <artifactId>openml-utils</artifactId>
  <!-- See project tags for latest version -->
  <version>0.2.0</version>
</dependency>
```

## Building
Build this Maven project using the following command:
```bash
mvn clean install
```


## Developing

Make sure your provider is identified according to the specification of [Java's Service Loader](https://docs.oracle.com/javase/9/docs/api/java/util/ServiceLoader.html). This entails generating a Jar with the code (possibly with all the dependencies necessary in it, or a set of Jars instead), and making sure you include a file `resources/META-INF/services/com.feedzai.openml.MachineLearningProvider` to indicate which providers you have in your code. Check out [our example in this repository](https://github.com/feedzai/feedzai-openml/blob/master/openml-example/src/main/resources/META-INF/services/com.feedzai.mlapi.provider.MachineLearningProvider). It may also be helpful to look into [Google's Auto Service](https://github.com/google/auto/tree/master/service) to set this up for you.



### IDE Compatibility

This project makes use of the [jgitver Maven plugin](https://github.com/jgitver/jgitver). When using Intellij IDEA you
must configure the project to skip the plugin altogether. [See the related issue](https://github.com/jgitver/jgitver-maven-plugin/wiki/Intellij-IDEA-configuration).
