# Feedzai OpenML API
This module contains the core concepts of the OpenML API.


## Developing

To implement an OpenML provider there are two options:

* Implement the **MachineLearningProvider** interface to load trained ML models to the Feedzai platform. It uses the __MachineLearningModelLoader__ interface to load a model.

* Implement the **TrainingMachineLearningProvider** extension of the MachineLearningProvider interface to train new ML models within the Feedzai platform. It uses the __MachineLearningModelTrainer__ interface to train a model.

The other key concepts are the following:
* __MachineLearningModel__: allows to implement two types of models:
  * __ClassificationMLModel__
  * __RegressionMLModel__
  
* __MLAlgorithmDescriptor__: describes a Machine Learning algorithm and its configuration parameters

See the [openml-example project](https://github.com/feedzai/feedzai-openml/tree/master/openml-example) for a trivial implementation of these concepts.
