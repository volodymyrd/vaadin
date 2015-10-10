package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import org.springframework.context.ApplicationContext;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.LanguageRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.FolderRepository;
import com.gmail.volodymyrdotsenko.cms.be.services.MultiMediaService;
import com.vaadin.data.Container;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TreeTable;

public class MediaLibraryTreeTable extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	private final TreeTable tree;

	private final String lang;
	private final LanguageRepository langRepo;
	private final FolderRepository folderRepo;
	private final MultiMediaService service;

	public MediaLibraryTreeTable(ApplicationContext applicationContext) {
		this.service = applicationContext.getBean(MultiMediaService.class);
		this.langRepo = applicationContext.getBean(LanguageRepository.class);
		this.folderRepo = applicationContext.getBean(FolderRepository.class);

		lang = VaadinSession.getCurrent().getLocale().getLanguage();

		setSpacing(true);
		
		tree = new TreeTable("Media Library");
		addComponent(tree);

		tree.setContainerDataSource(createHierarchicalContainer());
	}
	
	private Container.Hierarchical createHierarchicalContainer(){
		Container.Hierarchical c = new HierarchicalContainer();
		//tree.popu
		
		return c;
	}
}