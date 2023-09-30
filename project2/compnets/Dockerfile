FROM ubuntu
WORKDIR /compnets
RUN apt-get update
RUN apt-get install -y net-tools netcat tcpdump inetutils-ping openjdk-18-jdk
RUN apt-get install -y net-tools iputils-ping netcat tcpdump iptables
CMD ["/bin/bash"]

