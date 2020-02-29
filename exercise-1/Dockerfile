FROM ubuntu
RUN apt-get update && \
    apt-get install -y wget;

# Install Java8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk;

RUN java -version    

# Install Scala 2.12.8
RUN wget https://downloads.lightbend.com/scala/2.12.8/scala-2.12.8.deb
RUN dpkg -i scala-2.12.8.deb
RUN scala -version

# Install sbt

# Install vim, git, unzip

CMD ["bash"]
