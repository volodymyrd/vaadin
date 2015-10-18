package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.LanguageRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioSubtitle;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioSubtitleRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.Folder;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.FolderRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.MediaItemRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.MediaItem;
import com.gmail.volodymyrdotsenko.cms.be.services.MultiMediaService;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.Sections;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.EmbeddedView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

@Secured({ "ROLE_USER", "ROLE_ADMIN" })
@SpringView(name = "mmadmin")
@SideBarItem(sectionId = Sections.ADMINISTRATION, caption = "Multi Media")
@FontAwesomeIcon(FontAwesome.MUSIC)
public class MultiMediaAdminView extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;

	private VerticalLayout vl = new VerticalLayout();
	private Link linkVtt;
	private Link linkMp3;
	private Resource resourceVtt;
	private Resource resourceMp3;

	private final Set<String> langSet = new HashSet<>();
	private final VerticalLayout mainContainer = new VerticalLayout();
	private final VerticalLayout contentLayout = new VerticalLayout();
	private final MultiMediaMenuBar menu = new MultiMediaMenuBar(this);
	private final MediaLibraryTree mediaLibraryTree;
	private EmbeddedView currentEmbeddedView;

	private final ApplicationContext applicationContext;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public Set<String> getLangSet() {
		return langSet;
	}

	public FolderRepository getFolderRepo() {
		return folderRepo;
	}

	public MediaItemRepository getMediaItemRepo() {
		return mediaItemRepo;
	}

	public LanguageRepository getLangRepo() {
		return langRepo;
	}

	public MultiMediaService getService() {
		return service;
	}

	public AudioSubtitleRepository getAudioSubtitleRepo() {
		return audioSubtitleRepo;
	}

	private final FolderRepository folderRepo;
	private final MediaItemRepository mediaItemRepo;
	private final LanguageRepository langRepo;
	// private final MadiaItemContentRepository mediaItemContentRepo;
	private final MultiMediaService service;
	private final AudioSubtitleRepository audioSubtitleRepo;

	@Autowired
	public MultiMediaAdminView(ApplicationContext applicationContext) {

		this.applicationContext = applicationContext;
		this.folderRepo = applicationContext.getBean(FolderRepository.class);
		this.mediaItemRepo = applicationContext.getBean(MediaItemRepository.class);
		this.langRepo = applicationContext.getBean(LanguageRepository.class);
		this.service = applicationContext.getBean(MultiMediaService.class);
		this.audioSubtitleRepo = applicationContext.getBean(AudioSubtitleRepository.class);

		langRepo.findAll().forEach(e -> {
			langSet.add(e.getCode());
		});

		HorizontalSplitPanel spliter = new HorizontalSplitPanel();
		spliter.setSizeFull();
		spliter.setSplitPosition(20, Unit.PERCENTAGE);

		mediaLibraryTree = new MediaLibraryTree(this, applicationContext);
		spliter.setFirstComponent(mediaLibraryTree);
		spliter.setSecondComponent(contentLayout);

		mainContainer.addComponent(menu);
		mainContainer.addComponent(spliter);

		setCompositionRoot(mainContainer);
	}

	public void openView(Component component) {
		if (currentEmbeddedView != null) {
			currentEmbeddedView.close();
			currentEmbeddedView = null;
		}

		contentLayout.addComponent(component);

		if (component instanceof EmbeddedView) {
			currentEmbeddedView = (EmbeddedView) component;
		}
	}

	public Folder getSlectedFolder() {
		return folderRepo.getOne(mediaLibraryTree.getSelectedFolderNodeId());
	}

	public MediaItem save(MediaItem mediaItem, List<AudioSubtitle> audioSubtitles) {

		mediaItem = service.save(mediaItem, audioSubtitles);

		mediaLibraryTree.refreshNode(mediaLibraryTree.getSelectedFolderNodeKey());

		new Notification("Saved successful", Notification.Type.HUMANIZED_MESSAGE).show(Page.getCurrent());

		return mediaItem;
	}

	public void MultiMediaAdminView() {
		vl.setMargin(true);

		Button newBtn = new Button("New");
		// newBtn.addClickListener(e -> {
		// try {
		// buildAudio();
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// });

		newBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					buildAudio();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		resourceVtt = new StreamResource(new StreamSource() {

			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				// TODO Auto-generated method stub
				return getClass().getResourceAsStream("/scorpions_send_me_an_angel.txt.vtt");
			}

		}, "scorpions_send_me_an_angel.txt.vtt");

		resourceMp3 = new StreamResource(new StreamSource() {

			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				// TODO Auto-generated method stub
				return getClass().getResourceAsStream("/scorpions_send_me_an_angel.mp3");
			}

		}, "scorpions_send_me_an_angel.mp3");

		linkVtt = new Link("vtt", resourceVtt);
		linkMp3 = new Link("mp3", resourceMp3);

		vl.addComponent(newBtn);
		vl.addComponent(linkMp3);
		vl.addComponent(linkVtt);

		setCompositionRoot(vl);
	}

	public void buildAudio() throws URISyntaxException {

		String protocol = UI.getCurrent().getPage().getLocation().getScheme();
		String currentUrl = UI.getCurrent().getPage().getLocation().getAuthority();

		String mp3Url = protocol + "://" + currentUrl + "/vaadinServlet/APP/connector/" + linkMp3.getUI().getUIId()
				+ "/" + linkMp3.getConnectorId() + "/href/" + ((StreamResource) resourceMp3).getFilename();

		String vttUrl = protocol + "://" + currentUrl + "/vaadinServlet/APP/connector/" + linkVtt.getUI().getUIId()
				+ "/" + linkVtt.getConnectorId() + "/href/" + ((StreamResource) resourceVtt).getFilename();

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

		sample.setTemplateContents("<audio id='audio' preload='auto' controls> " + " <source src='" + mp3Url
				+ "' type='audio/mpeg'>" + " <track id='trk' kind='subtitles' srclang='en' src='" + vttUrl
				+ "' default  /></audio>" + " <br/><div id='lyrics'></div><br/><div id='timeUp'></div><br/>");

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
		// String basepath =
		// VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		// URL url =
		// getClass().getResource("/scorpions_send_me_an_angel.txt.vtt");
		// FileResource resource = new FileResource(new File(url.toURI()));
		// // Resource r = new StreamResource();
		// System.out.println("resource:" + resource);
		// ExternalResource r = new ExternalResource(url);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}