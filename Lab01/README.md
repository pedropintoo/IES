115304
# Lab 01

## Maven installation

1. Download the latest version from https://maven.apache.org/download.cgi
2. Extract with root permissions: ```tar xzvf apache-maven-3.9.9-bin.tar.gz -C /opt/```
3. Configure the environment variables (for permanent configuration, add the following lines to the end of the file ```~/.bashrc```):
    - ```export JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64/"```
    - ```export MAVEN_HOME="/opt/apache-maven-3.9.9"```
    - ```export PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"```
4. Check the installation with the command: ```mvn -version``` and ```java --version```

## Maven lifecycle

1. **validate**: validate the project is correct and all necessary information is available
2. **compile**: compile the source code of the project
3. **test**: test the compiled source code using a suitable unit testing framework. These tests should not require the code be packaged or deployed
4. **package**: take the compiled code and package it in its distributable format, such as a JAR.
5. **verify**: run any checks on results of integration tests to ensure quality criteria are met
6. **install**: install the package into the local repository, for use as a dependency in other projects locally
7. **deploy**: done in the build environment, copies the final package to the remote repository for sharing with other developers and projects

- **clean**: cleans up artifacts created by prior builds
- **site**: generates site documentation for this project (target/site)

## Maven - maven-archetype-quickstart

1. Create a new project with the following command: 
    ```sh
    mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
    ```
2. Change to the project directory: ```cd my-app```
3. Compile the project: ```mvn compile```
4. Run the project: ```java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App```

**Explanation** of some terms:
- **Maven archetype** - a template for creating projects, which can be used to create a new project with a specific structure
- **artifactId** - the unique identifier for the project, convention: lowercase, hyphen-separated (e.g., my-app)
- **groupId** - the unique identifier for the project's package, convention: reversed domain name (e.g., com.mycompany.app)
- **classifier** - a way to distinguish artifacts that were built from the same POM (!!) but differ in some way (eg. platform, JDK version, etc.)

## Maven – weather forecast 

1. Create a **new project** with the following command: 
    ```sh
    mvn archetype:generate -DgroupId=com.mycompany.weatherradar -DartifactId=my-weather-radar -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
    ```
2. Change to the project directory: ```cd my-weather-radar```
3. Add additional properties in the ```pom.xml``` file, such as **developer team, encoding or Java version**:
    ```xml
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>
    ```
4. Declaring **project dependencies** in the ```pom.xml``` file:
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
5. Construct the application. (https://gist.github.com/bastiao/9e3230329327ff3a45e61c4f0927911c & https://gist.github.com/bastiao/60c49452aa5246579744bad6de6c0f26)
6. **Package** the project: ```mvn package``` #get dependencies, compiles the project and creates the jar
7. **Run** the project: ```mvn exec:java -Dexec.mainClass="weatherradar.WeatherStarter"``` #adapt to match your own
package structure and **class name**
8. Change the code to accept the city code in argument and run the project with the following command: ```mvn exec:java -Dexec.mainClass="weatherradar.WeatherStarter" -Dexec.args="1010500"``` # change the city code to match your own (eg. Aveiro: 1010500)
9. Create a git repository and push the project to it.


## Simple contribution to Weather Radar Project

1. **Clone** the project: 
    ```sh 
    git clone git@github.com:detiuaveiro/individual-pedropintoo.git ies-pedropintoo-cloned
    ```

2. Add a **new feature** to the project and push it to the repository as a new branch:
    ```sh
    cd ies-pedropintoo-cloned
    git checkout -b feature/logging-track
    # add the feature (in this case, the logging track)
    git add .
    git commit -m "Add logging track" # maybe more commits
    git push origin feature/logging-track
    ```

3. Create a **pull request**:
    - Access the repository on GitHub
    - Click on the "Pull requests" tab
    - Click on the "New pull request" button
    - Select the branch with the new feature
    - Click on the "Create pull request" button
    - Add a title and a description to the pull request
    - Click on the "Create pull request" button

4. After the pull request is accepted (by the project maintainer), **synchronize** the cloned repository with the original repository:
    ```sh
    git pull
    ```

## Logging track with Log4j2 in Maven

1. Add the **Log4j2 dependency** to the ```pom.xml``` file:
    ```xml
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.6.1</version>
        </dependency>
        <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.6.1</version>
    </dependency>
    ```

2. Create a ```log4j2.xml``` file in the ```src/main/resources``` (good practice) directory with the following content, to log the messages **to the console and to a file**:
    - **Appenders**: define where the log messages are sent (e.g., console, file)
    - **Loggers**: define the log levels (e.g., info, error, debug)
    - **PatternLayout**: define the format of the log messages
    - **Root**: define the default log level and the appenders
    - **Configuration**.status: define the log level for the configuration messages (e.g., **error**, warn, info, debug, trace)
    ```xml
    <Configuration status="error" name="weather-radar" packages="">
        <Appenders>
            <!-- Console Appender -->
            <Console name="stdout" target="SYSTEM_OUT">
                <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %p %m%n"/>
            </Console>
            <!-- File Appender -->
            <File name="fout" fileName="weather-radar.log" append="true">
                <PatternLayout>
                    <Pattern>%d{yyyy-MM-dd HH:mm:ss} %-5p %m%n</Pattern>
                </PatternLayout>
            </File>
        </Appenders>

        <Loggers>
            <Root level="info">
                <AppenderRef ref="stdout" /> <!-- Console Appender -->
                <AppenderRef ref="fout"/>    <!-- File Appender -->
            </Root>
        </Loggers>
    </Configuration>
    </Configuration>
    ```
    
3. **Use the logger** in the java code:
    ```java
    import org.apache.logging.log4j.LogManager;
    import org.apache.logging.log4j.Logger;
    ...
    private static final Logger logger = LogManager.getLogger(WeatherStarter.class);
    ...
    logger.info("Info message");
    logger.error("Error message");
    logger.debug("Debug message");
    ```


## Portainer to manage Docker containers

1. **Install Docker** - See in Docker documentation
2. **Install Portainer** - See in Portainer documentation (using docker deployment option)
3. **Config Portainer** - Access the Portainer interface, depending on which port you have configured in the docker run -p option (e.g., http://localhost:9443, http://localhost:9000). Create an admin user and select the local Docker environment.
4. **Multiple services (Docker compose)** - When deploying multiple containers, it is recommended to use Docker Compose. Create a `compose.yaml` file with the services and deploy them with the command `docker-compose up` (-d to run in the background). 


## Example of Docker Compose
In directory `Lab01/Lab01_4/composetest` you have an example of Docker Compose with two services: a Redis database and a simple Flask application. When composing, it pulls a Redis image, builds an image for your code, and start the services you defined. In this case the code is statically copied into the image at build time. 

 - **Build** the services: `docker compose build`
 - **Run** the services: `docker compose up` (`--watch` for compose to watch for changes in the files)
 - **Check** the services: http://localhost:8050/
 - **Stop** the services: `docker compose down`