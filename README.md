# Escape Ship

Escape Ship is a small Java game. The project is built with Gradle using the
Gradle wrapper checked into this repository.

## Requirements

* Java 25 or another JDK supported by the configured Gradle wrapper
* Gradle wrapper distribution: Gradle 9.5.1

The project currently compiles Java source and target compatibility as Java 8.

## Build and run

Clone or download the repository, then use the Gradle wrapper from the project
root:

```sh
./gradlew run
```

On Windows, use:

```bat
gradlew.bat run
```

To build the project and produce the game jar, run:

```sh
./gradlew build
```

The jar artifact is named `escape-ship` and is written to `build/libs/`, for
example `build/libs/escape-ship-0.1.0.jar`.

## Author

Written by Tyler Evans.
