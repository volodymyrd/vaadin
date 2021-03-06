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
package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views;

import com.gmail.volodymyrdotsenko.cms.fe.vaadin.Sections;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 * When the user logs in and there is no view to navigate to, this view will be
 * shown.
 */
@SpringView(name = "")
@SideBarItem(sectionId = Sections.MAIN_MENU, captionCode = "application.section.item.home", order = 0)
@FontAwesomeIcon(FontAwesome.HOME)
public class HomeView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	public HomeView() {
		setSpacing(true);
		setMargin(true);

		Label header = new Label("Welcome to CMS application");
		header.addStyleName(ValoTheme.LABEL_H1);
		addComponent(header);

		// Label body = new Label("<p>This application demonstrate how a Vaadin
		// application can take care of security itself while still integrating
		// with Spring Security.</p>" +
		// "<p>Please try it out by clicking and navigating around as different
		// users. You can log in as <em>user/user</em> or <em>admin/admin</em>.
		// Some of the protected " +
		// "features are hidden from the UI when you cannot access them, others
		// are visible all the time.</p>" +
		// "<p>Also note that since we are using web socket based push, we do
		// not have access to cookies and therefore cannot use Remember Me
		// services.</p>");
		// body.setContentMode(ContentMode.HTML);
		// addComponent(body);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}
}
