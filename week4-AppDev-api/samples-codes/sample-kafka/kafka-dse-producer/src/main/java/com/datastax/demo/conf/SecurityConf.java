package com.datastax.demo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/** SpringBoot Admin Console. */
@Configuration
public class SecurityConf extends WebSecurityConfigurerAdapter {

  /** {@inheritDoc} */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
  }
}
