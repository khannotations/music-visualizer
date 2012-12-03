#!/bin/sh
ln -s ../Contents/Resources/Java/modes/java/libraries/net/library/net.jar net.jar
ln -s ../core/library/core.jar core.jar
javac -cp json-simple-1.1.1.jar:core.jar:net.jar *.java
java -cp '.:json-simple-1.1.1.jar:core.jar:net.jar' Server
