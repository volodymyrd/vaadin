package com.gmail.volodymyrdotsenko.cms.fe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 * Configure Spring Security.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// @EnableVaadinSharedSecurity
// @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new ShaPasswordEncoder(256));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); // Use Vaadin's built-in CSRF protection instead
		http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/login/**").permitAll()
				.antMatchers("/vaadinServlet/UIDL/**").permitAll().antMatchers("/vaadinServlet/HEARTBEAT/**")
				.permitAll().antMatchers("/cms/**").authenticated().antMatchers("/admin/**").authenticated();

		http.httpBasic().disable();
		http.formLogin().disable();
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll();
		http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
		http.rememberMe().rememberMeServices(rememberMeServices()).key("myAppKey");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/VAADIN/**");
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		TokenBasedRememberMeServices services = new TokenBasedRememberMeServices("myAppKey", userDetailsService());
		services.setAlwaysRemember(true);
		return services;
	}
}