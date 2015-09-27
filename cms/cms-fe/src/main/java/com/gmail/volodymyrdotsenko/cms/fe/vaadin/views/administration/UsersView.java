package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.gmail.volodymyrdotsenko.cms.be.domain.PersonRepository;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.MainUI;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.Sections;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * View that is available to administrators only.
 *
 */
@Secured("ROLE_ADMIN")
@SpringView(name = "users")
@SideBarItem(sectionId = Sections.ADMINISTRATION, captionCode = "application.section.item.users")
@FontAwesomeIcon(FontAwesome.USERS)
public class UsersView extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;

	private MainUI mainUI;

	private PersonRepository repo;

	// private UserManagementMainComp umMainComp = new UserManagementMainComp();
	private TabSheet usersTabSheet;

	@Autowired
	public UsersView(PersonRepository repo, MainUI mainUI) {
		this.repo = repo;
		this.mainUI = mainUI;

		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		Button b = new Button("test121");
		// umMainComp.addTab(b, "Main", null);
		// umMainComp.setSizeFull();
		vl.addComponent(buildUsersTabSheet());
		// vl.addComponent(umMainComp);
		// vl.setExpandRatio(umMainComp, 1f);
		// vl.addComponent(new Button("test123"));
		setCompositionRoot(vl);
	}

	private TabSheet buildUsersTabSheet() {
		// common part: create layout
		usersTabSheet = new TabSheet();
		usersTabSheet.setImmediate(true);
		usersTabSheet.setWidth("100.0%");
		usersTabSheet.setHeight("100.0%");

		Button btn = new Button("tab");
		btn.addClickListener(event -> {
			VerticalLayout l1 = new VerticalLayout();
			l1.setMargin(true);
			l1.addComponent(new UserManagementMainComp());
			usersTabSheet.addTab(l1, "Tab1", null);
		});
		usersTabSheet.addTab(btn, "Tab", null);

		return usersTabSheet;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		System.out.println("ViewChangeEvent");
	}
}