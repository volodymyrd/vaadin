package com.gmail.volodymyrdotsenko.cms.fe.web;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
	
	@RequestMapping("/login")
	public String welcome(Map<String, Object> model) {

		return "auth/login";
	}
}