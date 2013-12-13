# joodo

## Installation

### Leiningen 1

Don't use Leiningen 1.  Use Leiningen 2.

### Leiningen 2

1. Install [Leiningen](https://github.com/technomancy/leiningen) 2.0 or later.
2. Install the `lein-joodo` plugin by updating your `~/.lein/profiles.clj` file.

```clojure
{
  :user {:plugins [[joodo/lein-joodo "1.2.0"]]}
}
```

3. Try it out!

```bash
$ lein joodo help
```

If all goes well you should see a helpful message printed in your console.

## Usage

### Creating a new Project

1. Joodo will create a boiler plate project structure for you.

```bash
$ [lein] joodo new my_new_project
```

2. Download/Install all the dependencies.

```bash
$ cd my_new_project
$ lein deps
```

3. Start the development server with the following if you're using a joodo version below 2.0.0:

```bash
$ lein joodo server
```

When you are using a version of 2.0.0 or higher use the following command:

```bash
$ lein ring server
```

## License

Copyright (c) 2011-2013 Micah Martin All Rights Reserved.

Distributed under The MIT License.
