#!/bin/sh
javac -cp lib/core.jar:lib/net.jar:lib/video.jar:lib/minim.jar:lib/jsminim.jar:lib/tritonus_aos.jar:lib/tritonus_share.jar:lib/jl1.0.jar:lib/mp3spi1.9.4.jar *.java
java -Xprof -cp '.:lib/core.jar:lib/net.jar:lib/video.jar:lib/minim.jar:lib/jsminim.jar:lib/tritonus_aos.jar:lib/tritonus_share.jar:lib/jl1.0.jar:lib/mp3spi1.9.4.jar' MyServer
