package com.app.common.user;


import com.app.common.user.Role.Role;
import com.app.config.security.AuthResponseDTO;
import com.app.config.security.JWTAuthentication;
import com.app.config.security.JWTService;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constants.URI.USER)
@Slf4j
public class UserController {

  private final UserService userService;

  PasswordEncoder passwordEncoder;
  private final JWTService jwtService;

  public UserController(
      UserService userService,
      PasswordEncoder passwordEncoder,
      JWTService jwtService
  ) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    try {
      User user = userService.getUserById(id);
      if(user == null){
        log.info("User {} not found.", id);
        return ResponseEntity.notFound().build();
      }
      log.info("Found user: {}", user);
      return ResponseEntity.ok(user);
    } catch (EntityNotFoundException e) {
      log.error("User {} not found.", id);
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error("Failed to get user:{}, Error: {}", id, e.getMessage());
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers(JWTAuthentication jwtAuthentication) {
    log.info("Started getAllUsers");
    log.info("Principal: {}", jwtAuthentication.getName());
    log.info("Roles: {}", jwtAuthentication.getAuthorities());
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PostMapping
  public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucb) {
    try {
      user.setHashword(passwordEncoder.encode(user.getHashword()));
      User savedUser = userService.createUser(user);
      log.info("Saved User: {}", savedUser);
      URI locationOfNewCashCard = ucb
          .path(Constants.URI.USER + "/{id}")
          .buildAndExpand(savedUser.getId())
          .toUri();
      return ResponseEntity.created(locationOfNewCashCard).build();
    } catch (Exception e) {
      log.error("Failed to create user. Error: {}", e.getMessage());
      //TODO: user should know the reason for non creation of resource
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    log.info("Deleting user: {}", id) ;

    if (id == null) {
      log.error("Id cannot be null");
      return ResponseEntity.badRequest().build();
    }
    try {
      userService.deleteByid(id);
      log.info("Deleted Successfully: {}", id);
    } catch (EntityNotFoundException e) {
      log.error("User {} not found.", id);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (Exception e) {
      log.error("Error deleting user: {}. Error: {}", id, e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok("Deleted user: " + id);
  }

  @PostMapping("/login")
  @Transactional(rollbackOn = Exception.class)
  public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginData data) {
    try{
      if(data.password == null || data.username.isBlank()) {
        return ResponseEntity.badRequest().build();
      }
      User user = userService.getUserByUsername(data.getUsername());
      if(user != null && passwordEncoder.matches(data.getPassword(), user.getHashword())) {
        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(jwtService.createJWT(user.getUsername(), user.getRole().stream().map(Role::getName).toList()));
        log.info("Login Successful for user: {}", user);
        return ResponseEntity.ok(response);
      } else {
        log.error("Login Failed for user: {}", user);
      }
    } catch (Exception e) {
      log.error("Login Failed for user: {}. Error: {}", data.getUsername(), e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

}
