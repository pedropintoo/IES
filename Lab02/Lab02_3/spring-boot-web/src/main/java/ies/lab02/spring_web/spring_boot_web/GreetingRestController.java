package ies.lab02.spring_web.spring_boot_web;

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