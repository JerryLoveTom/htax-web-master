FROM java:8
EXPOSE 8080

VOLUME /tmp
ADD htax-web-master-3.0.0.jar  /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
