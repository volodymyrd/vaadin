package com.gmail.volodymyrdotsenko.cms.be.services.init;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.gmail.volodymyrdotsenko.cms.be.domain.users.RoleRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.User;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.UserRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.users.UserRole;

@Component
public class Loader implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger logger = Logger.getLogger(Loader.class.getName());

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Spring context refreshed event");

		try {
			UserRole role = new UserRole();
			role.setName("ROLE_ADMIN");
			User admin = new User();
			admin.setEmail("admin@admin.com");
			admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
			admin.setUserName("admin");
			Calendar c = Calendar.getInstance();
			c.set(9999, 11, 31, 0, 0, 0);
			admin.setExpired(c.getTime());
			admin.getRoles().add(role);

			roleRepo.save(role);
			userRepo.save(admin);
		} catch (Exception ex) {
			logger.severe(ex.getMessage());
		}
	}
}