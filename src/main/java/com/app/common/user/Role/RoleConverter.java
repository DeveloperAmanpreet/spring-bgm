package com.app.common.user.Role;

import org.modelmapper.AbstractConverter;

public class RoleConverter extends AbstractConverter<Role, String> {
  @Override
  protected String convert(Role role) {
    return role.getName();
  }
}