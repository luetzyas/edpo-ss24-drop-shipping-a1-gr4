FROM openjdk:21
VOLUME /tmp
ADD target/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar