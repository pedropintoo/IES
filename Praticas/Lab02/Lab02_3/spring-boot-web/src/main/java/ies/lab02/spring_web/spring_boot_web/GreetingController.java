package ies.lab02.spring_web.spring_boot_web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="msg", required=false, defaultValue="World") String message, Model model) {
		model.addAttribute("message", message.replace("\"", ""));
		return "greeting";
	}

}