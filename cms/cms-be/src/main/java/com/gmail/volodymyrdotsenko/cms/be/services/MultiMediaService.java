package com.gmail.volodymyrdotsenko.cms.be.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;
import com.gmail.volodymyrdotsenko.cms.be.domain.local.LanguageRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItem;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioItemRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.Folder;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.FolderRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.TextItem;
import com.gmail.volodymyrdotsenko.cms.be.dto.MapDto;

@Service
public class MultiMediaService {

	@Autowired
	private LanguageRepository langRepo;

	@Autowired
	private FolderRepository folderRepo;

	@Autowired
	private AudioItemRepository audioItemRepo;

	@Transactional
	public List<MapDto<Long, String>> getFolderDto(Long id, String lang) {

		List<MapDto<Long, String>> list = new ArrayList<>();

		Language l = langRepo.findOne(lang);

		if (id == null) {
			Folder f = folderRepo.findRoot();
			list.add(new MapDto<Long, String>(f.getId(), f.getLocal().get(l).getName()));
		} else {
			Folder f = folderRepo.findOne(id);
			f.getChildren();
			List<AudioItem> items = audioItemRepo.findByFolder(f);
			if (items != null) {
				items.forEach(e -> {
					System.out.println(e);
					list.add(new MapDto<Long, String>(e.getId(), e.getName()));
				});
			}
		}

		return list;
	}

	@Transactional
	public AudioItem getAudioItemWithContent(Long id) {
		AudioItem item = audioItemRepo.findOne(id);
		item.getContent().getContent();
		
		for (Map.Entry<Language, TextItem> e : item.getTextItem().entrySet()) {
		}

		return item;
	}
}