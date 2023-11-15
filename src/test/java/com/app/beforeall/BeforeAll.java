package com.app.beforeall;


import com.app.Utils.Utils;
import com.app.common.user.Constants;
import com.app.common.user.LoginData;
import com.app.common.user.Role.Role;
import com.app.common.user.User;
import com.app.config.security.AuthResponseDTO;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jdk.jshell.execution.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Order(1)
@Slf4j
class BeforeAllTests {

  @Autowired
  TestRestTemplate restTemplate;

  static List<User> userList;
  static List<Role> roleList;

  static String token;

  @BeforeAll
  static void setup() {
    roleList = Utils.getTestRoles();
    userList = Utils.getTestUsers();
  }

  @Test
  @Order(1)
  void createBaseData() {
    persistRoles();
    createUser();
    signInAndGetToken(userList.get(0).getUsername(), userList.get(0).getHashword());
    Utils.setToken(token);
  }

  public String signInAndGetToken(String username, String password) {
    if(token == null) {
      LoginData loginData = LoginData.builder()
          .username(username)
          .password(password)
          .build();
      ResponseEntity<AuthResponseDTO> response = restTemplate
          .postForEntity(Constants.URI.LOGIN, loginData, AuthResponseDTO.class);
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertNotNull(response.getBody());
      AuthResponseDTO authResponseDTO = response.getBody();
      assertNotNull(authResponseDTO.getAccessToken());
      log.info("Response: {}", response);
      token = authResponseDTO.getAccessToken();
      log.info("Token: {}", token);
    }
    return token;
  }

  void persistRoles() {
    for (var role : roleList) {
      ResponseEntity<Void> createResponse = restTemplate
          .postForEntity("/roles", role, Void.class);
      assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      log.info("Created role {}: {}", role.getName(),role.getId());
    }
  }

  void createUser() {
    userList = userList.stream().peek((user) -> {
      ResponseEntity<User> createResponse = restTemplate
          .postForEntity(Constants.URI.USER, user, User.class);
      assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

      URI locationOfNewUser = createResponse.getHeaders().getLocation();
      assert locationOfNewUser != null;
      user.setId(Long.valueOf(locationOfNewUser.getPath().substring("/users/".length())));

//      log.info("Created User at location: {}", locationOfNewUser);
//
//      createResponse = restTemplate.withBasicAuth("amanpreetsingh", "singh")
//          .getForEntity(locationOfNewUser, User.class);
//      assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }).toList();

    Utils.setUserList(userList);
  }
}
