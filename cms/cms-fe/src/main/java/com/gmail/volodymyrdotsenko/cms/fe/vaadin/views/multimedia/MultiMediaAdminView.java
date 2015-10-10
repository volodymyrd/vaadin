package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.InputStream;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.gmail.volodymyrdotsenko.cms.be.domain.media.Folder;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.FolderRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.MadiaItemRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.MediaItem;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.Sections;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.EmbeddedView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinService;
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

	private final VerticalLayout mainContainer = new VerticalLayout();
	private final VerticalLayout contentLayout = new VerticalLayout();
	private final MultiMediaMenuBar menu = new MultiMediaMenuBar(this);
	private final MediaLibraryTree mediaLibraryTree;
	private EmbeddedView currentEmbeddedView;

	private final ApplicationContext applicationContext;

	private final FolderRepository folderRepo;
	private final MadiaItemRepository mediaItemRepo;
	// private final MadiaItemContentRepository mediaItemContentRepo;

	@Autowired
	public MultiMediaAdminView(ApplicationContext applicationContext, FolderRepository folderRepo,
			MadiaItemRepository mediaItemRepo) {

		this.applicationContext = applicationContext;
		this.folderRepo = folderRepo;
		this.mediaItemRepo = mediaItemRepo;

		HorizontalSplitPanel spliter = new HorizontalSplitPanel();
		spliter.setSizeFull();
		spliter.setSplitPosition(20, Unit.PERCENTAGE);

		mediaLibraryTree = new MediaLibraryTree(applicationContext);
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
		return folderRepo.getOne(1L);
	}

	public void save(MediaItem audioItem) {
		audioItem.getContent().getContent();
		mediaItemRepo.save(audioItem);
		mediaLibraryTree.refreshNode(mediaLibraryTree.getSelectedFolderNodeKey());

		new Notification("Saved successful", Notification.Type.HUMANIZED_MESSAGE).show(Page.getCurrent());
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
		System.out.println(mp3Url);

		String vttUrl = protocol + "://" + currentUrl + "/vaadinServlet/APP/connector/" + linkVtt.getUI().getUIId()
				+ "/" + linkVtt.getConnectorId() + "/href/" + ((StreamResource) resourceVtt).getFilename();
		System.out.println(vttUrl);

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
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

		System.out.println("basepath:" + basepath);

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