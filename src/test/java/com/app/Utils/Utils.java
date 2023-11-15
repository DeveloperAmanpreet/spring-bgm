package com.app.Utils;

import com.app.common.user.Role.Role;
import com.app.common.user.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {

  static List<User> userList;
  static List<Role> roleList;

  static String token;
  static PasswordEncoder passwordEncoder;

  public static String getToken() {
    return token;
  }

  public static void setToken(String token) {
    Utils.token = token;
  }

  public static void createTestData() {
    getTestRoles();
    getTestUsers();
  }

  public static List<User> getTestUsers() {

    if(userList == null) {
      userList = new ArrayList<>();

      for (int i = 1; i < 10; i++) {
        Role role = roleList.get(i % 3);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = new User(((long) i), "name_" + i, "username_" + i, "username_"+i+ "@email.com", "hashword_" + i, Instant.now(), roles);
        if (i == 1) {
          user.setUsername("amanpreetsingh");
        }

        userList.add(user);
      }
    }

    return userList;
  }

  public static PasswordEncoder getPasswordEncoder(){
      if(passwordEncoder == null) {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
      }
      return passwordEncoder;
  }

  public static List<Role> getTestRoles() {
    if (roleList == null) {
      roleList = new ArrayList<>();

      Role roleAdmin = new Role();
      roleAdmin.setId(1L);
      roleAdmin.setName("ADMINTEST");
      roleAdmin.setDescription("Admin Test Role");
      roleList.add(roleAdmin);

      Role roleUser = new Role();
      roleUser.setId(2L);
      roleUser.setName("USERTEST");
      roleUser.setDescription("User Test Role");
      roleList.add(roleUser);

      Role roleGuestTest = new Role();
      roleGuestTest.setId(3L);
      roleGuestTest.setName("GUESTTEST");
      roleGuestTest.setDescription("Guest Test Role");
      roleList.add(roleGuestTest);

//      Role roleGuest = new Role();
//      roleGuest.setId(3L);
//      roleGuest.setName("GUEST");
//      roleGuest.setDescription("Guest Role");
//      roleList.add(roleGuest);
    }

    return roleList;
  }

  public static String encode(String password) {
    return getPasswordEncoder().encode(password);
  }

  public static void compareRole(Role actual, Role expected) {

  }

  public static void setUserList(List<User> userList) {
    Utils.userList = userList;
  }
}
