/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gmail.volodymyrdotsenko.cms.fe.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.security.VaadinSecurity;

/**
 * UI for the login screen.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringUI(path = "/login")
@Theme(ValoTheme.THEME_NAME)
public class LoginUI extends UI {

	private static final long serialVersionUID = 1L;

	private Set<String> langSet = Stream.of("en", "ru").collect(Collectors.toSet());

	@Autowired
	VaadinSecurity vaadinSecurity;

	private TextField userName;

	private PasswordField passwordField;

	private CheckBox rememberMe;

	private Button login;

	private Label loginFailedLabel;
	private Label loggedOutLabel;
	private ComboBox lang = new ComboBox("Language", langSet);

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Vaadin Shared Security Demo Login");

		FormLayout loginForm = new FormLayout();
		loginForm.setSizeUndefined();

		loginForm.addComponent(userName = new TextField("Username"));
		userName.setRequired(true);
		loginForm.addComponent(passwordField = new PasswordField("Password"));
		passwordField.setRequired(true);
		loginForm.addComponent(rememberMe = new CheckBox("Remember me"));
		loginForm.addComponent(lang);
		lang.setRequired(true);
		lang.setNullSelectionAllowed(false);
		loginForm.addComponent(login = new Button("Login"));
		login.addStyleName(ValoTheme.BUTTON_PRIMARY);
		login.setDisableOnClick(true);
		login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		login.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				login();
			}
		});

		VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setSpacing(true);
		loginLayout.setSizeUndefined();

		if (request.getParameter("logout") != null) {
			loggedOutLabel = new Label("You have been logged out!");
			loggedOutLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
			loggedOutLabel.setSizeUndefined();
			loginLayout.addComponent(loggedOutLabel);
			loginLayout.setComponentAlignment(loggedOutLabel, Alignment.BOTTOM_CENTER);
		}

		loginLayout.addComponent(loginFailedLabel = new Label());
		loginLayout.setComponentAlignment(loginFailedLabel, Alignment.BOTTOM_CENTER);
		loginFailedLabel.setSizeUndefined();
		loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
		loginFailedLabel.setVisible(false);

		loginLayout.addComponent(loginForm);
		loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

		VerticalLayout rootLayout = new VerticalLayout(loginLayout);
		rootLayout.setSizeFull();
		rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
		setContent(rootLayout);
		setSizeFull();

		String currentLang = getLocale().getLanguage();
		if (langSet.contains(currentLang))
			lang.select(currentLang);
		else
			lang.select("en");
	}

	private void login() {
		try {
			vaadinSecurity.login(userName.getValue(), passwordField.getValue(), rememberMe.getValue());

			VaadinSession.getCurrent().setLocale(new Locale((String) lang.getValue()));
		} catch (AuthenticationException ex) {
			userName.focus();
			userName.selectAll();
			passwordField.setValue("");
			loginFailedLabel.setValue(String.format("Login failed: %s", ex.getMessage()));
			loginFailedLabel.setVisible(true);
			if (loggedOutLabel != null) {
				loggedOutLabel.setVisible(false);
			}
		} catch (Exception ex) {
			Notification.show("An unexpected error occurred", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
			LoggerFactory.getLogger(getClass()).error("Unexpected error while logging in", ex);
		} finally {
			login.setEnabled(true);
		}
	}
}