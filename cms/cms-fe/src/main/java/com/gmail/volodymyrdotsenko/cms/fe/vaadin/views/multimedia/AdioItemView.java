package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItem;
import com.mpatric.mp3agic.Mp3File;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AdioItemView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final Upload upload;
	private AudioItem item = new AudioItem();

	public AdioItemView() {
		LineBreakCounter lineBreakCounter = new LineBreakCounter();
		// lineBreakCounter.setSlow(true);

		upload = new Upload(null, lineBreakCounter);
		final UploadInfoWindow uploadInfoWindow = new UploadInfoWindow(upload, lineBreakCounter);

		upload.setImmediate(false);
		upload.setButtonCaption("Upload File");
		upload.addStartedListener(new StartedListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void uploadStarted(final StartedEvent event) {
				if (uploadInfoWindow.getParent() == null) {
					UI.getCurrent().addWindow(uploadInfoWindow);
				}
				uploadInfoWindow.setClosable(false);
			}
		});

		upload.addFinishedListener(new Upload.FinishedListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void uploadFinished(final FinishedEvent event) {
				uploadInfoWindow.setClosable(true);
			}
		});

		addComponent(upload);
	}

	// @StyleSheet("uploadexample.css")
	private static class UploadInfoWindow extends Window implements Upload.StartedListener, Upload.ProgressListener,
			Upload.FailedListener, Upload.SucceededListener, Upload.FinishedListener {

		private static final long serialVersionUID = 1L;

		private final Label state = new Label();
		private final Label fileName = new Label();
		private final Label textualProgress = new Label();

		private final ProgressBar progressBar = new ProgressBar();
		private final Button cancelButton;
		private final LineBreakCounter counter;

		public UploadInfoWindow(final Upload upload, final LineBreakCounter lineBreakCounter) {
			super("Status");
			this.counter = lineBreakCounter;

			setWidth(350, Unit.PIXELS);

			addStyleName("upload-info");

			setResizable(true);
			setDraggable(true);

			final FormLayout l = new FormLayout();
			setContent(l);
			l.setMargin(true);

			final HorizontalLayout stateLayout = new HorizontalLayout();
			stateLayout.setSpacing(true);
			stateLayout.addComponent(state);

			cancelButton = new Button("Cancel");
			cancelButton.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					upload.interruptUpload();
				}
			});
			cancelButton.setVisible(false);
			cancelButton.setStyleName("small");
			stateLayout.addComponent(cancelButton);

			stateLayout.setCaption("Current state");
			state.setValue("Idle");
			l.addComponent(stateLayout);

			fileName.setCaption("File name");
			l.addComponent(fileName);

			progressBar.setCaption("Progress");
			progressBar.setVisible(false);
			l.addComponent(progressBar);

			textualProgress.setVisible(false);
			l.addComponent(textualProgress);

			upload.addStartedListener(this);
			upload.addProgressListener(this);
			upload.addFailedListener(this);
			upload.addSucceededListener(this);
			upload.addFinishedListener(this);

		}

		@Override
		public void uploadFinished(final FinishedEvent event) {
			state.setValue("Idle");
			progressBar.setVisible(false);
			textualProgress.setVisible(false);
			cancelButton.setVisible(false);
		}

		@Override
		public void uploadStarted(final StartedEvent event) {
			// this method gets called immediately after upload is started
			progressBar.setValue(0f);
			progressBar.setVisible(true);
			UI.getCurrent().setPollInterval(500);
			textualProgress.setVisible(true);
			// updates to client
			state.setValue("Uploading");
			fileName.setValue(event.getFilename());

			cancelButton.setVisible(true);
		}

		@Override
		public void updateProgress(final long readBytes, final long contentLength) {
			// this method gets called several times during the update
			progressBar.setValue(new Float(readBytes / (float) contentLength));
			textualProgress.setValue("Processed " + readBytes + " bytes of " + contentLength);
			// result.setValue(counter.getLineBreakCount() + " (counting...)");
		}

		@Override
		public void uploadSucceeded(final SucceededEvent event) {
			// result.setValue(counter.getLineBreakCount() + " (total)");

			try {
				Mp3File mp3file = new Mp3File(counter.getFile());

				System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
				System.out
						.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
				System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
				System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
				System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
				System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void uploadFailed(final FailedEvent event) {
			// result.setValue(counter.getLineBreakCount() + " (counting
			// interrupted at "
			// + Math.round(100 * progressBar.getValue()) + "%)");
		}
	}

	private static class LineBreakCounter implements Receiver {

		private static final long serialVersionUID = 1L;

		private File file;

		@Override
		public OutputStream receiveUpload(final String filename, final String MIMEType) {

			// Create upload stream
			FileOutputStream fos = null; // Stream to write to
			try {
				// Open the file for writing.
				file = new File("/tmp/" + filename);
				fos = new FileOutputStream(file);
			} catch (final java.io.FileNotFoundException e) {
				new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
				return null;
			}

			return fos; // Return the output stream to write to
		}

		public File getFile() {
			return file;
		}
	}
}