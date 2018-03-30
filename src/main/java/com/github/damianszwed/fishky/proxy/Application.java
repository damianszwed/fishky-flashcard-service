package com.github.damianszwed.fishky.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/")
    public String home() {
        return "Hello World!";
    }

    /**
     * (Optional) App Engine health check endpoint mapping.
     * @see <a href="https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking"></a>
     * If your app does not handle health checks, a HTTP 404 response is interpreted
     *     as a successful reply.
     */
    @RequestMapping("/_ah/health")
    public String healthy() {
        // Message body required though ignored
        return "Still surviving.";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
