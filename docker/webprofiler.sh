#!/bin/bash

if [ $# == 0 ]; then
    CMDFILE=demo.tst
else
    CMDFILE=$1
fi

JARFILE=web-profiler-1.0-jar-with-dependencies.jar

export DISPLAY=:99
/etc/init.d/xvfb start
sleep 1

cd /home/headless
java -jar $JARFILE -driver /usr/bin/chromedriver -input $CMDFILE

exit_value=$?
/etc/init.d/xvfb stop
exit $exit_value

