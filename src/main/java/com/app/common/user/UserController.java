package com.app.common.user;

import com.app.config.security.AuthResponseDTO;
import com.app.config.security.JWTAuthentication;
import com.app.config.security.JWTService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@CrossOrigin( origins = {"*"} , maxAge = 3600)
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
  public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
    try {
      UserDto userDto = userService.getUserById(id);
      if(userDto == null) {
        log.info("User {} not found.", id);
        return ResponseEntity.notFound().build();
      }
      log.info("Found user: {}", userDto);
      return ResponseEntity.ok(userDto);
    } catch (EntityNotFoundException e) {
      log.error("User {} not found.", id);
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error("Failed to get user:{}, Error: {}", id, e.getMessage());
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getAllUsers(JWTAuthentication jwtAuthentication) {
    log.info("Started getAllUsers");
    log.info("Principal: {}", jwtAuthentication.getName());
    log.info("Roles: {}", jwtAuthentication.getAuthorities());
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PostMapping
  public ResponseEntity<Void> createUser(@RequestBody UserDto userDto, UriComponentsBuilder ucb) {
    try {
      userDto.setHashword(passwordEncoder.encode(userDto.getHashword()));
      UserDto savedUser = userService.createUser(userDto);
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
      userService.deleteById(id);
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
  public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginData data) {
    try{
      if(data.password == null || data.username.isBlank()) {
        return ResponseEntity.badRequest().build();
      }
      UserDto userDto = userService.getUserByUsername(data.getUsername());
      if(userDto != null && passwordEncoder.matches(data.getPassword(), userDto.getHashword())) {
        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(
          jwtService.createJWT(
            userDto.getUsername(), userDto.getRoles().stream().toList()
          )
        );
        log.info("Login Successful for user: {}", userDto);
        return ResponseEntity.ok(response);
      } else {
        log.error("Login Failed for user: {}", userDto);
      }
    } catch (Exception e) {
      log.error("Login Failed for user: {}. Error: {}", data.getUsername(), e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

}
