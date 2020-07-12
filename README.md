# stf
stf is a command line tool for working with **S**tring **T**able **F**iles found in the game Star Wars Galaxies.

A stf file contains localizations for a specific language.

## Installation
The tool requires Java SE 13 or newer.

You can download the tool under Releases. 

### Globally available in shells
```shell script
$ sudo ln -s /path/to/stf.sh /usr/bin/stf
```

## Examples
Here are some examples of use cases for the tool.

### Searching
Search "hello" in a file named greeting.stf
```shell script
$ stf -i greeting.stf -p | grep "hello"
@greeting.stf:hello|Hello, World!
```

Search "hello" in all .stf files in the current directory as well as any subdirectory
```shell script
$ stf -i **.stf -p | grep "hello"
@greeting.stf:hello|Hello, World!
@altgreet.stf:hello|Greetings.
```

### Counting amount of entries in a stf file
We want to know how many files are in greeting.stf
```shell script
$ stf -i greeting.stf -p | wc -l
4
```