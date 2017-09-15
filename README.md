# Java-parser
This project parser the java source code into UML language through
yUML.

## Tools and Libraries
### Tools used to create JavaParser:
1. Eclipse IDE: (eclipse version 1.8.0_73) Use the Eclipse IDE to edit the source code.
2. Maven in Eclipse: Add the following to maven configuration in my dependency:
  ```
  <dependency>
    <groupId>com.github.javaparser</groupId>
    <artifactId>javaparser-core</artifactId>
    <version>2.3.0</version>
  </deoebdency>
  ```
3. Parser : Use the Javaparser from: https://github.com/javaparser/javaparser to parse
the java code
4. yUML: Use the yUML(http://yuml.me/diagram/scruffy/class/draw) to draw the diagram

Library: Used the library javaparser-core-2.3.0.jar to parse the java source files.

## Steps to run the Javaparser
1. Download the Javaparser and the five test cases to the local
directory.
2. Use shell bash to go to the directory you save the Javaparser and the five test
cases.
3. Execute the below command:
    ```
    $java - jar umlparser.jar <classpath> <output file name>. Below is an example:
    ```
    ```
    XiaoyuLiang-MacBook-Pro:Desktop kaichen$ java -jar umlparser.jar uml-parsertest-
    1 test1.png
    ```

