package com.app.config.security;

import com.app.common.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  UserService userService;

  public SecurityConfig(UserService userService) {
    this.userService = userService;
  }

  // Strict rules at top and more generic at bottom
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf((AbstractHttpConfigurer::disable)).cors((AbstractHttpConfigurer::disable))
        .authorizeRequests()
        .requestMatchers(HttpMethod.POST, "/users").permitAll()
        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/roles").permitAll()
        .anyRequest().authenticated();

    http.addFilterBefore(new JWTAuthenticationFilter(), AnonymousAuthenticationFilter.class)
        .sessionManagement(((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));

    return http.build();
  }

  @Bean
  public UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
    User.UserBuilder users = User.builder();
    UserDetails amanpreetsingh = users
        .username("amanpreetsingh")
        .password(passwordEncoder.encode("singh"))
        .roles() // No roles for now
        .build();
    UserDetails sarah = users
        .username("sarah1")
        .password(passwordEncoder.encode("abc123"))
        .roles("CARD-OWNER")
        .build();
    UserDetails hankOwnsNoCards = users
        .username("hank-owns-no-cards")
        .password(passwordEncoder.encode("qrs456"))
        .roles("NON-OWNER")
        .build();
    UserDetails kumar = users
        .username("kumar2")
        .password(passwordEncoder.encode("xyz789"))
        .roles("CARD-OWNER")
        .build();
    return new InMemoryUserDetailsManager(amanpreetsingh, sarah, hankOwnsNoCards, kumar);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}