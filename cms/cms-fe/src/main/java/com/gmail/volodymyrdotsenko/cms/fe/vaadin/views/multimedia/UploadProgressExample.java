package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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

public class UploadProgressExample extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final Upload upload;

	public UploadProgressExample() {
		LineBreakCounter lineBreakCounter = new LineBreakCounter();
		lineBreakCounter.setSlow(true);

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
		private final Label result = new Label();
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

			setResizable(false);
			setDraggable(false);

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

			result.setCaption("Line breaks counted");
			l.addComponent(result);

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
			result.setValue(counter.getLineBreakCount() + " (counting...)");
		}

		@Override
		public void uploadSucceeded(final SucceededEvent event) {
			result.setValue(counter.getLineBreakCount() + " (total)");
		}

		@Override
		public void uploadFailed(final FailedEvent event) {
			result.setValue(counter.getLineBreakCount() + " (counting interrupted at "
					+ Math.round(100 * progressBar.getValue()) + "%)");
		}
	}

	private static class LineBreakCounter implements Receiver {

		private static final long serialVersionUID = 1L;

		private int counter;
		private long total;
		private boolean sleep;

		/**
		 * return an OutputStream that simply counts lineends
		 */
		@Override
		public OutputStream receiveUpload(final String filename, final String MIMEType) {
			counter = 0;
			total = 0;
			return new OutputStream() {
				private static final int searchedByte = '\n';

				@Override
				public void write(final int b) throws IOException {
					
					if (b == searchedByte) {
						counter++;
					}

					total++;
					
					if (sleep && total % 1000 == 0) {
						try {
							Thread.sleep(100);
						} catch (final InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}

		public int getLineBreakCount() {
			return counter;
		}

		public void setSlow(final boolean value) {
			sleep = value;
		}
	}
}
