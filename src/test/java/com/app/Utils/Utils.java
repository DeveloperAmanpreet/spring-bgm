package com.app.Utils;

import com.app.common.user.Role.Role;
import com.app.common.user.Role.RoleDto;
import com.app.common.user.User;
import com.app.common.user.UserConverter;
import com.app.common.user.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {

  private static ModelMapper modelMapper;

  public static ModelMapper getModelMapper() {
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    modelMapper.addConverter(new UserConverter());

    return modelMapper;
  }

  static List<UserDto> userList;
  static List<RoleDto> roleList;

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

  public static List<UserDto> getTestUsers() {

    if(userList == null) {
      userList = new ArrayList<>();

      for (int i = 1; i < 10; i++) {
        RoleDto roleDto = roleList.get(i % 3);
        List<String> roles = new ArrayList<>();
        roles.add(roleDto.getName());
        UserDto userDto = new UserDto(((long) i), "name_" + i, "username_" + i, "username_"+i+ "@email.com", "hashword_" + i, Instant.now(), roles);
        if (i == 1) {
          userDto.setUsername("amanpreetsingh");
        }

        userList.add(userDto);
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

  public static List<RoleDto> getTestRoles() {
    if (roleList == null) {
      roleList = new ArrayList<>();

      RoleDto roleAdmin = new RoleDto();
      roleAdmin.setId(1L);
      roleAdmin.setName("ADMINTEST");
      roleAdmin.setDescription("Admin Test Role");
      roleList.add(roleAdmin);

      RoleDto roleUser = new RoleDto();
      roleUser.setId(2L);
      roleUser.setName("USERTEST");
      roleUser.setDescription("User Test Role");
      roleList.add(roleUser);

      RoleDto roleGuestTest = new RoleDto();
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

  public static void setUserList(List<UserDto> userList) {
    Utils.userList = userList;
  }
}
