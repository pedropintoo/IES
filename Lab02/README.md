115304
# Lab 02 - ....

## Table of Contents
...

---

## Embedded Jetty Server

First, add dependencies to the `pom.xml` file:

```xml
<dependency>
    <groupId>org.eclipse.jetty</groupId>
    <artifactId>jetty-server</artifactId>
    <version>9.2.15.v20160210</version>
</dependency>
<dependency>
    <groupId>org.eclipse.jetty</groupId>
    <artifactId>jetty-servlet</artifactId>
    <version>9.2.15.v20160210</version>
</dependency>
```

Now, create a new java file as `EmbeddedJettyExample.java`:
```java
import org.eclipse.jetty.server.Server;
 
public class EmbeddedJettyExample {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8680);
        try {
            server.start();
            server.dumpStdErr(); // not required, helps in debugging
            server.join();
        } catch (Exception e) {           
            e.printStackTrace();
        }  
    }
}
```

Hear, you create a Server from `org.eclipse.jetty.server.Server` and start it at port `8680`. The `dumpStdErr()` method is used to dump the server's standard error output to the console. The `join()` method is used to wait for the server to stop.

Now, the server is running at `http://0.0.0.0:8680`. Try connectivity by searching on a browser or with a simple `GET` request:
```bash
curl -X GET http://0.0.0.0:8680
```

Upgrade the server to handle http requests. For this, create a new class `HelloServlet.java` that will be serving as a simple servlet running inside an embedded jetty server with `ServerHandler`:
```java
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet 
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>New Hello Simple Servlet</h1>"); 
    } 
}
```

Also, change the `EmbeddedJettyExample.java` to include the `HelloServlet` before starting the server (i.e., `server.start()`):
```java
ServletHandler servletHandler = new ServletHandler();
server.setHandler(servletHandler);
servletHandler.addServletWithMapping(HelloServlet.class, "/");
```

With this change, the server is now handling requests to the root path `/` with the `HelloServlet` servlet. Mapping the servlet to the root path means that the servlet will be invoked when the server receives a request to the root path.

You can run the server using Maven and rety the http `GET` request:
```bash
mvn exec:java -Dexec.mainClass="ies.lab02.EmbeddedJettyExample" -q
curl -X GET http://0.0.0.0:8680
```

If you wanna accept request parameters to the http `GET` request, you can use the `request.getParameter()` method. For example, to get the `msg` parameter from the request:
```java
String message = request.getParameter("msg");
```

:warning: **Note**: This method doesn't sanitize the input, be careful with XSS attacks.

And now, try to run the server and make a request with a parameter:
```bash
mvn exec:java -Dexec.mainClass="ies.lab02.EmbeddedJettyExample" -q
curl -X GET http://0.0.0.0:8680?msg=Alice
```

## Server-side programming and application servers (Tomcat)

We are using Jakarta EE (formerly Java EE) to create a simple web application in `Lab02_2/JakartaWebStarted/src/main/java`. There are multiple ways to run the application server service and deploy artifacts into it; for now,
we will run Tomcat in a Docker container.
On first hand, we create `HelloServlet` derived from `HttpServlet`. Notice that `WebServlet` annotation is used to map the servlet to the endpoint  `/hello-servlet` and the `doGet` method is used to handle the GET requests.
```java
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet 
{
    private String message;

    public void init() 
    {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        if (request.getParameter("msg") != null) 
        {
            this.message = request.getParameter("msg").replace("\"", "");
        }   
        out.printf("<h1>Hello %s!</h1>\n",this.message);

        out.println("</body></html>");
    }

    public void destroy() {
    }
}
```

Now, to deploy it in Tomcat, we need to create a `docker-compose.yml` file in the root directory of the project:
```yaml
version: '3'
services:
    tomcat:
        image: tomcat:10.1-jdk17
        container_name: jakarta_web_starter
        ports:
            - "8080:8080"
        volumes:
            - ./target/:/usr/local/tomcat/webapps/
```

After, we need to build the project and run the docker container:
```bash
mvn clean package
docker compose up
```

Now, you can access the application via [http://127.0.0.1:8080/JakartaWebStarter-1.0-SNAPSHOT/hello-servlet?msg="Pedro"](http://127.0.0.1:8080/JakartaWebStarter-1.0-SNAPSHOT/hello-servlet?msg="Pedro").


## Spring Boot - Web development with a full-featured framework

We will use **VSCode** to the following steps. 

First, create a new Spring Boot project using the **Spring Initializr**. Open the VSCode and install the **Spring Boot Extension Pack**. 
Then, create a new project using the **Spring Initializr** in command palette (`Ctrl+Shift+P`) and search for `Spring Initializr: Generate a Maven Project`. Choose the following options:
```
- Project:      Maven Project
- Language:     Java
- Spring Boot:  3.3.4
- Dependencies: Spring Web
- GroupID:      ies.lab02
- ArtifactID:   spring-boot-web
```

### Spring MVC Controller

After this, you need to create a Web Controller to handle the requests. You can easily identify the controller by the `@Controller` and `@GetMapping` annotation. In the following example, **GreetingController** handles GET requests for `/greeting` by returning the name of a **View** (in this case, greeting). A **View** is responsible for rendering the HTML content. The following listing shows the controller:
```java
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

}
```

This controller is concise and simple, but there is plenty going on. We break it down step by step.
 - The **@GetMapping** annotation ensures that HTTP GET requests to `/greeting` are mapped to the greeting() method.
 - The **@RequestParam** binds the value of the query string parameter name into the name parameter of the `greeting()` method. This query string parameter is not required. If it is absent in the request, the defaultValue of World is used. The value for the name parameter is added to a Model object, ultimately making it accessible to the view template.

Now, we use a server-side rendering technology called **Thymeleaf** to render the HTML content. Create a new file (**in resources!**) `src/main/resources/templates/greeting.html`, where the `th:text` attribute (`${name}`) is used to evaluate the expression and display the result:

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head> 
    <title>Getting Started: Serving Web Content</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <p th:text="|Hello, ${name}!|" />
</body>
</html>
```

Now, when you change something you need to restart the server. You can solve this problem by using the **Spring Boot DevTools**. Add the following dependency to the `pom.xml` file:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
</dependency>
```

After this, you can create a main class to run the application. Create a new file `src/main/java/ies/lab02/SpringBootWebApplication.java`:
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServingWebContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class, args);
    }

}
```


**@SpringBootApplication** is a convenience annotation that adds all of the following:

 - **@Configuration**: Tags the class as a source of bean definitions for the application context.

 - **@EnableAutoConfiguration**: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings. For example, if `spring-webmvc` is on the classpath, this annotation flags the application as a web application and activates key behaviors, such as setting up a DispatcherServlet.

 - **@ComponentScan**: Tells Spring to look for other components, configurations, and services in the `ies.lab02.spring_web` package, letting it find the controllers.

The `main()` method uses Spring Boot’s `SpringApplication.run()` method to launch an application. Did you notice that there was not a single line of XML? There is no web.xml file, either. This **web application is 100% pure Java** and you did not have to deal with configuring any plumbing or infrastructure. To clean, compile and test the application, use the following Maven command:
```bash
mvn clean package
java -jar target/spring-boot-web-0.0.1-SNAPSHOT.jar
```
and access the application via [http://localhost:8080/greeting?name=Pedro](http://localhost:8080/greeting?name=Pedro).


You can also, add a home page in `resources/static/index.html`:
```html
<!DOCTYPE HTML>
<html>
<head> 
    <title>Getting Started: Serving Web Content</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <p>Get your greeting <a href="/greeting">here</a></p>
</body>
</html>
```

Now, you can access the home page via [http://localhost:8080](http://localhost:8080).


### Spring RESTful web service controller

In the previous example, the controller returned a view. However, if you want to return data (like JSON, XML, etc.), you can use the **@RestController** annotation. The conversion of the object to JSON is done automatically by Spring Boot using the **Jackson library** (`record` is a new feature in Java 14 that is similar to a `class` but with less boilerplate code).

The following example shows a controller that return a simple "greeting" JSON object:
```java
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingRestController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
     
    public record GreetingRest(long id, String content) { } /// Simple immutable data object

	@GetMapping("/greeting_rest")
	public GreetingRest greeting_rest(@RequestParam(value = "msg", defaultValue = "World") String message) {
		return new GreetingRest(counter.incrementAndGet(), String.format(template, message));
	}
}
```

A key difference between a traditional **MVC controller** and the **RESTful web service controller** shown earlier is the way that the HTTP response body is created. Rather than relying on a view technology to perform server-side rendering of the greeting data to HTML, this RESTful web service controller populates and returns a `Greeting` object. The object data will be written directly to the HTTP response as JSON.

 - This code uses Spring **@RestController** annotation, which marks the class as a controller where every method returns a domain **object instead of a view**. It is shorthand for including both **@Controller** and **@ResponseBody**.

 - The **Greeting object** must be converted to JSON. Thanks to Spring’s HTTP message converter support, you need not do this conversion manually. Because **Jackson 2 is on the classpath**, Spring’s `MappingJackson2HttpMessageConverter` is automatically chosen to convert the `Greeting` instance **to JSON**.

Now, test the RESTful endpoint with a simple `curl` GET request:
```bash
curl -X GET http://localhost:8080/greeting_rest?msg=Alice
```

## RESTful web service - quotes

In simpler terms, **Jakarta EE** is powerful and comprehensive, but for many cases (like creating **RESTful APIs**), we don’t need the full feature set of an application server. 
So, for **RESTful web services**, we can use **Spring Boot** to create a simple application that returns quotes depending on the request. 
The Controller is structured as follows:
```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class QuotesController 
{
	public static Map<Integer,Set<String>> quotesDict = Map.of(
		1, Set.of(
			"Just when I thought I was out, they pull me back in. - The Godfather III (1990)",
			"Life moves pretty fast. If you don't stop and look around once in a while, you could miss it. - Ferris Bueller's Day Off (1986)"
		),
		2, Set.of(
			"We're goin' streaking! - Old School (2003)"
		),
		3, Set.of(
			"I'm sorry father, for you there is only death. But our destiny is life! - The Fountain (2006)",
			"Sometimes it's people closest to us who lie to us best - Arrow (2015)",
			"Hope is a good thing, maybe the best of things, and no good thing ever dies. - Shawshank Redemption (1994)"
		),
		... // TODO: complete this list
	);

    public record QuotesRecord(Set<String> contents) { }
	public record ShowsRecord(Set<Integer> showsId) { }

	@GetMapping("/api/shows")
	public ShowsRecord api_shows() 
	{
		return new ShowsRecord(QuotesController.quotesDict.keySet());
	}

	@GetMapping("/api/quote")
	public QuotesRecord api_quote() 
	{
		int randomIdx = (int) (Math.random() * quotesDict.size()) + 1;
		return new QuotesRecord(QuotesController.quotesDict.get(randomIdx));
	}

	@GetMapping("/api/quotes")
	public QuotesRecord api_quote(@RequestParam(value="show", required=false, defaultValue="-1") int showID) 
	{
		if (!QuotesController.quotesDict.containsKey(showID)) {
			return new QuotesRecord(Set.of("Show not found. Usage: /api/quotes?show=<showID>"));
		}

		return new QuotesRecord(QuotesController.quotesDict.get(showID));
	}
}
```

Hear, we have three endpoints:
 - `/api/shows`: returns the list of shows available in the `quotesDict`.
 - `/api/quote`: returns a random quote from the `quotesDict`.
 - `/api/quotes?show=<show_id>`: returns all quotes from a specific show. If the show is not found, it returns an error message.

You can give a go to the application by running the following command:
```bash
mvn clean package
java -jar target/spring-boot-web-0.0.1-SNAPSHOT.jar
```

And try the endpoints:
```bash
curl -X GET http://localhost:8080/api/shows | jq
curl -X GET http://localhost:8080/api/quote | jq
curl -X GET http://localhost:8080/api/quotes?show=3 | jq
```
