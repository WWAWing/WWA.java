#!/bin/sh

/opt/java/jdk-10.0.2/bin/javac WWA.java
/opt/java/jdk-10.0.2/bin/appletviewer -J-Djava.security.policy=./policy wwamap.html