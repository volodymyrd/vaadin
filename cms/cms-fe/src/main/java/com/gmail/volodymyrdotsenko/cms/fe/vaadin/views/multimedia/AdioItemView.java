package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.File;

import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItem;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.UploadProgressView.SuccessHandler;
import com.mpatric.mp3agic.Mp3File;
import com.vaadin.ui.VerticalLayout;

public class AdioItemView extends VerticalLayout implements SuccessHandler {

	private static final long serialVersionUID = 1L;

	private final UploadProgressView upload = new UploadProgressView(this);
	private AudioItem item = new AudioItem();

	public AdioItemView() {

		addComponent(upload);
	}

	@Override
	public void fireLoadingSuccessEvent(File file) {
		try {
			Mp3File mp3file = new Mp3File(file);

			System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
			System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
			System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
			System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
			System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
			System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}