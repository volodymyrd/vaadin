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
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioSubtitle;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.AudioSubtitleRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.Folder;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.FolderRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.MediaItem;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.MediaItemRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.TextItem;
import com.gmail.volodymyrdotsenko.cms.be.dto.MapDto;

@Service
public class MultiMediaService {

	@Autowired
	private LanguageRepository langRepo;

	@Autowired
	private FolderRepository folderRepo;

	@Autowired
	private MediaItemRepository mediaItemRepo;

	@Autowired
	private AudioItemRepository audioItemRepo;

	@Autowired
	private AudioSubtitleRepository audioSubtitleRepo;

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
	public MediaItem save(MediaItem mediaItem, List<AudioSubtitle> audioSubtitles) {
		mediaItem.getContent().getContent();
		mediaItem = mediaItemRepo.save(mediaItem);

		if (audioSubtitles != null && audioSubtitles.size() > 0) {
			// audioSubtitles.forEach(e -> audioSubtitleRepo.save(e));
			audioSubtitleRepo.save(audioSubtitles);
		}

		return mediaItem;
	}

	@Transactional
	public AudioItem getAudioItemWithContent(Long id) {
		AudioItem item = audioItemRepo.findOne(id);
		item.getContent().getContent();

		for (Map.Entry<Language, TextItem> e : item.getTextItem().entrySet()) {
		}

		return item;
	}

	public List<AudioSubtitle> getAudioSubtitles(AudioItem item) {
		return audioSubtitleRepo.findByItem(item);
	}
}