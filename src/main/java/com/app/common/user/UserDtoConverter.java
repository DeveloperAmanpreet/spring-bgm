package com.app.common.user;

import com.app.common.Role.Role;
import org.modelmapper.AbstractConverter;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDtoConverter extends AbstractConverter<UserDto, User> {

  protected Collection<Role> convertRoles(Collection<String> roles) {
    return roles
        .stream()
        .map(role -> {
          Role newRole = new Role();
          newRole.setName(role);
          return newRole;
        })
        .collect(Collectors.toList());
  }

  @Override
  protected User convert(UserDto userDto) {
    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setEmail(userDto.getEmail());
    user.setName(userDto.getName());
    user.setStartTime(userDto.getStartTime());
    user.setId(userDto.getId());
    user.setHashword(userDto.getHashword());
    user.setRoles(convertRoles(userDto.getRoles()));

    return user;
  }
}