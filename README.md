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

## Play in browser

The browser version lives in `src/main/resources/web/` and starts from
`src/main/resources/web/index.html`. Because the page loads JavaScript modules
and supporting assets, open it through a local static file server instead of
opening the file directly from disk.

From the project root, one simple option is Python's built-in server:

```sh
python3 -m http.server 8000 --directory src/main/resources/web
```

Then open <http://localhost:8000/> in your browser. If your system provides the
`python` command instead of `python3`, run:

```sh
python -m http.server 8000 --directory src/main/resources/web
```

To create a distributable browser asset directory, run:

```sh
./gradlew webDist
```

This copies everything from `src/main/resources/web/` into `build/web/`,
including `index.html`, `main.js`, and the supporting browser files.

## Deployment

For GitHub Pages, publish the generated `build/web/` directory after running
`./gradlew webDist`. If you prefer GitHub Pages' checked-in folder workflow,
copy the contents of `build/web/` into a `docs/` directory and configure Pages
to publish from `docs/`.

## Author

Written by Tyler Evans.
