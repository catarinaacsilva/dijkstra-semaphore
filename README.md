# Dijstra Semaphore

An implementation in Java of the Dijstra semaphore.

Writed my own data type, implementing the same functionality, but using the basic thread blocking primitives described in the data type **LockSupport** of the package **java.util.concurrent**.

**locks** and other reference data types described in the package **java.util.concurrent**.

## Requirements

- Java 8
- Maven

## Run the example

Run the following command

```bash
mvn clean compile exec:java
```

After that, the program will follow the next steps:

1. The program asks for a setence
2. Write any setence (e.g. How much wood would a woodchuck chuck if a woodchuck could chuck wood?)
3. The producer will split the setence into tokens and insert them into a queue
4. At the same time de consumer will consume de tokens and print them
5. The tokens will be written in the terminal with the same order

## Run the unit test

Run the following command

```bash
mvn clean compile test
```

## Compile javadoc

Run the following command

```bash
mvn javadoc:javadoc
firefox target/site/apidocs/index.html
```

## Authors

- **Catarina Silva** - [catarinaacsilva](https://github.com/catarinaacsilva)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
