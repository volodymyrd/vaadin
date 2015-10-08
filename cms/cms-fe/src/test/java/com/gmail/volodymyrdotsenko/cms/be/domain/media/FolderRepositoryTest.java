package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

import com.gmail.volodymyrdotsenko.cms.fe.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class) 
public class FolderRepositoryTest {
	
	@Autowired
	private FolderRepository repo;
	
	@Test
	public void shouldfindRoot(){
		assertNotNull(repo.findRoot());
	}
}