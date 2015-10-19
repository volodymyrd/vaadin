package com.gmail.volodymyrdotsenko.cms.fe.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gmail.volodymyrdotsenko.cms.fe.Main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Basic integration tests for JSP application.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@DirtiesContext
public class SampleWebJspApplicationTests {

	@Value("${local.server.port}")
	private int port;

	@Test
	public void testJspWithEl() throws Exception {
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity("http://localhost:" + this.port,
				String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertTrue("Wrong body:\n" + entity.getBody(), entity.getBody().contains("/resources/text.txt"));
	}
}