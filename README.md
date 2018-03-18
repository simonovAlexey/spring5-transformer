# Spring 5 Functional Web Framework Sample

This repository contains a sample application that uses the functional web framework introduced in Spring 5.
It consists of the following types:

| Class                   | Description                                   |
| ----------------------- | --------------------------------------------- |
| `Transformer`           | POJO representing a person                    |
| `MapBasedTransformerRepository` | In memory implementation of `TransformerRepository`    |
| `TransformerReactiveHandler`         | Web handler that exposes a `TransformerRepository` |
| `TransformerReactiveController`         | Reactive REST Controller |
| `TransformerRestController`         | "Classic" REST Controller |
| `Server`                | Contains a `main` method to start the server  |
| `TransformerClient`                | Contains a `main` method to start the client (check starting port!)  |

### Running the Server
 - Build using maven
 - Run the `com.simonov.transformer.Application` class
 
