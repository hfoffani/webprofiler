cd /headless/webprofiler
JARFILE=web-profiler-1.0-jar-with-dependencies.jar
java -jar $JARFILE -driver /usr/bin/chromedriver -input - 
