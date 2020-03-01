FROM ubuntu
RUN apt-get update && \
    apt-get install -y wget && \
    apt-get install -y curl && \
    apt-get install sudo -y 

# Install Java8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk;


# Install Scala 2.12.8
RUN wget https://downloads.lightbend.com/scala/2.12.8/scala-2.12.8.deb
RUN dpkg -i scala-2.12.8.deb

# Install sbt
ARG SBT_VERSION=1.3.8

RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt 

# Install vim, git, unzip
RUN apt install -y vim
RUN apt install -y git
RUN apt-get install -y unzip

# Install Node 12
RUN apt-get install -y npm

RUN curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash && \
    sudo apt-get install -y nodejs

# Install npm 6.8
RUN npm install -g npm@6.8.0

VOLUME /home/szymon.smykala/projekt

# Verify
RUN vim --version
RUN git --version
RUN java -version  
RUN scala -version
# RUN sbt sbtVersion
RUN unzip -v
RUN node --version
RUN npm --version


CMD ["bash"]
