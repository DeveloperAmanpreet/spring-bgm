package com.app;


import com.app.Utils.Utils;
import com.app.common.user.Constants;
import com.app.common.user.Role.Role;
import com.app.common.user.Role.RoleDto;
import com.app.common.user.User;
import com.app.common.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Order(101)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {

  @Autowired
  TestRestTemplate restTemplate;

  static UserDto user;
  static HttpHeaders headers;

  @BeforeAll
  static void setup() {
    String token = Utils.getToken();
    headers = new HttpHeaders();
    headers.setBearerAuth(token);
  }

  @Test
  @Order(1)
  void shouldReturnNotFoundForNonExistentUser() {
    ResponseEntity<UserDto> response = restTemplate
        .exchange(Constants.URI.USER + "/" + 91238127, HttpMethod.GET, new HttpEntity<>(null, headers), UserDto.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNull();
  }

  @Test
  @Order(2)
  void shouldCreateUserSuccessfully() {
    RoleDto roleDto  = Utils.getTestRoles().get(1);
    Set<RoleDto> rolesDto = new HashSet<>();
    rolesDto.add(roleDto);
    user = new UserDto(
        3213214L, "name_" + 3213214,
        "username_" + 3213214,
        "username_" + 3213214 + "@email.com",
        "hashword_" + 3213214, Instant.now(),
        rolesDto.stream().map(RoleDto::getName).toList());
    ResponseEntity<UserDto> response = restTemplate
        .exchange(Constants.URI.USER, HttpMethod.POST, new HttpEntity<>(user, headers), UserDto.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    URI locationOfNewUser = response.getHeaders().getLocation();
    assert locationOfNewUser != null;
    String actualId = locationOfNewUser.getPath().substring(Constants.URI.USER.length() + 1);
    user.setId(Long.valueOf(actualId));
    shouldGetUserDetailsSuccessfully(locationOfNewUser, user);
  }

  private void shouldGetUserDetailsSuccessfully(URI locationOfNewUser, UserDto expectedUser) {
    ResponseEntity<UserDto> getResponse = restTemplate
        .exchange(locationOfNewUser, HttpMethod.GET, new HttpEntity<>(user, headers), UserDto.class);

    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    UserDto actualUser = Objects.requireNonNull(getResponse.getBody());
    assertEquals(expectedUser, actualUser);
    // TODO check that the hash is generated correctly each time
  }

  @Test
  @Order(3)
  void shouldDeleteSuccessfully() {
    ResponseEntity<Void> response = restTemplate
        .exchange(Constants.URI.USER + "/" + user.getId(), HttpMethod.DELETE, new HttpEntity<>(user, headers), Void.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    ResponseEntity<UserDto> getResponse = restTemplate
        .exchange(Constants.URI.USER + "/" + user.getId(), HttpMethod.GET, new HttpEntity<>(user, headers), UserDto.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnAnExistingUser() {
    UserDto userDto = Utils.getTestUsers().get(1);
    shouldGetUserDetailsSuccessfully(URI.create(Constants.URI.USER + "/" + userDto.getId()), userDto);
  }

  @Test
  void shouldReturnAllUsersWhenListIsRequested() {
    ResponseEntity<String> response = restTemplate
        .exchange(Constants.URI.USER, HttpMethod.GET, new HttpEntity<>(null, headers),String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

//    DocumentContext documentContext = JsonPath.parse(response.getBody());
//    int cashCardCount = documentContext.read("$.length()");
//    assertThat(cashCardCount).isEqualTo(3);
//
//    JSONArray ids = documentContext.read("$..id");
//    assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);
//
//    JSONArray amounts = documentContext.read("$..amount");
//    assertThat(amounts).containsExactlyInAnyOrder(123.45, 1.00, 150.00);
  }

//  @Test
//  void shouldReturnAPageOfCashCards() {
//    ResponseEntity<String> response = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .getForEntity(Constants.URI.CASH_CARD+"?page=0&size=1", String.class);
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//    DocumentContext documentContext = JsonPath.parse(response.getBody());
//    JSONArray page = documentContext.read("$[*]");
//    assertThat(page.size()).isEqualTo(1);
//  }
//
//  @Test
//  void shouldReturnASortedPageOfCashCards() {
//    ResponseEntity<String> response = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .getForEntity(Constants.URI.CASH_CARD+"/?page=0&size=1&sort=amount,desc", String.class);
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//    DocumentContext documentContext = JsonPath.parse(response.getBody());
//    JSONArray read = documentContext.read("$[*]");
//    assertThat(read.size()).isEqualTo(1);
//
//    double amount = documentContext.read("$[0].amount");
//    assertThat(amount).isEqualTo(150.00);
//  }
//
//  @Test
//  void shouldReturnASortedPageOfCashCardsWithNoParametersAndUseDefaultValues() {
//    ResponseEntity<String> response = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .getForEntity(Constants.URI.CASH_CARD, String.class);
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//    DocumentContext documentContext = JsonPath.parse(response.getBody());
//    JSONArray page = documentContext.read("$[*]");
//    assertThat(page.size()).isEqualTo(3);
//
//    JSONArray amounts = documentContext.read("$..amount");
//    assertThat(amounts).containsExactly(1.00, 123.45, 150.00);
//  }
//
//  @Test
//  void shouldNotReturnACashCardWhenUsingBadCredentials() {
//    ResponseEntity<String> response = restTemplate
//        .withBasicAuth("BAD-USER", "abc123")
//        .getForEntity(Constants.URI.CASH_CARD+"/99", String.class);
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//
//    response = restTemplate
//        .withBasicAuth("sarah1", "BAD-PASSWORD")
//        .getForEntity(Constants.URI.CASH_CARD+"/99", String.class);
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//  }
//
//  @Test
//  void shouldRejectUsersWhoAreNotCardOwners() {
//    ResponseEntity<String> response = restTemplate
//        .withBasicAuth("hank-owns-no-cards", "qrs456")
//        .getForEntity(Constants.URI.CASH_CARD+"/99", String.class);
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
////    TODO: Implement RBAC in security policy
//  }
//
//  @Test
//  void shouldNotAllowAccessToCashCardsTheyDoNotOwn() {
//    ResponseEntity<String> response = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .getForEntity(Constants.URI.CASH_CARD+"/102", String.class); // kumar2's data
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//  }
//
//  @Test
//  @DirtiesContext
//  void shouldUpdateAnExistingCashCard() {
//    CashCard cashCardUpdate = new CashCard(null, 19.99, null);
//    HttpEntity<CashCard> request = new HttpEntity<>(cashCardUpdate);
//    ResponseEntity<Void> response = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .exchange(Constants.URI.CASH_CARD+"/99", HttpMethod.PUT, request, Void.class);
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//
//    ResponseEntity<String> getResponse = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .getForEntity(Constants.URI.CASH_CARD+"/99", String.class);
//    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//    DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
//    Number id = documentContext.read("$.id");
//    Double amount = documentContext.read("$.amount");
//    assertThat(id).isEqualTo(99);
//    assertThat(amount).isEqualTo(19.99);
//  }
//
//  @Test
//  void shouldNotUpdateACashCardThatDoesNotExist() {
//    CashCard unknownCard = new CashCard(null, 19.99, null);
//    HttpEntity<CashCard> request = new HttpEntity<>(unknownCard);
//    ResponseEntity<Void> response = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .exchange(Constants.URI.CASH_CARD+"/99999", HttpMethod.PUT, request, Void.class);
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//  }
//
//  @Test
//  @DirtiesContext
//  void shouldDeleteAnExistingCashCard() {
//    ResponseEntity<Void> response = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .exchange(Constants.URI.CASH_CARD+"/99", HttpMethod.DELETE, null, Void.class);
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//
//    ResponseEntity<String> getResponse = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .getForEntity(Constants.URI.CASH_CARD+"/99", String.class);
//
//    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//  }
//
//  @Test
//  void shouldNotDeleteACashCardThatDoesNotExist() {
//    ResponseEntity<Void> deleteResponse = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .exchange(Constants.URI.CASH_CARD+"/99999", HttpMethod.DELETE, null, Void.class);
//    assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//  }
//
//  @Test
//  void shouldNotAllowDeletionOfCashCardsTheyDoNotOwn() {
//    ResponseEntity<Void> deleteResponse = restTemplate
//        .withBasicAuth("sarah1", "abc123")
//        .exchange(Constants.URI.CASH_CARD+"/102", HttpMethod.DELETE, null, Void.class);
//    assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//    ResponseEntity<String> getResponse = restTemplate
//        .withBasicAuth("kumar2", "xyz789")
//        .getForEntity(Constants.URI.CASH_CARD+"/102", String.class);
//    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//  }
}
