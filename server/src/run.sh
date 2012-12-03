#!/bin/sh
javac -cp json-simple-1.1.1.jar:core.jar:net.jar *.java
java -cp '.:json-simple-1.1.1.jar:core.jar:net.jar' Server
