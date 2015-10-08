package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gmail.volodymyrdotsenko.cms.fe.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class) 
public class AudioItemRepositoryTest {

	@Autowired
	private AudioItemRepository repo;

	@Autowired
	private FolderRepository folderRepo;
	
	@Test
	public void shouldFindByFolder(){
		System.out.println(repo.findByFolder(folderRepo.findRoot()));
	}
}