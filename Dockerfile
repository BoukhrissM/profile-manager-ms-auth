FROM eclipse-temurin:17-jdk
VOLUME /tmp
# List files to verify build context
RUN ls -la target || echo "target folder not found"

COPY target/*.jar ms-auth.jar
ENTRYPOINT ["java","-Dspring.profiles.active=stg","-jar","/ms-auth.jar"]