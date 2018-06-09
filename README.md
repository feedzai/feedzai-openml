# feedzai-openml

Feedzai's OpenML libraries for abstracting the core concepts of machine learning in a pluggable and extensible way.

This project assembles the APIs that one must implement to provide new machine learning algorithms in Feedzai platform. 


## Getting Started

The key concept is that of a `MachineLearningProvider`, i.e., implementations of that interface are capable of yielding objects that can load machine learning models (already trained) in a way that Feedzai platform can use them.

An extension of that interface resides in `TrainingMachineLearningProvider`, which adds the ability to train new machine learning algorithms within Feedzai platform.

These are the entry point for developers:
an implementation of either results in an OpenML provider that can be used for, respectively, loading and training external algorithms to those already provided by Feedzai.

The `openml-example` project shows a trivial implementation of those concepts.
