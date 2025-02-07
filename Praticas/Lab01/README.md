115304
# Lab 01 - Maven, Logging, and Docker Integration

## Table of Contents
1. [Maven Installation](#maven-installation)
2. [Maven Lifecycle](#maven-lifecycle)
3. [Maven Archetype Quickstart](#maven---maven-archetype-quickstart)
4. [Weather Forecast Project](#maven---weather-forecast)
5. [Contributing to the Weather Radar Project](#simple-contribution-to-weather-radar-project)
6. [Logging with Log4j2](#logging-track-with-log4j2-in-maven)
7. [Portainer for Docker Management](#portainer-to-manage-docker-containers)
8. [Docker Compose Example](#example-of-docker-compose)
9. [Wrapping Up & Integrating Concepts](#wrapping-up--integrating-concepts)

---

## Maven Installation

1. **Download** the latest Maven version from [Maven Download](https://maven.apache.org/download.cgi).
2. **Extract** with root permissions:
    ```bash
    sudo tar xzvf apache-maven-3.9.9-bin.tar.gz -C /opt/
    ```
3. **Configure Environment Variables** (for a permanent setup, add these lines to `~/.bashrc`):
    ```bash
    export JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64/"
    export MAVEN_HOME="/opt/apache-maven-3.9.9"
    export PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"
    ```
4. **Verify Installation**:
    ```bash
    mvn -version
    java --version
    ```

---

## Maven Lifecycle

- **validate**: Validates the project and checks for necessary information.
- **compile**: Compiles the project’s source code.
- **test**: Runs unit tests on the compiled code.
- **package**: Packages the code into a distributable JAR.
- **verify**: Performs checks on results of integration tests.
- **install**: Installs the package into the local repository.
- **deploy**: Copies the package to a remote repository.

Additional goals:
- **clean**: Cleans up artifacts from previous builds.
- **site**: Generates site documentation for the project.

---

## Maven - Maven Archetype Quickstart

1. **Create a new project**:
    ```bash
    mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
    ```
2. **Change to the project directory**:
    ```bash
    cd my-app
    ```
3. **Compile the project**:
    ```bash
    mvn compile
    ```
4. **Run the project**:
    ```bash
    java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App
    ```

**Key Terms Explained**:
- **Archetype**: A template for creating projects.
- **artifactId**: Unique project identifier, lowercase and hyphen-separated.
- **groupId**: Unique package identifier, typically reversed domain name.

---

## Maven – Weather Forecast

1. **Create a new project**:
    ```bash
    mvn archetype:generate -DgroupId=com.mycompany.weatherradar -DartifactId=my-weather-radar -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
    ```
2. **Configure the project**:
    - Add properties in `pom.xml`:
      ```xml
      <properties>
          <maven.compiler.source>21</maven.compiler.source>
          <maven.compiler.target>21</maven.compiler.target>
      </properties>
      ```
    - Add dependencies:
      ```xml
      <dependency>
          <groupId>com.squareup.retrofit2</groupId>
          <artifactId>retrofit</artifactId>
          <version>2.11.0</version>
      </dependency>
      <dependency>
          <groupId>com.squareup.retrofit2</groupId>
          <artifactId>converter-gson</artifactId>
          <version>2.11.0</version>
      </dependency>
      ```
3. **Build and run the project**:
    ```bash
    mvn package
    mvn exec:java -Dexec.mainClass="weatherradar.WeatherStarter"
    ```

---

## Simple Contribution to Weather Radar Project

1. **Clone the project**:
    ```bash
    git clone git@github.com:detiuaveiro/individual-pedropintoo.git ies-pedropintoo-cloned
    ```
2. **Create a new branch and add a feature**:
    ```bash
    git checkout -b feature/logging-track
    git add .
    git commit -m "Add logging track"
    git push origin feature/logging-track
    ```
3. **Create a Pull Request on GitHub**.
4. **Synchronize with the original repository**:
    ```bash
    git pull
    ```

---

## Logging Track with Log4j2 in Maven

1. **Add Log4j2 dependencies** to `pom.xml`:
    ```xml
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.24.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.24.0</version>
    </dependency>
    ```
2. **Create `log4j2.xml`** in `src/main/resources`:
    - **Appenders**: define where the log messages are sent (e.g., console, file)
    - **Loggers**: define the log levels (e.g., info, error, debug)
    - **PatternLayout**: define the format of the log messages
    - **Root**: define the default log level and the appenders
    - **Configuration**.status: define the log level for the configuration messages (e.g., **error**, warn, info, debug, trace)
    ```xml
    <Configuration status="error" name="weather-radar">
        <Appenders>
            <Console name="stdout" target="SYSTEM_OUT">
                <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %p %m%n"/>
            </Console>
            <File name="fout" fileName="weather-radar.log" append="true">
                <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %m%n"/>
            </File>
        </Appenders>
        <Loggers>
            <Root level="info">
                <AppenderRef ref="stdout"/>
                <AppenderRef ref="fout"/>
            </Root>
        </Loggers>
    </Configuration>
    ```
    
3. **Implement the logger** in your Java code:
    ```java
    import org.apache.logging.log4j.LogManager;
    import org.apache.logging.log4j.Logger;
    
    private static final Logger logger = LogManager.getLogger(WeatherStarter.class);
    
    logger.info("Info message");
    logger.error("Error message");
    ```

---

## Portainer to Manage Docker Containers

1. **Install Docker** - Refer to the official Docker documentation.
2. **Install Portainer** - Follow Portainer’s Docker deployment option.
3. **Configure Portainer**: Access the Portainer interface at `http://localhost:9000` or your configured port.

---


## Example of Docker Compose

1. **Navigate** to the `Lab01/Lab01_4/composetest` directory.
2. This example demonstrates a **Docker Compose setup** with two services: a Redis database and a simple Flask application. The setup pulls a Redis image, builds an image for your Flask application, and starts the defined services.

### Commands:
- **Build the services**:
    ```bash
    docker compose build
    ```
- **Run the services**:
    ```bash
    docker compose up
    ```
- **Check the application**: Access it via [http://localhost:8050/](http://localhost:8050/).
- **Stop the services**:
    ```bash
    docker compose down
    ```

---

## Wrapping-up & Integrating Concepts

### Shading the IPMA API Client
1. **Configure Maven Shade Plugin** in the `pom.xml` for Maven Project 2.
2. **Build the Shaded JAR**:
    ```bash
    mvn clean package
    ```
   The JAR will be created in the `target` directory as `ipma-api-client-1.0-SNAPSHOT-shaded.jar`. Move it to the `libs` directory of Project 1.

3. **Add the shaded jar to the local repository**:
    ```bash
    mvn install:install-file -Dfile=./libs/ipma-api-client-1.0-SNAPSHOT-shaded.jar -DgroupId=ies.lab01.ipma.api.client -DartifactId=ipma-api-client -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Dclassifier=shaded
    ```

4. **Reference the Shaded JAR** in the `AnyCityForecast` project:
    ```xml
    <dependency>
        <groupId>ies.lab01.ipma.api.client</groupId>
        <artifactId>ipma-api-client</artifactId>
        <version>1.0-SNAPSHOT</version>
        <classifier>shaded</classifier>
    </dependency>
    ```

5. **Compile and Run the project**:
    ```bash
    mvn clean package -U
    mvn exec:java -Dexec.mainClass="ies.lab01.forecast.App"
    ```

### Dockerizing the Application

1. **Create a Dockerfile** in the project root directory:
    ```Dockerfile
    FROM eclipse-temurin:21-jdk-alpine 
    COPY target/any-city-forecast-1.0-SNAPSHOT-shaded.jar /app.jar
    ENTRYPOINT ["java","-jar","/app.jar"]
    ```
2. **Build the Docker image**:
    ```bash
    docker build -t any-city-forecast:latest .
    ```
3. **Run the Docker container**:
    ```bash
    docker run --name any-city-forecast-container any-city-forecast:latest
    ```
4. **Start the container** with output attached:
    ```bash
    docker start any-city-forecast-container -a
    ```

### If Running in Background

- **View logs**:
    ```bash
    docker logs any-city-forecast-container
    ```
- **Stop the container**:
    ```bash
    docker stop any-city-forecast-container
    ```

---

## References

- [Maven](https://maven.apache.org/)
- [Log4j2](https://logging.apache.org/log4j/2.x/)
- [Docker](https://www.docker.com/)
- [Portainer](https://www.portainer.io/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Maven Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/)
