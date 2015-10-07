package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;

public class MultiMediaMenuBar extends VerticalLayout{

	private static final long serialVersionUID = 1L;
	
    private final MenuBar menubar = new MenuBar();
    private final MultiMediaAdminView mainView;

    public MultiMediaMenuBar(MultiMediaAdminView mainView) {
    	this.mainView = mainView;
    	
    	setMargin(new MarginInfo(false, false, true, false));
    	
        final MenuBar.MenuItem audio = menubar.addItem("Audio", null);
        final MenuBar.MenuItem newAdioItem = audio.addItem("New", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				mainView.openView(new AdioItemView(mainView));
			}
        });
        
        audio.addSeparator();

        // Save reference to individual items so we can add sub-menu items to
        // them
        final MenuBar.MenuItem file = menubar.addItem("File", null);
        final MenuBar.MenuItem newItem = file.addItem("New", null);
        file.addItem("Open file...", menuCommand);
        file.addSeparator();

        newItem.addItem("File", menuCommand);
        newItem.addItem("Folder", menuCommand);
        newItem.addItem("Project...", menuCommand);

        file.addItem("Close", menuCommand);
        file.addItem("Close All", menuCommand);
        file.addSeparator();

        file.addItem("Save", menuCommand);
        file.addItem("Save As...", menuCommand);
        file.addItem("Save All", menuCommand);

        final MenuBar.MenuItem edit = menubar.addItem("Edit", null);
        edit.addItem("Undo", menuCommand);
        edit.addItem("Redo", menuCommand).setEnabled(false);
        edit.addSeparator();

        edit.addItem("Cut", menuCommand);
        edit.addItem("Copy", menuCommand);
        edit.addItem("Paste", menuCommand);
        edit.addSeparator();

        final MenuBar.MenuItem find = edit.addItem("Find/Replace", menuCommand);

        // Actions can be added inline as well, of course
        find.addItem("Google Search", new Command() {
            public void menuSelected(MenuItem selectedItem) {
            	//getMainWindow().open(new ExternalResource("http://www.google.com"));
            }
        });
        find.addSeparator();
        find.addItem("Find/Replace...", menuCommand);
        find.addItem("Find Next", menuCommand);
        find.addItem("Find Previous", menuCommand);

        final MenuBar.MenuItem view = menubar.addItem("View", null);
        view.addItem("Show/Hide Status Bar", menuCommand);
        view.addItem("Customize Toolbar...", menuCommand);
        view.addSeparator();

        view.addItem("Actual Size", menuCommand);
        view.addItem("Zoom In", menuCommand);
        view.addItem("Zoom Out", menuCommand);

        addComponent(menubar);
    }

    private Command menuCommand = new Command() {
        public void menuSelected(MenuItem selectedItem) {
            //getWindow().showNotification("Action " + selectedItem.getText());
        }
    };

}