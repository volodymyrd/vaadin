package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.administration;

import org.vaadin.spring.i18n.I18N;
import org.vaadin.teemu.switchui.Switch;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.gmail.volodymyrdotsenko.cms.be.domain.Person;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.User;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.UserRepository;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.MainUI;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UserForm extends TabSheet {

	private static final long serialVersionUID = 1L;

	private User user;

	private UsersView usersView;

	private UserRepository repo;

	private I18N i18n;

	public UserForm(User user, UserRepository repo, UsersView usersView, I18N i18n) {
		this.user = user;
		this.repo = repo;
		this.i18n = i18n;
		this.usersView = usersView;

		setSizeFull();
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

		addTab(buildGeneralTab());
		addTab(buildSecurityTab());
		addTab(buildRolesTab());
	}

	AbstractForm<User> buildGeneralTab() {
		return new GeneralInfo();
	}

	AbstractForm<User> buildSecurityTab() {
		return new SecurityInfo();
	}

	Component buildRolesTab() {
		VerticalLayout v = new VerticalLayout();
		v.setCaption("Roles");

		return v;
	}

	private class GeneralInfo extends AbstractForm<User> {
		private static final long serialVersionUID = 1L;

		TextField userName = new MTextField("UserName");
		TextField email = new MTextField("Email");
		// TextField phoneNumber = new MTextField("Phone");
		// DateField birthDay = new DateField("Birthday");
		// Switch colleague = new Switch("Colleague");

		GeneralInfo() {
			setCaption("General info");
			setSizeUndefined();
			setEntity(user);
		}

		@Override
		protected Component createContent() {

			return new VerticalLayout(new MFormLayout(userName, email).withMargin(true));
		}
	}

	private class SecurityInfo extends AbstractForm<User> {
		private static final long serialVersionUID = 1L;

		PasswordField password = new MPasswordField("Password");

		// TextField phoneNumber = new MTextField("Phone");
		// DateField birthDay = new DateField("Birthday");
		// Switch colleague = new Switch("Colleague");

		SecurityInfo() {
			setCaption("Security");
			setSizeUndefined();
			setEntity(user);
		}

		@Override
		protected Component createContent() {

			return new VerticalLayout(new MFormLayout(password).withMargin(true));
		}
	}

}