package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItem;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.MediaItemContent;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.TextItem;
import com.gmail.volodymyrdotsenko.cms.be.services.MultiMediaService;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.EmbeddedView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView.SuccessHandler;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonArray;

public class AdioItemView extends VerticalLayout implements EmbeddedView, SuccessHandler {

	private static final long serialVersionUID = 1L;

	private final MultiMediaAdminView mainView;

	private final HorizontalLayout topLayot = new HorizontalLayout();
	private final HorizontalLayout bottomLayot = new HorizontalLayout();
	private final TabSheet tabs = new TabSheet();
	private final UploadProgressView upload = new UploadProgressView(this);
	private AudioInfoForm audioInfoForm;
	private final Link audioFileLink = new Link();

	private AudioItem item;

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

		lang = new ComboBox("Language", mainView.getLangSet());

		if (adioItemId == null) {
			item = new AudioItem();
			item.setFolder(mainView.getSlectedFolder());
		} else {
			MultiMediaService service = mainView.getApplicationContext().getBean(MultiMediaService.class);

			item = service.getAudioItemWithContent(adioItemId);
			Map<Language, TextItem> texts = item.getTextItem();
			for (Map.Entry<Language, TextItem> e : texts.entrySet()) {
				lang.select(e.getKey().getCode());
				text.setValue(e.getValue().getText());

				break;
			}
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
		if (item.getContent() != null)
			refreshTop(new CustomLayout());

		return topLayot;
	}

	private double timeTrack;

	private void refreshTop(CustomLayout topCl) {

		topLayot.setSizeFull();
		topLayot.setHeight(40, Unit.PIXELS);
		topLayot.addComponent(topCl);

		String protocol = UI.getCurrent().getPage().getLocation().getScheme();
		String currentUrl = UI.getCurrent().getPage().getLocation().getAuthority();

		String mp3Url = protocol + "://" + currentUrl + "/content/" + item.getId();

		String vttUrl = "";

		// String timeUpId = UUID.randomUUID().toString();

		String js = ""// "//<![CDATA[ "
				+ " var lyrics = document.getElementById('lyrics'); "
				+ " var audio = document.getElementById('audio'); " + " var track = document.getElementById('trk'); "
				// + " var timeUp = document.getElementById('timeUp'); "
				// + " var timeUp = document.getElementById('" + timeUpId + "');
				// "
				+ " var textTrack = track.track; " + " track.addEventListener('cuechange', cueChange, false); "
				+ " audio.addEventListener('timeupdate', timeUpdate, false); "
				+ " function cueChange(){ var cues = textTrack.activeCues; if (cues.length > 0){ lyrics.innerHTML = cues[0].text;}} "
				+ " function timeUpdate(){ /*timeUp.innerHTML=audio.currentTime;*/ com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia.timeUpdate(audio.currentTime);} ";
		// + " //]]>";

		topCl.setTemplateContents("<div style='margin: 10px 10px 0px 10%; width: 100%;'>"
				+ "<audio id='audio' controls> " + " <source src='" + mp3Url + "' type='audio/mpeg'>"
				+ " <track id='trk' kind='subtitles' srclang='en' src='" + vttUrl + "' default  /></audio>"
				+ "<div id='timeUp' style='width:20%;float: right'></div><div id='lyrics' style='width:30%;float: right'></div></div>");

		// timeTrack = new CustomLayout();
		// timeTrack.setId(timeUpId);
		// TextField trackPosChanger = new TextField();
		// trackPosChanger.setId(timeUpId);
		// trackPosChanger.addTextChangeListener(new TextChangeListener() {
		//
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void textChange(TextChangeEvent event) {
		// try {
		// double v = Double.valueOf(event.getText());
		// String js1 = " var audio = document.getElementById('audio'); " + "
		// audio.currentTime=" + v + ";";
		// JavaScript.getCurrent().execute(js1);
		// } catch (NumberFormatException ex) {
		//
		// }
		// }
		// });

		// topLayot.addComponent(timeTrack);

		JavaScript.getCurrent().execute(js);
	}

	private Component buildBottom() {
		bottomLayot.setSizeFull();
		// bottomLayot.setMargin(true);

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
				if (audioInfoForm.isValid() && lang.isValid() && item != null && item.getContent() != null) {

					Map<Language, TextItem> textItem = new HashMap<>();

					textItem.put(mainView.getLangRepo().findOne((String) lang.getValue()),
							new TextItem(text.getValue()));

					item.setTextItem(textItem);

					item = (AudioItem) mainView.save(item);

					if (audioFileLink.getResource() instanceof FileResource) {
						File f = ((FileResource) audioFileLink.getResource()).getSourceFile();
						if (f != null)
							try {
								Files.deleteIfExists(f.toPath());
							} catch (IOException e) {
								e.printStackTrace();
							}

						buildAudioLink();
						refreshTop(new CustomLayout());
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

	private final ComboBox lang;
	private final TextArea text = new TextArea("Text");
	private final TwinColSelect select = new TwinColSelect();
	private final ListSelect select1 = new ListSelect();
	private final TimeLabelTextField start = new TimeLabelTextField("Start");
	private final TimeLabelTextField end = new TimeLabelTextField("End");

	private Component buildTab2() {
		VerticalLayout v = new VerticalLayout();
		HorizontalLayout h1 = new HorizontalLayout();
		HorizontalLayout h2 = new HorizontalLayout();
		v.setMargin(new MarginInfo(true, true, false, true));
		v.setSizeFull();

		lang.setNullSelectionAllowed(false);
		lang.setRequired(true);
		h1.addComponent(lang);
		h1.addComponent(h2);
		h1.setSizeFull();
		h1.setComponentAlignment(h2, Alignment.MIDDLE_RIGHT);
		h1.setMargin(new MarginInfo(false, false, true, false));
		h2.setSizeFull();
		// start.setWidth("50%");
		h2.addComponent(start);
		h2.setComponentAlignment(start, Alignment.MIDDLE_RIGHT);
		// end.setWidth("50%");
		h2.addComponent(end);
		h2.setComponentAlignment(end, Alignment.MIDDLE_RIGHT);
		v.addComponent(h1);

		lang.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("select lang " + event.getProperty().getValue());
			}
		});

		// GridLayout h = new GridLayout();
		HorizontalSplitPanel h = new HorizontalSplitPanel();
		h.setSizeFull();
		h.setHeight(300, Unit.PIXELS);
		;
		h.setSplitPosition(35, Unit.PERCENTAGE);
		// h.setRows(1);
		// h.setColumns(2);
		// h.setColumnExpandRatio(0, 1);
		// h.setColumnExpandRatio(1, 2);
		// h.setMargin(new MarginInfo(true, false, false, false));
		v.addComponent(h);

		// text.setHeight(350, Unit.PIXELS);
		text.setSizeFull();
		HorizontalLayout h31 = new HorizontalLayout();
		h31.setSizeFull();
		h31.setMargin(new MarginInfo(false, true, false, false));
		h.addComponent(h31);
		h31.addComponent(text);

		select.setSizeFull();
		HorizontalLayout h32 = new HorizontalLayout();
		h32.setSizeFull();
		h32.setMargin(new MarginInfo(false, false, false, true));
		h32.addComponent(select);
		h.addComponent(h32);
		select.setRows(10);
		select.setNullSelectionAllowed(true);
		select.setMultiSelect(true);
		select.setImmediate(true);
		select.setLeftColumnCaption("Presubtitle");
		select.setRightColumnCaption("Subtitle");

		if (text.getValue() != null && !text.getValue().isEmpty()) {
			updateSelect(text.getValue());
		}

		text.addTextChangeListener(new TextChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {
				updateSelect(event.getText());
			}
		});

		JavaScript.getCurrent().addFunction("com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia.timeUpdate",
				new JavaScriptFunction() {

					private static final long serialVersionUID = 1L;

					@Override
					public void call(JsonArray arguments) {
						System.out.println(arguments.asString());
						timeTrack = arguments.asNumber();
					}
				});

		select1.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {

			}
		});

		select.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				/// System.out.println(event.getProperty().getValue());
				// timeTrack.markAsDirty();
				// System.out.println(timeTrack.getData());

				// String js1 = " var audio = document.getElementById('audio');
				// console.log(audio.currentTime);";
				// JavaScript.getCurrent().execute(js1);
				Notification.show("Value changed:", String.valueOf(event.getProperty().getValue()),
						Type.TRAY_NOTIFICATION);
			}

		});

		return v;
	}

	private void updateSelect(String text) {
		select.removeAllItems();
		String[] txt = text.split("[\\n\\r]+");
		for (String s : txt)
			select.addItem(s.trim());
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