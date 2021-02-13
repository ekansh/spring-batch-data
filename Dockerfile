FROM openjdk:8-jdk-alpine
ARG JAR_FILE=vestings/target/*.jar
RUN echo " jar file $JAR_FILE"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

##create env variable , app will have access to it
ENV PORT=8080
## expose a port
EXPOSE 8080
RUN ls -latr /home
