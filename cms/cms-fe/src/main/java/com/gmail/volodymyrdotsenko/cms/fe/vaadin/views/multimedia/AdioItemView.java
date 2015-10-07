package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;

import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItem;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.EmbeddedView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView.SuccessHandler;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
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

			VerticalLayout vl = new VerticalLayout(
					new MFormLayout(name, lengthView, track, artist, title, album, year, genre, genreDescription, comment)
							.withMargin(new MarginInfo(false, false, false, true)));

			return vl;
		}
	}

	public AdioItemView(MultiMediaAdminView mainView) {
		this.mainView = mainView;
		
		item = new AudioItem();
		item.setFolder(mainView.getSlectedFolder());

		setSizeFull();

		addComponent(buildTop());
		addComponent(buildTab());
		addComponent(buildBottom());
	}

	private Component buildTop() {

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
				mainView.save(item);
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

	private Component buildTab() {
		tabs.setSizeFull();
		tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		tabs.addTab(buildTab1(), "File info");
		tabs.addTab(buildTab2(), "Subtitle");

		return tabs;
	}

	private Component buildTab1() {
		HorizontalLayout h = new HorizontalLayout();
		// h.setSizeFull();
		h.setMargin(true);

		h.addComponent(upload);

		audioInfoForm = new AudioInfoForm();
		h.addComponent(audioInfoForm);

		return h;
	}

	private Component buildTab2() {
		HorizontalLayout h = new HorizontalLayout();

		return h;
	}

	@Override
	public void fireLoadingSuccessEvent(File file) {
		try {
			Mp3File mp3file = new Mp3File(file);

			item.setLength(mp3file.getLengthInSeconds());
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

			audioInfoForm.refresh();

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