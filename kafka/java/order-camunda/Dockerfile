FROM openjdk:17-alpine
RUN apk --no-cache add libstdc++ libc6-compat gcompat
VOLUME /tmp
ADD target/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.library.path=/usr/lib -jar /app.jar
