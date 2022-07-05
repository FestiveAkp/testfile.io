# testfile.io

A simple API for retrieving test files of various sizes and filetypes for testing programs, networks, etc. easily from the command line, written in Clojure.

## Usage

Just need a simple text file? Make a `GET` request to `/text`:

    $ curl testfile.io/text > test.txt

    I met a traveller from an antique land
    Who said: "Two vast and trunkless legs of stone
    ...

Want a bigger file? Choose one between `sm`, `md`, `lg`, and `xl`:

    $ curl testfile.io/text/md

    I: LAYING PLANS

    Sun Tzu said:

    The art of war is of vital importance to the state. It is a matter of life and death...

Need a file with an exact size? Request a number with units to get a text file with random characters:

    $ curl testfile.io/2KB

    8UNdyRyEloZUocHzpeV2waiHlfM6FDsC3NsB5sOMsQSc5ibHfV129iLYpFKyT4qB8DgseoD2YgdLExF
    ...

Want a file with non-Latin characters? Retrieve the languages test file:

    $ curl testfile.io/languages

    Sanskrit: काचं शक्नोम्यत्तुम् । नोपहिनस्ति माम् ॥
    Sanskrit (standard transcription): kācaṃ śaknomyattum; nopahinasti mām.
    Inuktitut: ᐊᓕᒍᖅ ᓂᕆᔭᕌᖓᒃᑯ ᓱᕋᙱᑦᑐᓐᓇᖅᑐᖓ
    ...

You can also test for various UTF-8 symbols using the UTF-8 test file at [testfile.io/utf-8](http://testfile.io/utf-8).

Want a JSON file? Generate a fake list of users:

    $ curl testfile.io/json

    [
        {
            "address": "Apt. 988 6571 Inell Mission, Ullrichshire, MI 73112-9972",
            "email": "alida.murray@example.com",
            "phone": "744-022-6717",
            "birthday": "1961-04-30T11:16:11Z",
            "firstName": "Ron",
            "favoriteColor": "orange",
            "university": "O'Connell University",
            "lastName": "Konopelski",
            "favoriteGenre": "Reggae",
            "jobTitle": "Customer Executive"
        },
        ...
    }

There are various other endpoints which can be explored below.

## API Endpoints

| Route | Description | Parameters | Source | &nbsp;&nbsp;File&nbsp;Size&nbsp;&nbsp; | Link |
| --- | --- | --- | --- | --- | --- |
| `/text/sm` | Gets a line-oriented, human readable text file of small size |  | Ozymandias | ~650 B | [Link](http://testfile.io/text/sm) |
| `/text/md` | Gets a line-oriented, human readable text file of medium size |  | Chapter 1 of The Art of War | ~12 KB | [Link](http://testfile.io/text/md) |
| `/text/lg` | Gets a line-oriented, human readable text file of large size |  | The Odyssey | ~600 KB | [Link](http://testfile.io/text/lg) |
| `/text/xl` | Gets a line-oriented, human readable text file of extra large size |  | Webster's Unabridged Dictionary | ~27 MB | [Link](http://testfile.io/text/xl) |
| `/text` | Alias for `/text/sm` |  |  |  | [Link](http://testfile.io/text) |
| `/{size}` | Generates a text file of an exact size | `size`, a number with units, like 10KB or 1.2MB |  |  | [Link](http://testfile.io/2.1KB) |
| `/languages` | Gets a text file containing several different languages, including non-Latin alphabets |  | Excerpt from The Kermit Project's [UTF-8 Sampler](https://kermitproject.org/utf8.html) | ~13 KB | [Link](http://testfile.io/languages) |
| `/utf-8` | Gets a text file containing several UTF-8 symbols and glyphs |  | Markus Kuhn's UTF-8 encoded sample plain-text file | ~13 KB | [Link](http://testfile.io/utf-8) |
| `/json?records={records}` | Generates a sample JSON document containing a list of N user objects | `records`, the # of user objects to generate, default: 3 |  |  | [Link](http://testfile.io/json) |
| `/pi?digits={digits}` | Gets N digits of pi up to 100,000 | `digits`, the # of digits after the decimal place to retrieve, default: 2 |  |  | [Link](http://testfile.io/pi?digits=25) |
| `/favicon` | Gets a 16x16 favicon image in ICO format |  |  | ~200 B | [Link](http://testfile.io/favicon) |
| `/beemovie` | Gets the entire Bee Movie script |  | Bee Movie | ~88 KB | [Link](http://testfile.io/beemovie) |

## Installation

Clone this repository. This is a standard Leiningen project, so you can choose to launch a REPL or just run the dev server on port 8080:

    $ lein run

To build this project, generate and run the uberjar:

    $ lein uberjar
    $ java -jar target/io.testfile-standalone.jar

## License

Copyright © 2022 testfile.io

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
