FROM consol/ubuntu-xfce-vnc

## Install java
USER 0
RUN \
    apt-get update -y && \
    apt-get install -y software-properties-common && \
    add-apt-repository -y ppa:webupd8team/java && \
    apt-get update -y && \
    (echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections) && \
    apt-get install -y oracle-java8-installer

# Install chromedriver
RUN \
    apt-get install unzip && \
    wget -N http://chromedriver.storage.googleapis.com/2.10/chromedriver_linux64.zip -P /tmp && \
    unzip /tmp/chromedriver_linux64.zip -d /tmp && \
    chmod +x /tmp/chromedriver && \
    mv -f /tmp/chromedriver /usr/local/share/chromedriver && \
    ln -s /usr/local/share/chromedriver /usr/local/bin/chromedriver && \
    ln -s /usr/local/share/chromedriver /usr/bin/chromedriver


## switch back to default user
USER 1984
