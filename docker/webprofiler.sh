#!/bin/bash

if [ $# == 0 ]; then
    CMDFILE=w3c.tst
else
    CMDFILE=$1
fi

CMDFILE=$1
JARFILE=web-profiler-1.0-jar-with-dependencies.jar

cd /headless/webprofiler
java -jar $JARFILE -driver /usr/bin/chromedriver -input $1
