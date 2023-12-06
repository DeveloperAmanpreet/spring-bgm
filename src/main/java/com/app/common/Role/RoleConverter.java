package com.app.common.Role;

import org.modelmapper.AbstractConverter;

public class RoleConverter extends AbstractConverter<Role, String> {
  @Override
  protected String convert(Role role) {
    return role.getName();
  }
}