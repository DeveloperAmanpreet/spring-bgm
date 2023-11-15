package com.app.beforeall;


import com.app.Utils.Utils;
import com.app.common.user.Constants;
import com.app.common.user.Role.Role;
import com.app.common.user.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.IToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Order(999)
@Slf4j
class AfterAllTests {

  @Autowired
  TestRestTemplate restTemplate;

  static List<User> userList;
  static List<Role> roleList;
  static String token;

  @BeforeAll
  static void setup() {
    userList = Utils.getTestUsers();
    roleList = Utils.getTestRoles();
    token = Utils.getToken();
    if (token == null || token.isBlank()) {
      log.error("Missing Auth Token");
    }
  }


  @Test
  void deleteCreatedData() {
    deleteUsers();
    deleteRoles();
  }

  void deleteRoles() {
    for (var role : roleList) {
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);

      log.info("Deleting role: {}", role.getId());
      ResponseEntity<Void> createResponse = restTemplate
          .exchange("/roles/" + role.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), Void.class);
      assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  void deleteUsers() {
    for (var user : userList) {
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);

      log.info("Deleting User: {}", user.getId());
      ResponseEntity<String> createResponse = restTemplate
          .withBasicAuth("amanpreetsingh", "singh")
          .exchange(Constants.URI.USER + "/" + user.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), String.class);
      assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

      ResponseEntity<User> getResponse = restTemplate
          .exchange(Constants.URI.USER + "/" +user.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), User.class);
      assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
  }
}
