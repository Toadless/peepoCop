FROM openjdk:14-jdk
MAINTAINER Toadless
COPY application.yml /production/application.yml
COPY peepoCop.jar /production/peepoCop.jar
WORKDIR /production/
CMD ["java","-jar","peepoCop.jar"]