# webprofiler

A small program to profile web sites. Includes a command language to navigate sites.

It uses WebDriver to drive the browser and get the performance logs which are later displayed as json format.
There is a docker image to ease the usage.


### Usage

The fastest way to test webprofiler is to download the image with the built-in test of some of the W3C organization
and run the container:

    docker pull herchu/webprofiler
    docker run herchu/webprofiler /home/headless/webprofiler.sh

You will see the following output:

    Starting virtual X frame buffer: Xvfb.
    Starting ChromeDriver 2.29.461571 (8a88bbe0775e2a23afda0ceaf2ef7ee74e822cc5) on port 18530
    Only local connections are allowed.
    May 07, 2017 10:56:53 AM org.openqa.selenium.remote.ProtocolHandshake createSession
    INFO: Attempting bi-dialect session, assuming Postel's Law holds true on the remote end
    May 07, 2017 10:56:56 AM org.openqa.selenium.remote.ProtocolHandshake createSession
    INFO: Detected dialect: OSS

Then the output of the program itself. First the steps of the program:

    Run: GO https://www.w3.org/
    Run: FINDTEXT Web Security
    Run: CLICK
    Run: FINDNAME q
    Run: TYPE rfc 822\n
    Run: FINDTEXT RFC 822: Standard for the Format of Arpa Internet Text Messages
    Run: CLICK

And it ends with the performace logs. The ones you are looking for:

    {"rqmethod":"GET / HTTP/1.1","url":"https://www.w3.org/","requestId":"306.1","starts":8174.135192,"ends":8174.465221,"time":330.02900000064983}
    {"rqmethod":"GET /2008/site/css/minimum HTTP/1.1","url":"https://www.w3.org/2008/site/css/minimum","requestId":"306.2","starts":8174.745157,"ends":8174.876674,"time":131.51699999980337}
    {"rqmethod":"GET /2008/site/css/advanced HTTP/1.1","url":"https://www.w3.org/2008/site/css/advanced","requestId":"306.3","starts":8174.747878,"ends":8174.898895,"time":151.01700000013807}
    {"rqmethod":"GET /2008/site/images/logo-w3c-mobile-lg HTTP/1.1","url":"https://www.w3.org/2008/site/images/logo-w3c-mobile-lg","requestId":"306.4","starts":8174.899451,"ends":8175.034737,"time":
    ... etc ...


This image is mostly entirely based on Selenium's ones.

Of course you want to use your own test suite. For this I recommend to place the command file
in a docker virtual drive. For instance:

    mkdir /tmp/data
    cp MY-TEST.tst /tmp/data
    docker run -v /tmp/data:/data herchu/webprofiler /home/headless/webprofiler.sh /data/MY-TEST.tst


### Command language

The command language is very simple. Currently  `go`, `findid`, `findname`, `findtext`, `findxpath`,
`findcss`, `type`,  `click` and `select` are implemented. You can use any Web Inspector tool from
the main browsers to see the arguments for the find commands.

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
The program itself is written in Java 8. Just invoke Maven from the `webprofiler-java`
directory to build it:

    mvn clean compile assembly:single package 

For the docker image just run the standard docker build from the docker subdirectory:

    docker build -t herchu/webprofiler .


### Future work

Besides bugs the main features that would nice to be addressed are:

1. API for the output logs.
1. Debugging parameters (allow for more verbose output).
1. Output to HAR format.


### Follow up

Just open an issue or tweet me if you like.

Thanks!
