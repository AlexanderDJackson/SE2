## TOML Library API
This TOML File Library API is made available to users for the purpose of importing it into their Java enviroment.

The primary purpose of this project is to make it possible for Java users to easily work with TOML files, extract data from within them, and add new data.

A good show of this implementation functioning correctly will consist of a variety of tests using Apache Maven. These tests prove that the code and functions work properly and do what they are supposed to. Tests for edge cases that might break the library are also included.

## Motivation
This project is to create a library that supports the use of TOML v1.0.0 (https://toml.io/en/) files in Java 1.8. Allowing users to seamlessly retrieve data from the TOML file with 'get' statements. Accessing data inside TOML files is often arduous, this library is made to be a solution to this. Furthermore, users will be able to simply add new data to their TOML file with 'set' statements. This will be similar to the way users can interact with JSON files. 

 ## Tech/Framework used
* Java 1.8
* Apache Maven 3.8.4

## Features
The library consists of TOML Parser that:

Parses key/value pairs from a TOML object and stores them in HashMaps

Output TOML data in various formats and data types defined in the TOML specifications (https://toml.io/en/v1.0.0):
 * Key/Value Pairs
 * Arrays
 * Tables
 * Inline tables
 * Arrays of tables
 * Integers & Floats
 * Booleans
 * Dates & Times, with optional offsets

* Update the TOML file by adding objects and manipulating the HashMap key/value pairs as needed

* Output the TOML object as a file/stream/string

## Code Example
**Reference the java code (Example.java) and TOML file (example.toml) in the main directory for assistance when importing and using the Toml library.**
```
import Toml.Toml;

example code in here
```

## Installation
Import Toml.Toml library at the top of your Java file (located in the same directory as Toml.java) that you wish to use the library functionalities in.

## Tests
Tests are stored in the src > test > java > TomlTest.java file of the Maven project folder. All tests adhere to the specifications of JUnit and are run through Apache Maven's mvn test command.
For example: when in the SE2 folder in the terminal, run **mvn test**
Also, navigate to the TomlTest.java folder to see the things that are being test and how they are relevant to the project.

## How to use?
Using the TOML Library the user first imports the library. Next, they can either pass it a TOML string or a TOML file as a parameter when calling toml.parseToml(). They can extract TOML data by using get functionalities and add more TOML data to the file with set functionalities.

## Contributions
If you want to contribute to the TOML library or think of any features to add, pull requests are always welcome!

## License
Apache Maven 3.8.4

## HAPPY CODING AND TOMLing!!
