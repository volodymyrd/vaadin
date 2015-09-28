package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.administration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.User;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.UserRepository;
import com.gmail.volodymyrdotsenko.cms.fe.vaadin.MainUI;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class UsersList extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private MainUI mainUI;
	private UsersView usersView;

	private UserRepository repo;

	private I18N i18n;

	private MTable<User> list;
	private Button addNew = new MButton(FontAwesome.PLUS, this::add);
	private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
	private Button delete = new ConfirmButton(FontAwesome.TRASH_O, "Are you sure you want to delete the entry?",
			this::remove);

	public UsersList(UserRepository repo, MainUI mainUI, UsersView usersView, I18N i18n) {
		this.repo = repo;
		this.i18n = i18n;
		this.usersView = usersView;
		this.mainUI = mainUI;
		setMargin(true);

		list = new MTable<>(User.class).withProperties("userName", "email", "expired")
				.withColumnHeaders(i18n.get("admin.users.list.username"), i18n.get("admin.users.list.email"),
						i18n.get("admin.users.list.expired"))
				.setSortableProperties("userName", "email").withFullWidth();

		list.setConverter("expired", new StringToDateConverter() {

			private static final long serialVersionUID = 1L;

			@Override
			public DateFormat getFormat(Locale locale) {
				return new SimpleDateFormat("dd.MM.yyyy");
			}
		});

		list.addItemClickListener(new ItemClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (!event.isDoubleClick())
					return;

				list.select(event.getItemId());

				usersView.openUserForm(list.getValue());
			}
		});

		listEntities();
		list.addMValueChangeListener(e -> adjustActionButtonState());
		addComponent(list);
	}

	protected void adjustActionButtonState() {
		boolean hasSelection = list.getValue() != null;
		edit.setEnabled(hasSelection);
		delete.setEnabled(hasSelection);
	}

	static final int PAGESIZE = 45;

	private void listEntities() {

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
		edit(new User());
	}

	public void edit(ClickEvent e) {
		edit(list.getValue());
	}

	public void remove(ClickEvent e) {
		repo.delete(list.getValue());
		list.setValue(null);
		listEntities();
	}

	protected void edit(final User user) {
	}

	public void saveEntry(User entry) {
		repo.save(entry);
		listEntities();
		closeWindow();
	}

	public void resetEntry(User entry) {
		listEntities();
		closeWindow();
	}

	private void closeWindow() {
		mainUI.closeWindow();
	}
}