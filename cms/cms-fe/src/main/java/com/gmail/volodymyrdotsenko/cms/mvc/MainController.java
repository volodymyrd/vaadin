package com.gmail.volodymyrdotsenko.cms.mvc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItem;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioSubtitle;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioSubtitleRepository;
import com.gmail.volodymyrdotsenko.cms.be.services.MainService;
import com.gmail.volodymyrdotsenko.cms.be.services.MultiMediaService;
import com.gmail.volodymyrdotsenko.cms.be.utils.Utils;

@Controller
@RequestMapping("/")
public class MainController {

	@Autowired
	private MainService mainService;

	@Autowired
	private MultiMediaService mmService;

	@Autowired
	private AudioSubtitleRepository audioSubtitleRepo;

	@Value("${application.message:Hello World}")
	private String message = "Hello World";

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		return "welcome";
		// return "redirect:/cms";
	}

	@RequestMapping("/admin")
	// @ResponseBody
	public String admin(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		return "admin";
		// return "redirect:/cms";
	}

	@RequestMapping(value = "/multimedia/audio/content/{id}", method = RequestMethod.GET)
	public String multiMediaAudioContent(@PathVariable("id") long id, HttpServletResponse response) {

		AudioItem item = mmService.getAudioItemWithContent(id);

		buildFileResponce(response, item.getFileName(), item.getFileLength().intValue(), item.getMIMEType(),
				item.getContent().getContent());

		return null;
	}

	@RequestMapping(value = "/multimedia/audio/subtitle/{id}", method = RequestMethod.GET)
	public String multiMediaAudioSubtitle(@PathVariable("id") long id, HttpServletResponse response) {

		List<AudioSubtitle> st = audioSubtitleRepo.findByItemOderByNum(id);

		StringBuilder sb = new StringBuilder("WEBVTT\n\n");
		st.forEach(e -> {
			int n = e.getId().getOrderNum().intValue() + 1;
			sb.append(n + "\n");
			sb.append(Utils.dateToVtt(e.getStart()) + " --> " + Utils.dateToVtt(e.getEnd()) + "\n");
			sb.append(e.getText() + "\n\n");
		});
		
		System.out.println("st\n" + sb.toString());

		byte[] content = sb.toString().getBytes();
		buildFileResponce(response, "st" + id + ".vtt", content.length, "", content);

		return null;
	}

	private void buildFileResponce(HttpServletResponse response, String fileName, int length, String mimeType,
			byte[] content) {
		try {
			response.setHeader("Content-Disposition", "inline;filename=\"" + fileName);

			OutputStream out = response.getOutputStream();
			response.setContentLength(length);
			response.setContentType("Content-Type: " + mimeType);

			IOUtils.copy(new ByteArrayInputStream(content), out);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}