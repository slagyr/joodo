# joodo

## Installation

### Leiningen 1

Don't use Leiningen 1.  Use Leiningen 2.

### Leiningen 2

1. Install [Leiningen](https://github.com/technomancy/leiningen) 2.0 or later.
2. Install the `lein-joodo` plugin by updating your ~/.lein/profiles.clj file.

        {
         :user {:plugins [[joodo/lein-joodo "1.0.0"]]}
         }

3. Try it out!

        lein joodo help



If all goes well you should see a helpful message printed in your console.

## Usage

### Creating a new Project

1. Joodo will create a boiler plate project structure for you.

        [lein] joodo new my_new_project

2. Download/Install all the dependencies.

        cd my_new_project
        lein deps

3. Start the development server

        lein joodo server

## License

Copyright (c) 2011-2012 Micah Martin All Rights Reserved.

Distributed under the The MIT License.
