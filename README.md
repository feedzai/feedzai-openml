# Feedzai OpenML API
[![Build Status](https://travis-ci.org/feedzai/feedzai-openml-r.svg?branch=master)](https://travis-ci.org/feedzai/feedzai-openml-r)
[![codecov](https://codecov.io/gh/feedzai/feedzai-openml/branch/master/graph/badge.svg)](https://codecov.io/gh/feedzai/feedzai-openml)


Feedzai's extensible Machine Learning API to integrate ML platforms with Feedzai's data science and runtime environment.

## Usage
The `openml-example` shows how a new provider can be implemented.

When building your OpenML Provider using Maven you can add dependencies on the artifacts in this repository. They are:

### OpenML API
[![Maven metadata URI](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/feedzai/openml-api/maven-metadata.xml.svg)](https://mvnrepository.com/artifact/com.feedzai/openml-api)

These are the main concepts that must extended/implemented to allow interaction with a new platform.

```xml
<dependency>
  <groupId>com.feedzai</groupId>
  <artifactId>openml-api</artifactId>
  <version>1.0.0</version>
</dependency>
```

### OpenML Utils
[![Maven metadata URI](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/feedzai/openml-utils/maven-metadata.xml.svg)](https://mvnrepository.com/artifact/com.feedzai/openml-utils)

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


## Developing

The key concept is that of a `MachineLearningProvider`, i.e., implementations of that interface are capable of yielding objects that can load machine learning models (already trained) in a way that Feedzai platform can use them.

An extension of that interface resides in `TrainingMachineLearningProvider`, which adds the ability to train new machine learning algorithms within Feedzai platform.

These are the entry point for developers:
an implementation of either results in an OpenML provider that can be used for, respectively, loading and training external algorithms to those already provided by Feedzai.

The `openml-example` project shows a trivial implementation of those concepts.

### IDE Compatibility

This project makes use of the [jgitver Maven plugin](https://github.com/jgitver/jgitver). When using Intellij IDEA you
must configure the project to skip the plugin altogether: [see issue](https://github.com/jgitver/jgitver-maven-plugin/wiki/Intellij-IDEA-configuration).
