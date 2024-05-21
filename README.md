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

## Sequence Diagram for Chess Project:

url for diagram: 
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMTxvD8QJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfKYA3uH9nmDW1dkmA4sJBQoN13HpSIPL9PieG0lhomAAXOTclW1IcYAQBCkDQWF4MQ1F0VibFB11XtkzJCkzThMjCyZZM3Q5cheX5f02MMAAxcIagAWWrHsi206cB2VYS1MZGBYQgHQACsUHAeSHMUmyWVTY1MkzbNcxrQNOwbc90E0l0S0KMtvV9QzPxQOsov-dsYxHCcpxnfjdyk8SVzXFyQtSZy3PclxsSYkFBJKK4BiM79Hl-DKL1avo6OKO87PgdCMGfV932a1KutPDr0C6g4QLArxfH8AIvBQNspN8ZhkPSTJMAfPJ7J3ecKmkfT6i5ZoWgI1QiO6P8L1vBiziBXc7vHOrMIc8pRPsDbYVetAfKEvytICmByTAcq-qmgHrJB11S107k+QFSLs2ipJTIs1HUlhuKt0Hcpyuc1yPK8rFcoTAklNBjgUG4YKGyhtH-1ivsEfKE6+TOmBMcskcYAAdRYSsuWx9GYAAXhgAByf7pdxtmGvBGA1Q1CnDH6urSkwd7+vo7X6PvQawGGt9LzUkSEPJDgYA2vpTDm8DFsCSEbZ5aEYAAcXzVkttQ3bjY+w7ygqT2uTwlp7HzW7oYetktbF-8dYKx6CZE6FvdGVQmdHC9AcnSmZGpg1wch-7WdshLEf0lH-p5sy+YbBXbKVlUYCJ7QXKqsn86nKn-JLilM5Qd3YlhCuFXZvTkcFAAqeusaj0Zm8n1vhOH4nu-AVRe4TfLnvnUewGH1QSoQdcU-x3r5xYvol7UcZKn6e+PWkR+AEYnwAZgAFieFDMhqTuCsL4OgECgGbEAw8Ewvj3y9PmGBexGg9UOkbHIJsYAvjNrfe+qhH4VGfvmV+H9v5-ymAA00LUYFPDARAqB7E+iwPzPA0YiDkFmE4KYBakEAjYB8FAbA3B4BBUMMPFI200LoKDo1SotQGgXXvjHZmF4SJ32YdAxhvE477znMOaGswcHqIYbRZOB9pHKzTCaYesIvQoGSMPGSGJd4a37nDFMYMKRl2hhPeGVdOQ10FHXXm2MV7wzXoTBsm9SbeXVsDOKKlj75lhHA-MPj4rsn8TPMGRiTIN2ycvJMq8DoF2EqrTUClEzF3cZYzItj7FJJSaMNJOlPQVirC-aQswyldPVAgWYjS0oqzsfAVILJrGYiyvksQhSwnFPBBvTuJNPIxIqTo4E5Q4AiIcWoNcpi5xX2DnuKYHSSG-xQccfqe0MFYNUSc8on9f5aI4aBJ2PDLB01EskGAAApCA4kvb5kCLQkAzYA5SIathKolII6KOCNDd8gjgDvKgHACAokoD9KIdIc5fVGIpz0co9ABjnjgORai9Fj8nh3K0brOZbd3J-LQNY354ltloicbEypA93Gl0ZuXUJ6SyxIwMonC8C9G7dhmSWcJ7dImLK3uTCprj4keMSaMZJWLmlT2FSjDp4qpkCoOSU8oCz5Bd2iYq3yXK3HlD8FoTI1jTXAC1X4mAlIqjSAUNze+elBQAB5EVkrRdAfI9DDBSw7vIFwZFBbC3CKLPVUsOmGr1p9TlazLh7IEhCso2jThXNNt0PoLK0AoBtvfe2zyuEQSWl4JFA10ywGANgQRhB4iJHEf7K50jsKc3DudVoxhtF4oPtrWl25jWGm4HgcenLlXFlTNOqAalZ1SpaTIU6otgk3DnUXbli7G3jJTa6vt3Ngn313VKg9eBIYuoyRurmW68k5TXamoSBsrUZtzbrHNH7UGXONoWy8iBG1lvtnNIAA
