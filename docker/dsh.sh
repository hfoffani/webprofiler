#!/bin/bash

FS=`pwd`/../scripts
docker run -it -v $FS:/data herchu/webprofiler bash
