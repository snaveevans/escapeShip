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

### Cloudflare Pages

The easiest way to make the game public is Cloudflare Pages' Git integration.
This browser build is already plain static HTML and JavaScript, so Cloudflare
does not need to run Gradle or install Java to deploy it.

In Cloudflare Pages, connect this repository and use these build settings:

* Framework preset: **None**
* Build command: leave blank, or use `exit 0`
* Build output directory: `src/main/resources/web`

The same output directory is recorded in `wrangler.toml` so Wrangler-based Pages
deployments use the browser assets directly. After the first deploy, every push
to the production branch gets published automatically, and pull requests get
preview deployments.

For a one-off command-line deploy, install or invoke Wrangler and upload the web
asset directory directly:

```sh
npx wrangler pages deploy src/main/resources/web --project-name escape-ship
```

If you prefer to deploy a generated artifact directory instead, run
`./gradlew webDist` and publish the generated `build/web/` directory. That path
is also suitable for hosts such as GitHub Pages. If you prefer GitHub Pages'
checked-in folder workflow, copy the contents of `build/web/` into a `docs/`
directory and configure Pages to publish from `docs/`.

## Author

Written by Tyler Evans.
