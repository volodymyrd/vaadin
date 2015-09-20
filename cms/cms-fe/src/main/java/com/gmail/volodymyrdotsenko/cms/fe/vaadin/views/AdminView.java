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

import com.gmail.volodymyrdotsenko.cms.be.domain.Person;
import com.gmail.volodymyrdotsenko.cms.be.domain.PersonRepository;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.MainUI;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.Sections;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.backend.MyBackend;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * View that is available to administrators only.
 *
 */
@Secured("ROLE_ADMIN")
@SpringView(name = "admin")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Admin View")
@FontAwesomeIcon(FontAwesome.COGS)
public class AdminView extends CustomComponent implements View {

	private static final long serialVersionUID = 1L;

	private final MyBackend backend;

	private MainUI mainUI;

	PersonRepository repo;

	private MTable<Person> list = new MTable<>(Person.class).withProperties("id", "name", "email")
			.withColumnHeaders("id", "Name", "Email").setSortableProperties("name", "email").withFullWidth();

	private Button addNew = new MButton(FontAwesome.PLUS, this::add);
	private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
	private Button delete = new ConfirmButton(FontAwesome.TRASH_O, "Are you sure you want to delete the entry?",
			this::remove);

	@Autowired
	public AdminView(MyBackend backend, PersonRepository repo, MainUI mainUI) {
		this.backend = backend;
		this.repo = repo;
		// Button button = new Button("Call admin backend", new
		// Button.ClickListener() {
		// @Override
		// public void buttonClick(Button.ClickEvent event) {
		// Notification.show(AdminView.this.backend.adminOnlyEcho("Hello Admin
		// World!"));
		// }
		// });
		// setCompositionRoot(button);
		setCompositionRoot(new MVerticalLayout(new RichText().withMarkDownResource("/welcome.md"),
				new MHorizontalLayout(addNew, edit, delete), list).expand(list));
		listEntities();
		list.addMValueChangeListener(e -> adjustActionButtonState());

		this.mainUI = mainUI;
	}

	protected void adjustActionButtonState() {
		boolean hasSelection = list.getValue() != null;
		edit.setEnabled(hasSelection);
		delete.setEnabled(hasSelection);
	}

	static final int PAGESIZE = 45;

	private void listEntities() {
		// A dead simple in memory listing would be:
		// list.setBeans(repo.findAll());

		// Lazy binding with SortableLazyList: memory and query efficient
		// connection from Vaadin Table to Spring Repository
		// Note that fetching strategies can be given to MTable constructor as
		// well.
		// Use this approach if you expect you'll have lots of data in your
		// table.

		list.setBeans(new SortableLazyList<>(
				// entity fetching strategy
				(firstRow, asc,
						sortProperty) -> repo.findAllBy(new PageRequest(firstRow / PAGESIZE, PAGESIZE,
								asc ? Sort.Direction.ASC : Sort.Direction.DESC,
								// fall back to id as "natural order"
								sortProperty == null ? "id" : sortProperty)),
				// count fetching strategy
				() -> (int) repo.count(), PAGESIZE));
		adjustActionButtonState();

	}

	public void add(ClickEvent clickEvent) {
		edit(new Person());
	}

	public void edit(ClickEvent e) {
		edit(list.getValue());
	}

	public void remove(ClickEvent e) {
		repo.delete(list.getValue());
		list.setValue(null);
		listEntities();
	}

	protected void edit(final Person phoneBookEntry) {
		PhoneBookEntryForm phoneBookEntryForm = new PhoneBookEntryForm(phoneBookEntry);
		phoneBookEntryForm.openInModalPopup();
		phoneBookEntryForm.setSavedHandler(this::saveEntry);
		phoneBookEntryForm.setResetHandler(this::resetEntry);
	}

	public void saveEntry(Person entry) {
		repo.save(entry);
		listEntities();
		closeWindow();
	}

	public void resetEntry(Person entry) {
		listEntities();
		closeWindow();
	}

	private void closeWindow() {
		mainUI.closeWindow();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
	}
}