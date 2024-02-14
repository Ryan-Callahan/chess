# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Sequence Diagram
[Server Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBkInIZSqjxDKaQDiwALYphykCi6rJTGABF+wAIIhDXYwBMrAI2BcUMRy8yZ2UCABXbBgAYjRgKgBPVg5uXn4kQTCAdwALJDAxRFRSAFoAPnIlSgAuGABtAAUAeTIAFQBdGAB6QI8oAB00AG92ygjdABoYXAcU6EcRlG1gJAQAX0x9ShhC2M4ePgFFEUNyqDieSgAKfqhBlBGxrgmoKZgZuYQASkw2TYSd-VE1osswDY7EYuOV2CgwABVDpnDqXN4AoH2Lh-LwucpkACiMkxcHqMHOlxgADMAtoCR1MIjbMi1usPvFtkldgYUOU0IEEAh3kcvsyfoZUdTgQ5yiBDvwUNDToSdFdRu5bpMEVYaSDUd5ygBJAByWJY+NlwwV40m01m8xguvqNQptAZW0SggFnnWwuRYolWWsgTAaVhAzlKsBaocGvRVr1mINdoucpGwF9aXqEAA1uhIzaYIm-VTVSKUXl6bymc6lKJyjnk2n0DzPqWWb8i0UVlBK0mU+m0MsSrBm-BkOgwOUAEwABjH3R6Vc76CW6Ecvn8QRCoWg0nBMDkClSGSymByQ9RrfK1TqTVaHgczKnRvlNzujiWrdR26Qjf2MAQ8nfAbjxofZVMDfD9XQKd0QTBCFpSgP94TzEMCz+TVyGxXFDThOUSTJWMEKRdVmxAl1yjvYCf1Av4INFGBwTAKpFUfE5APuYN8LDQoUKxHE8RNJV7mwiByXOPDQ0LQoiPLT9mMXCS9jAqjQRgcUUElH0-TgoMRKQjiI11fUMMDY0ZxrNBM1tKstNpftZNZds-VnbsbKbdYT2zDsTJ7ahVn7Q8MFHCcp2Mrt5zQRc-ACYIwn8FAM23IJmFCdJMmyQd8hbXtT3MND6kxZoWivLgbwidyu08mg+wKJzP2-dh4rIhQXUo-MPTcv1oCQAAvMQFJ0jE0J4oKM1JQTWrSSyQUI8jiNG+r3xdQoFPKGq6p6goUKynEctjS4ExKoacIsl9m184cYHHMdMAXJcItXQ5HC3eIYC0XQUUSvcUtyZgXIyyoNExC8WnYOVukG7sjoKZ69EkowlviSHjEhl1C3A5rING9quvGhxetQ7j8VBgTyQsnrCkR6HFIssm5ORxavzhuVjAU5CIy49CYAAKkJmi5SxsSIblJGwQZzAqdZPnii8ttuZesrvMKE7-MnXogZekKwuXSLQmwQIoGwBBPDgL1PEh3dkoPVKvvSyXT1qBo8pVlAQb2tApwdnU5WfXs-lF0RFOUyVIZON3NJ9+wmsQlqqwx7rUextaWf6-Hna54nY64ZtQ5hmbM5ptPSlowPg90VjROZvq8ellB3d0LmHd5ul+d0QWYA5LkRYF8nw7Yv2jcLuVq5QEuCxxvTo3xIvPGtW066ZjOO+poXdC1cx26b8nmxPB3l9lqBjot0pztduVl7V66VzCKIUC5CAUhgAApCB3yerDQhcBBQFTc3PuPH7qkhAGHZO3siZKcJ04AQG-FAEYW9zCe0lqTeeYtSgACtH5oEDmAiB0BoHH3MG8HOC087lSQMSKIfcl54N5r1VmPEYG1x5iTRuUMF51wISjCOkFUHvgwRbcBkCh5WSKJqUeMZMGQLMpXHee9PoHwnJdUKZ9Nb+GADERAKlYDAGwHrQgTpTJvTNidH+1tKgbX+rlVo3gpFMObiAfWeBV7MLFoQjh1FbHqKofHUopitooHKlEP0752A7wbgOGRZ05FXSAA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
