package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.viritin.label.RichText;

import com.gmail.volodymyrdotsenko.cms.fe.vaadin.Sections;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ClassResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Audio;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Secured({ "ROLE_USER", "ROLE_ADMIN" })
@SpringView(name = "mmadmin")
@SideBarItem(sectionId = Sections.ADMINISTRATION, caption = "Multi Media")
@FontAwesomeIcon(FontAwesome.MUSIC)
public class MultiMediaAdminView extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;

	private VerticalLayout vl = new VerticalLayout();

	public MultiMediaAdminView() {
		vl.setMargin(true);
		
		

	}

	public void buildAudio() throws URISyntaxException {
		
		StreamResource resource = new StreamResource(new StreamSource() {

			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				// TODO Auto-generated method stub
				return getClass().getResourceAsStream("/scorpions_send_me_an_angel.txt.vtt");
			}

		}, "scorpions_send_me_an_angel.txt.vtt");

		Link link = new Link("Link to the image file", resource);
		String protocol = UI.getCurrent().getPage().getLocation().getScheme();
		String currentUrl = UI.getCurrent().getPage().getLocation().getAuthority();
		String cid = link.getConnectorId();
		Integer uiId = link.getUI().getUIId();
		String filename = resource.getFilename();
		System.out.println(protocol + "://" + currentUrl + "/APP/connector/" + "" + "/" + "" + "/source/" + filename);

		vl.addComponent(link);
		setCompositionRoot(vl);


		CustomLayout sample = new CustomLayout();
		vl.addComponent(sample);

		String js = ""// "//<![CDATA[ "
				+ " var lyrics = document.getElementById('lyrics'); "
				+ " var audio = document.getElementById('audio'); " + " var track = document.getElementById('trk'); "
				+ " var timeUp = document.getElementById('timeUp'); " + " var textTrack = track.track; "
				+ " track.addEventListener('cuechange', cueChange, false); "
				+ " audio.addEventListener('timeupdate', timeUpdate, false); "
				+ " function cueChange(){ var cues = textTrack.activeCues; if (cues.length > 0){ lyrics.innerHTML = cues[0].text;}} "
				+ " function timeUpdate(){ timeUp.innerHTML=audio.currentTime;} ";
		// + " //]]>";

		sample.setTemplateContents("<audio id='audio' preload='auto' controls> "
				+ " <source src='http://localhost:8180/ielts/task1/scorpions_send_me_an_angel.mp3' type='audio/mpeg'>"
				+ " <track id='trk' kind='subtitles' srclang='en' src='http://localhost:8180/ielts/task1/scorpions_send_me_an_angel.txt.vtt' default  /></audio>"
				+ " <br/><div id='lyrics'></div><br/><div id='timeUp'></div><br/>");

		// Audio sample = new Audio();
		// final Resource audioResource = new ExternalResource(
		// "http://mirrors.creativecommons.org/ccmixter/contrib/Wired/The%20Rapture%20-%20Sister%20Saviour%20(Blackstrobe%20Remix).mp3");
		// sample.setSource(audioResource);
		// sample.setHtmlContentAllowed(true);
		// sample.setAltText("Can't play media");
		//
		// if(sample.getId() == null){
		// sample.setId(UUID.randomUUID().toString());
		// }

		// getUI().getPage().s

		JavaScript.getCurrent().execute(js);

		// Find the application directory
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

		System.out.println("basepath:" + basepath);

//		URL url = getClass().getResource("/scorpions_send_me_an_angel.txt.vtt");
//		FileResource resource = new FileResource(new File(url.toURI()));
//		// Resource r = new StreamResource();
//		System.out.println("resource:" + resource);
//		ExternalResource r = new ExternalResource(url);

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}