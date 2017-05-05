#!/bin/bash

FS=`pwd`/../scripts
# docker run -v $FS:/data herchu/webprofiler /headless/webprofiler/webprofiler.sh /data/w3c.tst
docker run herchu/webprofiler /headless/webprofiler/webprofiler.sh
