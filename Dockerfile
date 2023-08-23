#FROM tomcat:9.0-alpine
#CMD  ./gradlew.bat publish_war
# Delete existing ROOT folder
FROM tomcat:9.0.79-jdk8-corretto-al2
ENV JAVA_OPTS="-Xms2048m -Xmx2048m -XX:MaxPermSize=512m -XX:MaxMetaspaceSize=512m"
RUN rm -rf /usr/local/tomcat/webapps/ROOT
# Copy to images tomcat path
COPY ./out/artifacts/ROOT.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]