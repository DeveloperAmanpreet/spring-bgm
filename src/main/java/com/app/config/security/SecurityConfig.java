package com.app.config.security;

import com.app.common.user.Constants;
import com.app.common.user.UserConverter;
import com.app.common.user.UserDtoConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    modelMapper.addConverter(new UserConverter());
    modelMapper.addConverter(new UserDtoConverter());
    return modelMapper;
  }

//  UserService userService;
//
//  public SecurityConfig(UserService userService) {
//    this.userService = userService;
//  }

  // Strict rules at top and more generic at bottom
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests((authorize) -> authorize
        .requestMatchers(HttpMethod.POST, "/users").permitAll()
        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/roles").permitAll()
        .anyRequest().authenticated()
      )
      .cors(Customizer.withDefaults())
      .csrf(AbstractHttpConfigurer::disable);

    http.addFilterBefore(new JWTAuthenticationFilter(), AnonymousAuthenticationFilter.class)
        .sessionManagement(((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));

    return http.build();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(Constants.URI.USER ).allowedOrigins("*");
      }
    };
  }

//  @Bean
//  public UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
//    User.UserBuilder users = User.builder();
//    UserDetails amanpreetsingh = users
//        .username("amanpreetsingh")
//        .password(passwordEncoder.encode("singh"))
//        .roles() // No roles for now
//        .build();
//    UserDetails sarah = users
//        .username("sarah1")
//        .password(passwordEncoder.encode("abc123"))
//        .roles("CARD-OWNER")
//        .build();
//    UserDetails hankOwnsNoCards = users
//        .username("hank-owns-no-cards")
//        .password(passwordEncoder.encode("qrs456"))
//        .roles("NON-OWNER")
//        .build();
//    UserDetails kumar = users
//        .username("kumar2")
//        .password(passwordEncoder.encode("xyz789"))
//        .roles("CARD-OWNER")
//        .build();
//    return new InMemoryUserDetailsManager(amanpreetsingh, sarah, hankOwnsNoCards, kumar);
//  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}