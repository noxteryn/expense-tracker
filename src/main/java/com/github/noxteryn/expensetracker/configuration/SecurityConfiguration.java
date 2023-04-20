package com.github.noxteryn.expensetracker.configuration;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration
{
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http
				.csrf().disable() // Do NOT do this in real life.
				.authorizeHttpRequests((authz) -> authz
						.anyRequest().authenticated()
				)
				.httpBasic(withDefaults());
		return http.build();
	}
}