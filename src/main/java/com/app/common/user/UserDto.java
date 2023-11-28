package com.app.common.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
  Long id;
  String name;
  private String username;
  private String email;
  String hashword;
  Instant startTime;
  Collection<String> roles;

  @Override
  public boolean equals(Object anObject) {
    if(this == anObject) return true;
    if(anObject instanceof UserDto userDto) {
      return Objects.equals(userDto.getId(), id)
        && Objects.equals(userDto.getName(), name)
        && Objects.equals(userDto.getUsername(), username)
        && Objects.equals(userDto.getStartTime(), startTime);
    }
    return false;
  }
}
