FROM ubuntu
RUN apt-get update && \
    apt-get install -y wget && \
    apt-get install -y curl;

# Install Java8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk;

RUN java -version    

# Install Scala 2.12.8
RUN wget https://downloads.lightbend.com/scala/2.12.8/scala-2.12.8.deb
RUN dpkg -i scala-2.12.8.deb
RUN scala -version

# Install sbt
ARG SBT_VERSION=1.3.8

RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

# Install vim, git, unzip
RUN apt install -y vim
RUN vim --version
RUN apt install -y git
RUN git --version
RUN apt-get install -y unzip
RUN unzip -v

# Install npm 6.8


CMD ["bash"]
