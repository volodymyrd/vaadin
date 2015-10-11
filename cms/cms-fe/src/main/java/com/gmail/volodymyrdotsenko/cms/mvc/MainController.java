package com.gmail.volodymyrdotsenko.cms.mvc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItem;
import com.gmail.volodymyrdotsenko.cms.be.services.MainService;
import com.gmail.volodymyrdotsenko.cms.be.services.MultiMediaService;

@Controller
@RequestMapping("/")
public class MainController {

	@Autowired
	private MainService mainService;

	@Autowired
	private MultiMediaService mmService;
	
	@Value("${application.message:Hello World}")
	private String message = "Hello World";

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		return "welcome";
		//return "redirect:/cms";
	}

	@RequestMapping("/admin")
	//@ResponseBody
	public String admin(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		return "admin";
		//return "redirect:/cms";
	}

	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content(@PathVariable("id") long id, HttpServletResponse response) {

		System.out.println(mmService);
		
		AudioItem item = mmService.getAudioItemWithContent(id);

		try {
			response.setHeader("Content-Disposition", "inline;filename=\"" + item.getName() + ".mp3\"");

			OutputStream out = response.getOutputStream();
			response.setContentType("Content-Type: audio/mpeg");

			IOUtils.copy(new ByteArrayInputStream(item.getContent().getContent()), out);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}