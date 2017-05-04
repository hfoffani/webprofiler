# webprofiler

A small program to profile web sites. Includes a command language to navigate sites.

It uses WebDriver to drive the browser and get the performance logs which are later displayed as json format.
There is a docker image to ease the usage.

### Usage

- download the image
- run

    docker run -v /tmp:/data herchu/webprofiler /headless/webprofiler/webprofiler.sh /data/w3c.tst


Because of the complexity of the base image `consol/ubuntu-xfce-vnc` I am using it displays a lot of messages at
startup. I will rebuild it using thinner layers of dependencies.


### Command language

Example:


    GO https://www.w3.org/
    FINDTEXT Web Security
    CLICK
    FINDNAME q
    TYPE rfc 822\n
    FINDTEXT RFC 822: Standard for the Format of Arpa Internet Text Messages
    CLICK


### Build

If you want to build the system you will need the Chrome browser and the Chrome Driver.
The program itself is written in Java 8. Just invoke Maven to build it:

    mvn clean compile assembly:single package 

For the docker image just copy the target .jar file to the docker sub-directory
and run the standard docker build from there.


### Follow up

Just open an issue or tweet me if you like.

Thanks!
