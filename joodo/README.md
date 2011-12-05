# joodo

## Installation

1. Install [Leiningen](https://github.com/technomancy/leiningen) 1.5 or later.
2. Install the `lein-joodo` plugin

        lein plugin install joodo/lein-joodo 0.6.0-SNAPSHOT

3. Make sure `~/.lein/bin` is in your path.

        export PATH=$PATH:~/.lein/bin

4. Try it out!

        joodo help

If all goes well you should see a helpful message printed in your console.

## Usage

### Creating a new Project

1. Joodo will create a boiler plate project structure for you.

        joodo new my_new_project

2. Download/Install all the dependencies.

        cd my_new_project
        lein deps

3. Start the development server

        joodo server

## License

Copyright (C) 2011 Micah Martin All Rights Reserved.

Distributed under the The MIT License.