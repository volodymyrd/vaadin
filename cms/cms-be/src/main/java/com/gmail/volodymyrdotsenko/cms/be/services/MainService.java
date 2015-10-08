package com.gmail.volodymyrdotsenko.cms.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.LanguageRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.Folder;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.FolderRepository;

@Service
public class MainService {

	@Autowired
	private LanguageRepository langRepo;

	@Autowired
	private FolderRepository folderRepo;
}