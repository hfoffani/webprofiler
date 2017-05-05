#!/bin/bash

FS=`pwd`/../scripts
docker run -it -p 5901:5901 -p 6901:6901 --user 0 -v $FS:/data herchu/webprofiler bash
