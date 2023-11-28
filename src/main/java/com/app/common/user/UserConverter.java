package com.app.common.user;

import com.app.common.user.Role.Role;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.AbstractConverter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class UserConverter extends AbstractConverter<User, UserDto> {

  protected List<String> convertRoles(Collection<Role> roles) {

    return roles
        .stream()
        .map(Role::getName)
        .collect(Collectors.toList());
  }

  @Override
  protected UserDto convert(User user) {
    log.info("Converting user: {}", user);
    UserDto userDto = new UserDto();
    userDto.setUsername(user.getUsername());
    userDto.setEmail(user.getEmail());
    userDto.setName(user.getName());
    userDto.setStartTime(user.getStartTime());
    userDto.setId(user.getId());
    userDto.setHashword(user.getHashword());
    userDto.setRoles(convertRoles(user.getRoles()));

    return userDto;
  }
}