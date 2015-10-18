package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;
import com.gmail.volodymyrdotsenko.cms.be.domain.local.LanguageRepository;
import com.gmail.volodymyrdotsenko.cms.fe.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class) 
public class AudioSubtitleRepositoryTest {

	@Autowired
	private LanguageRepository langRepo;

	@Autowired
	private AudioItemRepository audioItemRepo;

	@Autowired
	private AudioSubtitleRepository repo;
	
	@Test
	public void shouldFindByLangAndByItem(){
		
//		System.out.println(repo.findByLangAndByItem(langRepo.findOne("en"), 
//				audioItemRepo.findOne(1L)));
	}
}