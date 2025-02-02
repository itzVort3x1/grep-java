# Grep Implementation in Java

## Overview
This project is a Java implementation of the `grep` command, designed to match patterns in input strings using regular expressions. It follows the CodeCrafters platform specifications and utilizes Java's built-in capabilities for pattern matching.

## Features
- Supports pattern matching with:
   - `.` (wildcard for any character)
   - `+` (one or more occurrences)
   - `?` (zero or one occurrence)
   - Character classes (e.g., `[abc]`, `[^abc]`)
   - Escape sequences (`\d`, `\w`)
   - Anchors (`^` for start, `$` for end)
   - Grouping and alternation (e.g., `(abc|def)`)
- Recursive approach for pattern evaluation
- Java-based CLI interface
- Compatible with CodeCrafters testing framework

## Directory Structure
```sh
.
├── README.md
├── src/
│   └── main/
│       └── java/
│           ├── Character.java  # Pattern matching logic
│           └── Main.java       # CLI entry point
├── pom.xml                     # Maven configuration
├── codecrafters.yml            # CodeCrafters configuration
└── your_program.sh             # Local execution script
```

## Installation & Usage
### Prerequisites
- Java 11 or later
- Maven

### Compile the Project
To build the project locally, run:
```sh
./your_program.sh
```

### Run the Program
To execute the program with a pattern:
```sh
./your_program.sh -E "pattern"
```
It reads input from `stdin` and matches it against the given pattern.

### Example
```sh
$ echo "hello world" | ./your_program.sh -E "hello"
```
If the input matches the pattern, the program exits with code `0`; otherwise, it exits with `1`.

## Development
### Running Locally
The `your_program.sh` script allows you to run the program locally with the same setup as CodeCrafters.

### Debugging
Print debug logs using:
```java
System.err.println("Debug message");
```
These will appear during test execution.

### Testing
Run test cases manually by executing the program with different patterns and inputs.

## Deployment
This project is built using Maven. The `pom.xml` ensures all dependencies are packaged correctly.

## Contributions
Feel free to fork and modify the project. Pull requests are welcome!

## License
This project is free to use and modify. 

