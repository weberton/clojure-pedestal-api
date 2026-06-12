# clojure-pedestal-api

A real-world REST API built with Clojure, Pedestal, and Stuart Sierra's Component library.

This project follows a tutorial series focused on building a practical Clojure backend from scratch.

## Technologies

- [Clojure](https://clojure.org/) 1.12
- [Pedestal](http://pedestal.io/) — HTTP server and routing
- [Component](https://github.com/stuartsierra/component) — lifecycle management for stateful parts of the app
- [Aero](https://github.com/juxt/aero) — configuration from EDN files
- [Leiningen](https://leiningen.org/) — build tool

## Requirements

- Java 11 or newer
- [Leiningen](https://leiningen.org/) installed

## Running the server

```bash
lein run
```

By default the server starts on port `8080`. To use a different port, set the environment variable:

```bash
REAL_WORLD_CLOJURE_API_SERVER_PORT=9000 lein run
```

## Running the tests

```bash
lein eftest
```

## Project structure

```
src/       application source code
test/      tests
resources/ configuration files
dev/       development utilities (REPL helpers)
```

## License

Copyright © 2026

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
https://www.eclipse.org/legal/epl-2.0.
