package com.app.common.user.Role;


import com.app.common.user.Constants;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(Constants.URI.ROLE)
public class RoleController {


  private final RoleRepository roleRepository;

  public RoleController(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @GetMapping("/{id}")
  public Role getById(Long id) {
    return roleRepository.getReferenceById(id);
  }

  @PostMapping
  public ResponseEntity<Void> createNewUser(@RequestBody Role role, UriComponentsBuilder ucb) {
    Role savedRole = roleRepository.save(role);

    log.info("Saved role: {}", savedRole);
    URI locationOfNewCashCard = ucb
        .path(Constants.URI.ROLE+"/{id}")
        .buildAndExpand(savedRole.getId())
        .toUri();
    return ResponseEntity.created(locationOfNewCashCard).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateRole(@PathVariable Long id, Role role) {
    return ResponseEntity.ok("Updated role: " + id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteRole(@PathVariable Long id) {
    log.info("Deleting role: {}", id);
    try {
      roleRepository.deleteById(id);
    } catch (EntityNotFoundException e) {
      log.error("Entity not found: {}", id);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (Exception e) {
      log.info("Error deleting role: {}", id);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    log.info("Deleted Successfully: {}", id);
    return ResponseEntity.ok("Deleted role: " + id);
  }
}
