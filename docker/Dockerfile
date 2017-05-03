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

## switch back to default user
USER 1984
