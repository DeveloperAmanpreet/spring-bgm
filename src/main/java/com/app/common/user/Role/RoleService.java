package com.app.common.user.Role;

import org.springframework.stereotype.Service;

@Service
public class RoleService {
  private final RoleRepository roleRepository;

  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public Role getById(Long id) {
    return roleRepository.getReferenceById(id);
  }

  public Role create(Role role) {
    return roleRepository.save(role);
  }

  public void delete(Long id) {
    roleRepository.deleteById(id);
    return;
  }
}
