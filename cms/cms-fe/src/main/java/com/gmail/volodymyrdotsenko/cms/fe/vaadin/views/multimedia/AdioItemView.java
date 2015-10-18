package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItem;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioSubtitle;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.MediaItemContent;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.TextItem;
import com.gmail.volodymyrdotsenko.cms.be.services.MultiMediaService;
import com.gmail.volodymyrdotsenko.cms.be.utils.Utils;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.EmbeddedView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView.SuccessHandler;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
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
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
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

	private final static String containerPropertyNum = "num";
	private final static String containerPropertyLang = "lang";
	private final static String containerPropertyText = "text";
	private final static String containerPropertyStart = "start";
	private final static String containerPropertyEnd = "end";
	private final static String containerPropertyTools = "tools";

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

		tabs.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

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

			// subtitles = service.getAudioSubtitles(item);
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

		String mp3Url = protocol + "://" + currentUrl + "/multimedia/audio/content/" + item.getId();

		String vttUrl = protocol + "://" + currentUrl + "/multimedia/audio/subtitle/" + item.getId();

		// String timeUpId = UUID.randomUUID().toString();

		String js = ""// "//<![CDATA[ "
				+ " var lyrics = document.getElementById('lyrics'); "
				+ " var audio = document.getElementById('audio'); " + " var track = document.getElementById('trk'); "
				// + " var timeUp = document.getElementById('timeUp'); "
				// + " var timeUp = document.getElementById('" + timeUpId + "');
				// "
				+ " var textTrack = track.track; " + " track.addEventListener('cuechange', cueChange, false); "
				+ " audio.addEventListener('timeupdate', timeUpdate, false); "
				+ " function cueChange(){ var cues = textTrack.activeCues; if (cues.length > 0){ console.log(cues[0].text); lyrics.innerHTML = cues[0].text;}} "
				+ " function timeUpdate(){ /*timeUp.innerHTML=audio.currentTime;*/ com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia.timeUpdate(audio.currentTime);} ";
		// + " //]]>";

		topCl.setTemplateContents("<div style='margin: 10px 10px 0px 10px; width: 100%;'>"
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

					List<AudioSubtitle> subtitles = new ArrayList<>(table.getItemIds().size());
					table.getItemIds().forEach(e -> {
						Item i = table.getItem(e);
						Language l = mainView.getLangRepo()
								.findOne((String) i.getItemProperty(containerPropertyLang).getValue());
						AudioSubtitle as = new AudioSubtitle(l, item,
								(Integer) i.getItemProperty(containerPropertyNum).getValue());

						as.setEnd(Utils.vttToDate((String) i.getItemProperty(containerPropertyEnd).getValue()));
						as.setStart(Utils.vttToDate((String) i.getItemProperty(containerPropertyStart).getValue()));
						as.setText((String) i.getItemProperty(containerPropertyText).getValue());

						subtitles.add(as);
					});

					item = (AudioItem) mainView.save(item, subtitles);

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
	private final Table table = new Table();
	private final Button addText = new Button("Add");

	private Component buildTab2() {
		HorizontalSplitPanel h = new HorizontalSplitPanel();
		h.setSizeFull();
		h.setSplitPosition(35, Unit.PERCENTAGE);
		VerticalLayout v = new VerticalLayout();
		v.setSizeFull();
		v.setMargin(new MarginInfo(false, true, true, true));
		h.addComponent(v);

		lang.setNullSelectionAllowed(false);
		lang.setRequired(true);
		v.addComponent(lang);

		lang.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				// System.out.println("select lang " +
				// event.getProperty().getValue());
			}
		});

		class TakeTimeClickListener implements ClickListener {

			private static final long serialVersionUID = 1L;
			private AudioSubtitle as;

			public TakeTimeClickListener(AudioSubtitle as) {
				this.as = as;
			}

			@Override
			public void buttonClick(ClickEvent event) {
				Item it = table.getItem(as.getId().getOrderNum());
				it.getItemProperty("Start").setValue(Utils.convertToVtt(timeTrack));

				if (as.getId().getOrderNum() > 0)
					it = table.getItem(as.getId().getOrderNum() - 1);
				it.getItemProperty("End").setValue(Utils.convertToVtt(timeTrack));
			}
		}

		text.setSizeFull();
		text.setHeight(250, Unit.PIXELS);
		text.setImmediate(true);
		v.addComponent(text);

		addText.setEnabled(false);
		addText.setImmediate(true);
		addText.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String v = text.getValue();
				if (v == null || v.isEmpty())
					return;

				String[] s = v.split("[\\n\\r]+");
				Language l = mainView.getLangRepo().findOne((String) lang.getValue());

				Set<Object> deletedIds = new HashSet<>();
				table.getItemIds().forEach(e -> {
					if (lang.getValue().equals(table.getItem(e).getItemProperty(containerPropertyLang).getValue())) {
						deletedIds.add(e);
					}
				});
				deletedIds.forEach(e -> table.removeItem(e));

				for (int i = 0; i < s.length; i++) {
					Button b = new Button("Take");
					Object obj[] = { i, lang.getValue(), s[i], "", "", b };

					AudioSubtitle as = new AudioSubtitle(l, item, i);
					b.addClickListener(new TakeTimeClickListener(as));
					as.setText(s[i]);

					table.addItem(obj, i);
				}
			}
		});

		v.addComponent(addText);

		text.addTextChangeListener(new TextChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {
				if (lang.getValue() != null && event.getText() != null && !event.getText().isEmpty())
					addText.setEnabled(true);
				else
					addText.setEnabled(false);
			}
		});

		JavaScript.getCurrent().addFunction("com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia.timeUpdate",
				new JavaScriptFunction() {

					private static final long serialVersionUID = 1L;

					@Override
					public void call(JsonArray arguments) {
						timeTrack = arguments.asNumber();
					}
				});

		VerticalLayout v1 = new VerticalLayout();
		v1.setSizeFull();
		v1.setMargin(true);
		h.addComponent(v1);
		table.setSizeFull();
		table.setSelectable(true);
		table.setHeight(300, Unit.PIXELS);
		table.addContainerProperty(containerPropertyNum, Integer.class, null);
		table.addContainerProperty(containerPropertyLang, String.class, null);
		table.addContainerProperty(containerPropertyText, String.class, null);
		table.addContainerProperty(containerPropertyStart, String.class, null);
		table.addContainerProperty(containerPropertyEnd, String.class, null);
		table.addContainerProperty(containerPropertyTools, Button.class, null);

		table.setStyleName("table");
		table.setColumnHeader(containerPropertyNum, "N");
		table.setSortEnabled(false);
		table.setEditable(true);
		table.setTableFieldFactory(new DefaultFieldFactory() {

			private static final long serialVersionUID = 1L;

			@Override
			public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
				if (propertyId.equals(containerPropertyLang) || propertyId.equals(containerPropertyText)
						|| propertyId.equals(containerPropertyNum) || propertyId.equals(containerPropertyTools))
					container.getContainerProperty(itemId, propertyId).setReadOnly(true);

				return super.createField(container, itemId, propertyId, uiContext);
			}
		});

		table.setColumnWidth(containerPropertyNum, 50);
		table.setColumnWidth(containerPropertyLang, 50);
		table.setColumnWidth(containerPropertyText, 250);
		// table.setColumnWidth("Tools", 50);

		mainView.getService().getAudioSubtitles(item).forEach(e -> {
			Button b = new Button("Take");
			b.addClickListener(new TakeTimeClickListener(e));

			Object obj[] = { e.getId().getOrderNum(), e.getId().getLang().getCode(), e.getText(),
					Utils.dateToVtt(e.getStart()), Utils.dateToVtt(e.getEnd()), b };

			table.addItem(obj, e.getId().getOrderNum());
		});

		v1.addComponent(table);

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