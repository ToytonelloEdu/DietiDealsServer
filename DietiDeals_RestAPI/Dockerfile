FROM maven:3.8.5-openjdk-17

# application placed into /opt/app
RUN mkdir -p /opt/app
WORKDIR /opt/app

# copy the POM file
COPY pom.xml /opt/app/

# rest of the project
COPY src /opt/app/src
RUN mvn package

COPY ./wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN chmod +x /usr/local/bin/wait-for-it.sh

EXPOSE 8080

CMD ["/usr/local/bin/wait-for-it.sh", "db:5432", "--timeout=30", "--", "mvn", "exec:java"]