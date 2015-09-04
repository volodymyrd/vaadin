package com.example.documentmanager;

import java.io.File;
import java.util.Arrays;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@SuppressWarnings("serial")
@Theme("documentmanager")
public class DocUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DocUI.class)
	public static class Servlet extends VaadinServlet {
	}

	FilesystemContainer docs = new FilesystemContainer(new File(
			"/home/dotsenko-vv/workspace/iplatform/svn/ebank/branches/iPlatform-0.2/ukraine/panteon/sapjcogen/build/docs/javadoc"));
	Table docList = new Table("Documents", docs);
	DocEditor docView = new DocEditor();

	private String[] themes = { "valo", "reindeer", "runo", "chameleon" };

	@Override
	protected void init(VaadinRequest request) {

		ComboBox themePicker = new ComboBox("Theme", Arrays.asList(themes));
		themePicker.setValue(getTheme());

		themePicker.addValueChangeListener(e -> {
			String theme = (String) e.getProperty().getValue();
			setTheme(theme);
		});

		VerticalSplitPanel vl = new VerticalSplitPanel();
		vl.addComponent(themePicker);
		vl.addComponent(docList);
		HorizontalSplitPanel hSplit = new HorizontalSplitPanel();

		hSplit.addComponent(vl);
		hSplit.addComponent(docView);
		setContent(hSplit);
		docList.setSizeFull();

		docList.addValueChangeListener(e -> {
			docView.setPropertyDataSource(new TextFileProperty((File) e.getProperty().getValue()));
		});
		docList.setImmediate(true);

		docList.setSelectable(true);
	}

}