FROM tomcat:9.0-alpine
#CMD  ./gradlew.bat publish_war
# Delete existing ROOT folder
RUN rm -rf /usr/local/tomcat/webapps/ROOT
# Copy to images tomcat path
COPY ./out/artifacts/ROOT.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]