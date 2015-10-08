package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.util.Collection;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.LanguageRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.FolderRepository;
import com.gmail.volodymyrdotsenko.cms.be.dto.MapDto;
import com.gmail.volodymyrdotsenko.cms.be.services.MultiMediaService;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.event.Action;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractSelect;

//https://dev.vaadin.com/svn/demo/sampler/src/com/vaadin/demo/sampler/features/
//https://vaadin.com/forum/#!/thread/131803/131802
public class MediaLibraryTree extends HorizontalLayout
		implements Property.ValueChangeListener, Button.ClickListener, Action.Handler {

	private static final long serialVersionUID = 1L;

	// Actions for the context menu
	private static final Action ACTION_ADD = new Action("Add child item");
	private static final Action ACTION_DELETE = new Action("Delete");
	private static final Action[] ACTIONS = new Action[] { ACTION_ADD, ACTION_DELETE };

	private final Tree tree;
	private final String lang;

	private final LanguageRepository langRepo;
	private final FolderRepository folderRepo;
	private final MultiMediaService service;

	HorizontalLayout editBar;
	private TextField editor;
	private Button change;

	public MediaLibraryTree(ApplicationContext applicationContext) {
		this.service = applicationContext.getBean(MultiMediaService.class);
		this.langRepo = applicationContext.getBean(LanguageRepository.class);
		this.folderRepo = applicationContext.getBean(FolderRepository.class);

		lang = VaadinSession.getCurrent().getLocale().getLanguage();

		setSpacing(true);

		// Create the Tree,a add to layout
		tree = new Tree("Media Library");
		addComponent(tree);

		// Contents from a (prefilled example) hierarchical container:
		//tree.setContainerDataSource(buildContainer());
		buildContainer();
		// Add Valuechangelistener and Actionhandler
		tree.addValueChangeListener(this);

		// Add actions (context menu)
		tree.addActionHandler(this);

		// Cause valueChange immediately when the user selects
		tree.setImmediate(true);

		// Set tree to show the 'name' property as caption for items
		tree.setItemCaptionPropertyId("name");
		tree.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);

		// Expand whole tree
		// for (Object id : tree.rootItemIds()) {
		// tree.expandItemsRecursively(id);
		// }

		// Create the 'editor bar' (textfield and button in a horizontallayout)
		// editBar = new HorizontalLayout();
		// editBar.setMargin(true);
		// editBar.setEnabled(false);
		// addComponent(editBar);
		// // textfield
		// editor = new TextField("Item name");
		// editor.setImmediate(true);
		// editBar.addComponent(editor);
		// // apply-button
		// change = new Button("Apply", this);
		// editBar.addComponent(change);
		// editBar.setComponentAlignment(change, Alignment.BOTTOM_LEFT);

		tree.addExpandListener(new ExpandListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void nodeExpand(ExpandEvent event) {
				System.out.println(event.getItemId());
				List<MapDto<Long, String>> l = service.getFolderDto((Long) event.getItemId(), lang);
				if (l != null) {
					l.forEach(e -> {
						Item item = tree.addItem(e.getKey());
						System.out.println(item);
						tree.setParent(item, event.getItemId());
						item.getItemProperty("name").setValue(e.getValue());
					});
				}
			}
		});
	}

	@Override
	public Action[] getActions(Object target, Object sender) {
		return ACTIONS;
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		if (action == ACTION_ADD) {
			// Allow children for the target item, and expand it
			tree.setChildrenAllowed(target, true);
			tree.expandItem(target);

			// Create new item, set parent, disallow children (= leaf node)
			Object itemId = tree.addItem();
			tree.setParent(itemId, target);
			tree.setChildrenAllowed(itemId, false);

			// Set the name for this item (we use it as item caption)
			Item item = tree.getItem(itemId);
			Property name = item.getItemProperty("name");
			name.setValue("New Item");

		} else if (action == ACTION_DELETE) {
			Object parent = tree.getParent(target);
			tree.removeItem(target);
			// If the deleted object's parent has no more children, set its
			// childrenallowed property to false (= leaf node)
			if (parent != null) {
				Collection<?> children = tree.getChildren(parent);
				if (children != null && children.isEmpty()) {
					tree.setChildrenAllowed(parent, false);
				}
			}
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		// If the edited value contains something, set it to be the item's new
		// 'name' property
		if (!editor.getValue().equals("")) {
			Item item = tree.getItem(tree.getValue());
			Property name = item.getItemProperty("name");
			name.setValue(editor.getValue());
		}
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().getValue() != null) {
			// If something is selected from the tree, get its 'name' and
			// insert it into the textfield
			// editor.setValue(tree.getItem(event.getProperty().getValue()).getItemProperty("name"));
			// editor.requestRepaint();
			// editBar.setEnabled(true);
		} else {
			// editor.setValue("");
			// editBar.setEnabled(false);
		}
	}

	private final HierarchicalContainer hwContainer = new HierarchicalContainer();

	private HierarchicalContainer buildContainer() {
		tree.addContainerProperty("name", String.class, null);

		// build root
		List<MapDto<Long, String>> root = service.getFolderDto(null, lang);

		if (root != null && root.size() > 0) {
			Item item = tree.addItem(root.get(0).getKey());
			item.getItemProperty("name").setValue(root.get(0).getValue());
			tree.setChildrenAllowed(root.get(0).getKey(), true);
		}
		return hwContainer;
	}

	// private static final String[][] hardware = { //
	// { "Desktops", "Dell OptiPlex GX240", "Dell OptiPlex GX260", "Dell
	// OptiPlex GX280" },
	// { "Monitors", "Benq T190HD", "Benq T220HD", "Benq T240HD" },
	// { "Laptops", "IBM ThinkPad T40", "IBM ThinkPad T43", "IBM ThinkPad T60" }
	// };
	//
	// public static HierarchicalContainer getHardwareContainer() {
	// Item item = null;
	// int itemId = 0; // Increasing numbering for itemId:s
	//
	// // Create new container
	// HierarchicalContainer hwContainer = new HierarchicalContainer();
	// // Create containerproperty for name
	// hwContainer.addContainerProperty("name", String.class, null);
	// // Create containerproperty for icon
	// // hwContainer.addContainerProperty(hw_PROPERTY_ICON,
	// // ThemeResource.class,
	// // new ThemeResource("../runo/icons/16/document.png"));
	// for (int i = 0; i < hardware.length; i++) {
	// // Add new item
	// item = hwContainer.addItem(itemId);
	// // Add name property for item
	// item.getItemProperty("name").setValue(hardware[i][0]);
	// // Allow children
	// hwContainer.setChildrenAllowed(itemId, true);
	// itemId++;
	// for (int j = 1; j < hardware[i].length; j++) {
	// // if (j == 1) {
	// // item.getItemProperty(hw_PROPERTY_ICON).setValue(
	// // new ThemeResource("../runo/icons/16/folder.png"));
	// // }
	// // Add child items
	// item = hwContainer.addItem(itemId);
	// item.getItemProperty("name").setValue(hardware[i][j]);
	// hwContainer.setParent(itemId, itemId - j);
	// hwContainer.setChildrenAllowed(itemId, false);
	//
	// itemId++;
	// }
	// }
	// return hwContainer;
	// }

}