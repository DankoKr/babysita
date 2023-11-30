FROM gradle:7.5.0-jdk17

# Install netcat (nc)
RUN apt-get update && apt-get install -y netcat

# Copy the wait-for-db script
COPY wait-for-db.sh /wait-for-db.sh
RUN chmod +x /wait-for-db.sh

WORKDIR /opt/app

# Copy your application jar
COPY ./build/libs/babysita-0.0.1-SNAPSHOT.jar ./

# Set the Spring profile to 'docker'
ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8080

# Update the ENTRYPOINT to use the wait-for-db script
ENTRYPOINT ["/wait-for-db.sh", "host.docker.internal", "java", "-jar", "babysita-0.0.1-SNAPSHOT.jar"]

