package com.gmail.volodymyrdotsenko.cms.fe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.annotation.EnableVaadinSharedSecurity;
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.web.authentication.VaadinUrlAuthenticationSuccessHandler;

/**
 * Configure Spring Security.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableVaadinSharedSecurity
// @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.inMemoryAuthentication().withUser("user").password("user").roles("USER").and().withUser("admin")
		// .password("admin").roles("ADMIN");

		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); // Use Vaadin's built-in CSRF protection instead
		http.authorizeRequests().antMatchers("/login/**").anonymous().antMatchers("/vaadinServlet/UIDL/**").permitAll()
				.antMatchers("/vaadinServlet/HEARTBEAT/**").permitAll().anyRequest().authenticated();
		http.httpBasic().disable();
		http.formLogin().disable();
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll();
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
		// TODO Is there some way of exposing the RememberMeServices instance
		// that the remember me configurer creates by default?
		TokenBasedRememberMeServices services = new TokenBasedRememberMeServices("myAppKey", userDetailsService());
		services.setAlwaysRemember(true);
		return services;
	}

	@Bean(name = VaadinSharedSecurityConfiguration.VAADIN_AUTHENTICATION_SUCCESS_HANDLER_BEAN)
	VaadinAuthenticationSuccessHandler vaadinAuthenticationSuccessHandler(HttpService httpService,
			VaadinRedirectStrategy vaadinRedirectStrategy) {
		return new VaadinUrlAuthenticationSuccessHandler(httpService, vaadinRedirectStrategy, "/");
	}
}