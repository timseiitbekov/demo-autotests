FROM gradle:8.10.2-jdk21

WORKDIR /app
ENV GRADLE_USER_HOME=/gradle_home

RUN mkdir /gradle_home && \
    chmod 777 /gradle_home

# Copy all project files
COPY . .
RUN chmod +x gradlew

# Download dependencies by actually running the tasks
RUN ./gradlew clean test -Ptags='sometagnotexists' --no-daemon || true && \
    ./gradlew clean ktlintCheck --no-daemon || true && \
    ./gradlew clean compileKotlin --no-daemon || true && \
    ./gradlew clean compileTestKotlin --no-daemon || true && \
    ./gradlew dependencies || true

# Clean up the copied project files to ensure we use mounted ones later
RUN rm -rf /app/*