# Base image
FROM ubuntu:16.04

MAINTAINER hfoffani@gmail.com

# Sources
RUN \
    echo "deb http://archive.ubuntu.com/ubuntu xenial main universe\n" > /etc/apt/sources.list && \
    echo "deb http://archive.ubuntu.com/ubuntu xenial-updates main universe\n" >> /etc/apt/sources.list && \
    echo "deb http://security.ubuntu.com/ubuntu xenial-security main universe\n" >> /etc/apt/sources.list

# Basic tools
RUN \
    apt-get update -qqy && \
    apt-get -qqy --no-install-recommends install \
      bzip2 tzdata sudo unzip wget

# Install Java
RUN \
    apt-get -qqy --no-install-recommends install \
      ca-certificates openjdk-8-jre-headless && \
    sed -i 's/securerandom\.source=file:\/dev\/random/securerandom\.source=file:\/dev\/urandom/' ./usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/java.security

# Install Xvfb virtual X11.
RUN \
    apt-get -qqy install \
      locales xvfb

# Configure Xvfb
ADD xvfb_init /etc/init.d/xvfb
RUN chmod a+x /etc/init.d/xvfb
ENV DISPLAY :99.0

# Intall Google Chrome
RUN \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list && \
    apt-get -qqy update && \
    apt-get -qqy install google-chrome-stable

# Clean up
RUN \
    rm /etc/apt/sources.list.d/google-chrome.list && \
    rm -rf /var/lib/apt/lists/* /var/cache/apt/*

# Add user
RUN \
    useradd headless --shell /bin/bash --create-home && \
    usermod -a -G sudo headless && \
    echo 'ALL ALL = (ALL) NOPASSWD: ALL' >> /etc/sudoers && \
    echo 'headless:nopassword' | chpasswd

# Install WebDriver for Chrome
ARG CHROME_DRIVER_VERSION=2.29
RUN \
    wget -q -N http://chromedriver.storage.googleapis.com/$CHROME_DRIVER_VERSION/chromedriver_linux64.zip -P /tmp && \
    unzip /tmp/chromedriver_linux64.zip -d /tmp && \
    chmod +x /tmp/chromedriver && \
    mv -f /tmp/chromedriver /usr/local/share/chromedriver && \
    ln -s /usr/local/share/chromedriver /usr/local/bin/chromedriver && \
    ln -s /usr/local/share/chromedriver /usr/bin/chromedriver

# Patch launcher for headless Chrome inside Docker.
COPY chrome_launcher.sh /opt/google/chrome/google-chrome
RUN chmod +x /opt/google/chrome/google-chrome

# https://github.com/SeleniumHQ/docker-selenium/issues/87
ENV DBUS_SESSION_BUS_ADDRESS=/dev/null

# Install our application
# Copy files
RUN mkdir /data && \
    chmod 777 /data
COPY web-profiler-1.0-jar-with-dependencies.jar /home/headless
COPY webprofiler.sh /home/headless
RUN chmod +x /home/headless/webprofiler.sh
COPY demo.tst /home/headless

# USER headless

