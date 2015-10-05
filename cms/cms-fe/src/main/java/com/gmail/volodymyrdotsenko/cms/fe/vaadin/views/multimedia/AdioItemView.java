package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;

import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

public class AdioItemView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private Label state = new Label();
	private Label result = new Label();
	private Label fileName = new Label();
	private Label textualProgress = new Label();

	private ProgressIndicator pi = new ProgressIndicator();

	private LineBreakCounter counter = new LineBreakCounter();

	private Upload upload = new Upload(null, counter);

	public AdioItemView() {
		setMargin(true);

        setSpacing(true);

        addComponent(new Label(
                "Upload a file and we'll count the number of line break characters (\\n) found in it."));

        // make analyzing start immediatedly when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption("Upload File");
        addComponent(upload);

        CheckBox handBrake = new CheckBox("Simulate slow server");
        handBrake.setValue(true);
        counter.setSlow(true);
        handBrake
                .setDescription("Sleep for 100ms after each kilobyte to simulate slower processing/bandwidth. This is to show progress indicator even with rather small files.");
        handBrake.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                counter.setSlow(event.getButton().booleanValue());
            }
        });

        final Button cancelProcessing = new Button("Cancel");
        cancelProcessing.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                upload.interruptUpload();
            }
        });
        cancelProcessing.setVisible(false);
        cancelProcessing.setStyleName("small");

        handBrake.setImmediate(true);

        addComponent(handBrake);

        Panel p = new Panel("Status");
        p.setSizeUndefined();
        FormLayout l = new FormLayout();
        l.setMargin(true);
        p.setContent(l);
        HorizontalLayout stateLayout = new HorizontalLayout();
        stateLayout.setSpacing(true);
        stateLayout.addComponent(state);
        stateLayout.addComponent(cancelProcessing);
        stateLayout.setCaption("Current state");
        state.setValue("Idle");
        l.addComponent(stateLayout);
        fileName.setCaption("File name");
        l.addComponent(fileName);
        result.setCaption("Line breaks counted");
        l.addComponent(result);
        pi.setCaption("Progress");
        pi.setVisible(false);
        l.addComponent(pi);
        textualProgress.setVisible(false);
        l.addComponent(textualProgress);

        addComponent(p);

        upload.addListener(new StartedListener() {
            public void uploadStarted(StartedEvent event) {
                // this method gets called immediatedly after upload is
                // started
                pi.setValue(0f);
                pi.setVisible(true);
                pi.setPollingInterval(500); // hit server frequantly to get
                textualProgress.setVisible(true);
                // updates to client
                state.setValue("Uploading");
                fileName.setValue(event.getFilename());

                cancelProcessing.setVisible(true);
            }
        });

        upload.addListener(new Upload.ProgressListener() {
            public void updateProgress(long readBytes, long contentLength) {
                // this method gets called several times during the update
                pi.setValue(new Float(readBytes / (float) contentLength));
                textualProgress.setValue("Processed " + readBytes
                        + " bytes of " + contentLength);
                result.setValue(counter.getLineBreakCount() + " (counting...)");
            }

        });

        upload.addListener(new SucceededListener() {
            public void uploadSucceeded(SucceededEvent event) {
                result.setValue(counter.getLineBreakCount() + " (total)");
            }
        });

        upload.addListener(new FailedListener() {
            public void uploadFailed(FailedEvent event) {
                result.setValue(counter.getLineBreakCount()
                        + " (counting interrupted at "
                        + Math.round(100 * (Float) pi.getValue()) + "%)");
            }
        });

        upload.addListener(new FinishedListener() {
            public void uploadFinished(FinishedEvent event) {
                state.setValue("Idle");
                pi.setVisible(false);
                textualProgress.setVisible(false);
                cancelProcessing.setVisible(false);
            }
        });
	}

	public static class LineBreakCounter implements Receiver {
		
		private static final long serialVersionUID = 1L;
		
		private String fileName;
		private String mtype;

		private int counter;
		private int total;
		private boolean sleep;

		/**
		 * return an OutputStream that simply counts lineends
		 */
		public OutputStream receiveUpload(String filename, String MIMEType) {
			counter = 0;
			total = 0;
			fileName = filename;
			mtype = MIMEType;
			return new OutputStream() {
				private static final int searchedByte = '\n';

				@Override
				public void write(int b) throws IOException {
					total++;
					if (b == searchedByte) {
						counter++;
					}
					if (sleep && total % 1000 == 0) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
		}

		public String getFileName() {
			return fileName;
		}

		public String getMimeType() {
			return mtype;
		}

		public int getLineBreakCount() {
			return counter;
		}

		public void setSlow(boolean value) {
			sleep = value;
		}

	}
}