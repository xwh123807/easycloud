package org.easycloud.platform.core.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConditionalOnProperty(prefix = "org.myfly.platform", name = "security", matchIfMissing = false)
public class SecurityAutoConfiguration {

	@Bean
	public ApplicationSecurity applicationSecurity() {
		return new ApplicationSecurity();
	}

	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// 默认启用了csrf，拦截post请求
			http.csrf().disable();
			http.authorizeRequests().antMatchers("/css/**").permitAll().anyRequest().fullyAuthenticated().and()
					.formLogin().loginPage("/login").failureUrl("/login?error").permitAll().and().logout().permitAll();
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER","ADMIN");
		}

	}
}
