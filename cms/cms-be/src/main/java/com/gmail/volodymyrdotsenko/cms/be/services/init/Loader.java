package com.gmail.volodymyrdotsenko.cms.be.services.init;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;
import com.gmail.volodymyrdotsenko.cms.be.domain.local.LanguageRepository;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.Folder;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.FolderLocal;
import com.gmail.volodymyrdotsenko.cms.be.domain.media.FolderRepository;
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

	@Autowired
	private LanguageRepository langRepo;

	@Autowired
	private FolderRepository folderRepo;

	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Spring context refreshed event");

		try {
			// Languages
			Language l = new Language("en", "English", "en");
			langRepo.save(l);
			l = new Language("ru", "Русский", "ru");
			langRepo.save(l);
			l = new Language("ua", "Українська", "ua");
			langRepo.save(l);

			// Users
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

			// Root folder
			Folder f = new Folder();
			Map<Language, FolderLocal> fl = new HashMap<>();
			langRepo.findAll().forEach(e -> {
				fl.put(e, new FolderLocal("root"));
			});
			f.setLocal(fl);
			folderRepo.save(f);
		} catch (Exception ex) {
			logger.severe(ex.getMessage());
		}
	}
}