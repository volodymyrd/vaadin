package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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

public class UploadProgressView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final Upload upload;

	public UploadProgressView(SuccessHandler sh) {
		Receiver receiver = new ReceiverImpl();

		upload = new Upload(null, receiver);
		final UploadInfoWindow uploadInfoWindow = new UploadInfoWindow(upload, receiver, sh);

		upload.setImmediate(true);
		upload.setButtonCaption("Upload");
		upload.addStartedListener(new StartedListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void uploadStarted(final StartedEvent event) {
				if (isEmpty(event.getFilename())) {
					new Notification("Choose file, firstly", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
					return;
				}

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

		// upload.addChangeListener(new ChangeListener() {
		//
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void filenameChanged(ChangeEvent event) {
		// if (!isEmpty(event.getFilename()))
		// upload.setButtonCaption("Upload");
		// }
		// });

		addComponent(upload);
	}

	// @StyleSheet("uploadexample.css")
	private static class UploadInfoWindow extends Window implements Upload.StartedListener, Upload.ProgressListener,
			Upload.FailedListener, Upload.SucceededListener, Upload.FinishedListener {

		private static final long serialVersionUID = 1L;

		private final Label state = new Label();
		private final Label fileName = new Label();
		private final Label textualProgress = new Label();
		private final SuccessHandler sh;
		private final Receiver receiver;

		private final ProgressBar progressBar = new ProgressBar();
		private final Button cancelButton;

		public UploadInfoWindow(final Upload upload, final Receiver receiver, final SuccessHandler sh) {
			super("Status");

			this.sh = sh;
			this.receiver = receiver;

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
			if (isEmpty(event.getFilename()))
				return;

			state.setValue("Idle");
			progressBar.setVisible(false);
			textualProgress.setVisible(false);
			cancelButton.setVisible(false);
		}

		@Override
		public void uploadStarted(final StartedEvent event) {
			if (isEmpty(event.getFilename()))
				return;

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
			new Notification("File loaded successful ", Notification.Type.HUMANIZED_MESSAGE).show(Page.getCurrent());
			sh.fireLoadingSuccessEvent(((ReceiverImpl) receiver).getFile());
		}

		@Override
		public void uploadFailed(final FailedEvent event) {
			if (isEmpty(event.getFilename()))
				return;

			new Notification("File loading is failed", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
		}
	}

	private static class ReceiverImpl implements Receiver {

		private static final long serialVersionUID = 1L;

		private File file;

		@Override
		public OutputStream receiveUpload(final String fileName, final String MIMEType) {

			if (!isEmpty(fileName)) {
				// Create upload stream
				FileOutputStream fos = null; // Stream to write to
				try {
					// Open the file for writing.
					file = new File("/tmp/" + fileName);
					fos = new FileOutputStream(file);
				} catch (final java.io.FileNotFoundException e) {
					new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
							.show(Page.getCurrent());
					return null;
				}

				return fos; // Return the output stream to write to
			}

			return null;
		}

		public File getFile() {
			return file;
		}
	}

	public interface SuccessHandler {
		void fireLoadingSuccessEvent(File file);
	}

	private static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}
}