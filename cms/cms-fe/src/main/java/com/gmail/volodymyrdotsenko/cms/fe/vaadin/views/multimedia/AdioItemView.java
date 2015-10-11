package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;

import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItem;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.MediaItemContent;
import com.gmail.volodymyrdotsenko.cms.be.services.MultiMediaService;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.EmbeddedView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView.SuccessHandler;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class AdioItemView extends VerticalLayout implements EmbeddedView, SuccessHandler {

	private static final long serialVersionUID = 1L;

	private final MultiMediaAdminView mainView;

	private final HorizontalLayout topLayot = new HorizontalLayout();
	private final HorizontalLayout bottomLayot = new HorizontalLayout();
	private final TabSheet tabs = new TabSheet();
	private final UploadProgressView upload = new UploadProgressView(this);
	private AudioInfoForm audioInfoForm;
	private final Link audioFileLink = new Link();

	private final AudioItem item;

	private class AudioInfoForm extends AbstractForm<AudioItem> {
		private static final long serialVersionUID = 1L;

		private TextField lengthView = new MTextField("Length");
		private TextField name = new MTextField("Name");
		private TextField track = new MTextField("Track");
		private TextField artist = new MTextField("Artist");
		private TextField title = new MTextField("Title");
		private TextField album = new MTextField("Album");
		private TextField year = new MTextField("Year");
		private TextField genre = new MTextField("Genre");
		private TextField genreDescription = new MTextField("Genre Description");
		private TextField comment = new MTextField("Comment");

		public void refresh() {
			setEntity(item);
			if (item.getLength() != null) {
				lengthView.setReadOnly(false);
				lengthView.setValue(String.valueOf(item.getLength()));
				lengthView.setReadOnly(true);
			}
		}

		public AudioInfoForm() {
			setSizeUndefined();
			refresh();
		}

		@Override
		protected Component createContent() {
			HorizontalLayout vl = new HorizontalLayout(
					new MFormLayout(name, lengthView, track, artist, title)
							.withMargin(new MarginInfo(false, false, false, true)),
					new MFormLayout(album, year, genre, genreDescription, comment)
							.withMargin(new MarginInfo(false, false, false, true)));

			return vl;
		}
	}

	public AdioItemView(MultiMediaAdminView mainView, Long adioItemId) {
		this.mainView = mainView;

		if (adioItemId == null) {
			item = new AudioItem();
			item.setFolder(mainView.getSlectedFolder());
		} else {
			MultiMediaService service = mainView.getApplicationContext().getBean(MultiMediaService.class);

			item = service.getAudioItemWithContent(adioItemId);
		}

		setSizeFull();

		addComponent(buildTop());
		addComponent(buildTab());
		addComponent(buildBottom());

		if (adioItemId != null) {
			buildAudioLink();
		}
	}

	private Component buildTop() {
		if (item.getContent() == null)
			return topLayot;

		String protocol = UI.getCurrent().getPage().getLocation().getScheme();
		String currentUrl = UI.getCurrent().getPage().getLocation().getAuthority();

		String mp3Url = protocol + "://" + currentUrl + "/content/" + item.getId();

		String vttUrl = "";
		CustomLayout cl = new CustomLayout();
		topLayot.addComponent(cl);

		String js = ""// "//<![CDATA[ "
				+ " var lyrics = document.getElementById('lyrics'); "
				+ " var audio = document.getElementById('audio'); " + " var track = document.getElementById('trk'); "
				+ " var timeUp = document.getElementById('timeUp'); " + " var textTrack = track.track; "
				+ " track.addEventListener('cuechange', cueChange, false); "
				+ " audio.addEventListener('timeupdate', timeUpdate, false); "
				+ " function cueChange(){ var cues = textTrack.activeCues; if (cues.length > 0){ lyrics.innerHTML = cues[0].text;}} "
				+ " function timeUpdate(){ timeUp.innerHTML=audio.currentTime;} ";
		// + " //]]>";

		cl.setTemplateContents("<audio id='audio' preload='auto' controls> " + " <source src='" + mp3Url
				+ "' type='audio/mpeg'>" + " <track id='trk' kind='subtitles' srclang='en' src='" + vttUrl
				+ "' default  /></audio>" + " <br/><div id='lyrics'></div><br/><div id='timeUp'></div><br/>");

		JavaScript.getCurrent().execute(js);

		return topLayot;
	}

	private Component buildBottom() {
		bottomLayot.setSizeFull();
		bottomLayot.setMargin(true);

		HorizontalLayout h = new HorizontalLayout();
		h.setMargin(true);

		Button saveBtn = new Button("Save");
		saveBtn.setSizeFull();
		HorizontalLayout h1 = new HorizontalLayout(saveBtn);
		h1.setMargin(new MarginInfo(false, true, false, false));
		Button closeBtn = new Button("Close");
		HorizontalLayout h2 = new HorizontalLayout(closeBtn);
		h1.addComponent(saveBtn);
		h2.addComponent(closeBtn);
		h.addComponent(h1);
		h.addComponent(h2);
		bottomLayot.addComponent(h);
		bottomLayot.setComponentAlignment(h, Alignment.MIDDLE_LEFT);

		saveBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (audioInfoForm.isValid()) {
					mainView.save(item);

					if (audioFileLink.getResource() instanceof FileResource) {
						File f = ((FileResource) audioFileLink.getResource()).getSourceFile();
						if (f != null)
							try {
								Files.deleteIfExists(f.toPath());
							} catch (IOException e) {
								e.printStackTrace();
							}

						buildAudioLink();
					}

				} else
					new Notification("There are a few errors exist", Notification.Type.ERROR_MESSAGE)
							.show(Page.getCurrent());
			}
		});

		closeBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});

		return bottomLayot;
	}

	void buildAudioLink() {
		audioFileLink.setVisible(true);
		audioFileLink.setResource(new StreamResource(new StreamSource() {

			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(item.getContent().getContent());
			}

		}, item.getName() + ".mp3"));

		audioFileLink.setCaption(item.getName());
		audioFileLink.setTargetName("_blank");
	}

	private Component buildTab() {
		tabs.setSizeFull();
		tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		tabs.addTab(buildTab1(), "File info");
		tabs.addTab(buildTab2(), "Subtitle");

		return tabs;
	}

	private Component buildTab1() {
		VerticalLayout v = new VerticalLayout();
		v.setMargin(new MarginInfo(true, false, true, true));
		v.addComponent(audioFileLink);

		HorizontalLayout h = new HorizontalLayout();
		// h.setMargin(true);
		v.addComponent(h);
		h.addComponent(upload);
		audioInfoForm = new AudioInfoForm();
		h.addComponent(audioInfoForm);

		audioFileLink.setVisible(false);

		return v;
	}

	private Component buildTab2() {
		HorizontalLayout h = new HorizontalLayout();

		return h;
	}

	@Override
	public void fireLoadingSuccessEvent(String fileName, String MIMEType, long fileLength, File file) {
		try {
			Mp3File mp3file = new Mp3File(file);

			item.setLength(mp3file.getLengthInSeconds());
			item.setFileName(fileName);
			item.setFileLength(fileLength);
			item.setMIMEType(MIMEType);
			
			if (mp3file.hasId3v1Tag()) {
				ID3v1 id3v1Tag = mp3file.getId3v1Tag();
				item.setTrack(id3v1Tag.getTrack());
				item.setArtist(id3v1Tag.getArtist());
				item.setTitle(id3v1Tag.getTitle());
				item.setAlbum(id3v1Tag.getAlbum());
				item.setYear(id3v1Tag.getYear());
				item.setGenre(id3v1Tag.getGenre());
				item.setGenreDescription(id3v1Tag.getGenreDescription());
				item.setComment(id3v1Tag.getComment());
			}

			MediaItemContent content = item.getContent();
			if (content == null)
				content = new MediaItemContent();

			content.setContent(Files.readAllBytes(file.toPath()));
			item.setContent(content);

			audioInfoForm.refresh();

			audioFileLink.setVisible(true);
			audioFileLink.setResource(new FileResource(file));
			audioFileLink.setCaption(file.getName());
			audioFileLink.setTargetName("_blank");

			// String protocol =
			// UI.getCurrent().getPage().getLocation().getScheme();
			// String currentUrl =
			// UI.getCurrent().getPage().getLocation().getAuthority();

			// String mp3Url = protocol + "://" + currentUrl +
			// "/vaadinServlet/APP/connector/"
			// + audioFileLink.getUI().getUIId() + "/" +
			// audioFileLink.getConnectorId() + "/href/"
			// + file.getName();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		this.setVisible(false);
	}
}