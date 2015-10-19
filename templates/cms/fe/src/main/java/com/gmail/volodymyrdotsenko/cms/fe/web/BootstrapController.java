package com.gmail.volodymyrdotsenko.cms.fe.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bootstrap")
public class BootstrapController {

	@RequestMapping
	public String welcome(Map<String, Object> model) {
		
		return "bootstrap/index";
	}
}