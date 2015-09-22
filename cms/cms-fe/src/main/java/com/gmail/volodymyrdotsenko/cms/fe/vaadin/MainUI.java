package com.gmail.volodymyrdotsenko.cms.fe.vaadin;

import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.AccessDeniedView;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.ErrorView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SecurityExceptionUtils;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import org.vaadin.spring.sidebar.security.VaadinSecurityItemFilter;

/**
 * Main application UI that allows the user to navigate between views, and log
 * out.
 */
@SpringUI
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

	private static final long serialVersionUID = 1L;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	VaadinSecurity vaadinSecurity;

	@Autowired
	SpringViewProvider springViewProvider;

	@Autowired
	ValoSideBar sideBar;

	@Autowired
	I18N i18n;

	private String lang;

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	protected void init(VaadinRequest request) {
		//setLocale(new Locale(lang == null ? "en" : lang));
		
		getPage().setTitle(i18n.get("myMessageKey", "My argument"));
		// Let's register a custom error handler to make the 'access denied'
		// messages a bit friendlier.
		setErrorHandler(new DefaultErrorHandler() {

			private static final long serialVersionUID = 1L;

			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				if (SecurityExceptionUtils.isAccessDeniedException(event.getThrowable())) {
					Notification.show("Sorry, you don't have access to do that.");
				} else {
					super.error(event);
				}
			}
		});
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();

		// By adding a security item filter, only views that are accessible to
		// the user will show up in the side bar.
		sideBar.setItemFilter(new VaadinSecurityItemFilter(vaadinSecurity));
		layout.addComponent(sideBar);

		CssLayout viewContainer = new CssLayout();
		viewContainer.setSizeFull();
		layout.addComponent(viewContainer);
		layout.setExpandRatio(viewContainer, 1f);

		Navigator navigator = new Navigator(this, viewContainer);
		// Without an AccessDeniedView, the view provider would act like the
		// restricted views did not exist at all.
		springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		navigator.addProvider(springViewProvider);
		navigator.setErrorView(ErrorView.class);
		navigator.navigateTo(navigator.getState());

		setContent(layout); // Call this here because the Navigator must have
							// been configured before the Side Bar can be
							// attached to a UI.
	}

	public void closeWindow() {
		getWindows().stream().forEach(w -> removeWindow(w));
	}
}