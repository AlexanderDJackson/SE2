## TOML Library API
This TOML File Library API is made available to users for the purpose of importing it into their Java enviroment.

The primary purpose of this project is to make it possible for Java users to easily work with TOML files, extract data from within them, and add new data.

A good show of this implementation functioning correctly will consist of a variety of tests using Apache Maven. These tests prove that the code and functions work properly and do what they are supposed to. Tests for edge cases that might break the library are also included.

## Motivation
This project is to create a library that supports the use of TOML v1.0.0 (https://toml.io/en/) files in Java 1.8. Allowing users to seamlessly retrieve data from the TOML file with 'get' statements. Accessing data inside TOML files is often arduous, this library is made to be a solution to this. Furthermore, users will be able to simply add new data to their TOML file with 'set' statements. This will be similar to the way users can interact with JSON files. 

## Code style
If you're using any code style like xo, standard etc. That will help others while contributing to your project. Ex. -

[![js-standard-style](https://img.shields.io/badge/code%20style-standard-brightgreen.svg?style=flat)](https://github.com/feross/standard)
 
## Screenshots
Include logo/demo screenshot etc.

## Tech/framework used
-Java 1.8
-Apache Maven 3.8.4

## Features
The library consists of TOML Parser that:

-Parses key/value pairs from a TOML object and stores them in HashMaps

-Output TOML data in various formats and data types defined in the TOML specifications (https://toml.io/en/v1.0.0)(DateTime, Array, String, Integer, etc.)

-Update the TOML file by adding objects and manipulating the HashMap key/value pairs as needed

-Output the TOML object as a file/stream/string

## Code Example
Show what the library does as concisely as possible, developers should be able to figure out **how** your project solves their problem by looking at the code example. Make sure the API you are showing off is obvious, and that your code is short and concise.

## Installation
Import SE2Toml.java library at the top of your Java file that you wish to use the library functionalities in.

## Tests
Tests are stored in the src > test > java > TomlTest.java file of the Maven project folder. All tests adhere to the specifications of JUnit and are run through Apache Maven's mvn test command.
For example: when in the SE2 folder in the terminal, run **mvn test**
Also, navigate to the TomlTest.java folder to see the things that are being test and how they are relevant to the project.

## How to use?
If people like your project they’ll want to learn how they can use it. To do so include step by step guide to use your project.

## Contribute
If you want to contribute to the TOML library or think of any features to add, pull requests are always welcome!

## Credits
Give proper credits. This could be a link to any repo which inspired you to build this project, any blogposts or links to people who contrbuted in this project. 

#### Anything else that seems useful

## License
A short snippet describing the license (MIT, Apache etc)
