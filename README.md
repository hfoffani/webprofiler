# webprofiler

A small program to profile web sites. Includes a command language to navigate sites.

It uses WebDriver to drive the browser and get the performance logs which are later displayed as json format.
There is a docker image to ease the usage.

### Usage

- download the image
- run


### Command language

Example:

  GO http://www.wikipedia.org
  

### Build

If you want to build the system you will need the Chrome browser and the Chrome Driver.
The program itself is written in Java 8. Just invoke Maven to build it:

  mvn
  
