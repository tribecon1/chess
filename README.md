# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

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
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

##Sequence Diagram for Chess Project:
url: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMTxvD8QJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfKYA3uH9nmDW1dkmA4sJBQoN13HpSIPL9PieG0lhomAAXOTclW1IcYAQBCkDQWF4MQ1F0VibFB11XtkzJCkzThMjCyZZM3Q5cheX5f02MMAAxcIagAWWrHsi206cB2VYS1MZGBYQgHQACsUHAeSHMUmyWVTY1MkzbNcxrQNOwbc90E0l0S0KMtvV9QzPxQOsov-dsYxHCcpxnfjdyk8SVzXFyQtSZy3PclxsSYkFBJKK4BiM79Hl-DKL1avo6OKO87PgdCMGfV932a1KutPDr0C6g4QLArxfH8AIvBQNspN8ZhkPSTJMAfPJ7J3ecKmkfT6i5ZoWgI1QiO6P8L1vBiziBXc7vHOrMIc8pRPsDbYVetAfKEvytICowUG4YKGz+qaAeskHXVLXSTr5M6YFMizIuzGAAHUWErLlMdHC8YAAXhgAByf7ybhuKt0HVV1QQFz-pgZJ9HFXxOEBycE3y56ykwd7+vo0oHvvQawGGt9LzUkSEPJDgYA2vpTDm8DFsCSFFZ5aEYAAcXzVkttQ3aJY+w7ygqPWuTwlp7HzW6YbFvm52HJ2hYOnnhOQWIDdGVRoezaLYYUxMlNB8kwHKwOiZimm+0Rzl9IFQng7RszLJypMFWFz6YHK5zXI8rysVyhMCXDg1I79lAddiWFYoThLdO5PkU4AKnTjH7dGePbIa8F9fzQuqpL1RubyxiCvnOuwBr1QSoQddp7p3r5xYvoe7UcZKn6LePWkHeAEYnwAZgAFieFDMjUu4Vi+HQEFAZtb8PCYvi3r183fvZGh6w7xY5EljAF80sN5b1UDvCoe98wH2PmfS+Uxr6mhau-J4j9n6v3Yn0D++Yv6jB-n-MwnBTALUggEbAPgoDYG4PAIKhga4pG2mhIB5tGqVFqA0C6W9HZB3-CRTeeC344N4s7Ke-M3Z8IvLMcBQjsG0UFivAeKpDT0JrrCL0KBkg1xkhiCe5cZCVxTDASO0d-qN1ss3JObdBQs3RpnBsfcc7KOEgXbQLlR7eTLoYMO-kq4UnUZ-fMFic5WL0jYkxciTIZ0ib3bOCMXEMw1N44GcVArpkyJo7R+ZYRBNGCEhGYSkqVkFPvaQsw1QagqYzWYeTDBRhjFvJxCTPbghriPYuXjQ4u2BOUOAaj8yL2XvzVeFs9xTDKfAi+-9jj9T2sA0BAjJnlBPhfURxDQLq3IZYcGolkgwAAFIQHEkPUUAQMEgGbKbVhDVsJVEpLbHhwQYbvhocAHZUA4AQFElAWpsDpAzL6uI12qd-wyOeE-D5Xyfk7yeMs0RHttxe3KO5Y5aB1FHPEjolAaI9EpN8fDYxpiobmOafFdk1iDKguJvYwmZLRnIvzg2Dpnkum+QJWkkxAScllIKeSssrcqVlK7pZJp8SSyJNOSgFlJd9E+Irn44xfgtCZHUW8qF3zoCzDcfIP5owD58p0uUSkVRpAKFRlvPSgoAA86rKDQugPkLBhgyY6uAC4MiOM8bhAJsKsmZT6W5yEqLbpwLemKJGUGxqztTjzKlt0PomK0AoEVlvFWGzSEQSWl4d5A10ywGANgGhhB4iJCYSbeZbDsLIxtudVoxgxFPVdhGucbDB4gG4HgBu+KFWEtTJ2qAalu3iqNTIU6BNaU3B7YYxV-b83qMNYnMdKMJ0xKaaHXtnKO35ujousJNbUa0qzkYhl4J8U9MuC2gStyBb0UAY+EBI1LyIHzSmlWc0gA
