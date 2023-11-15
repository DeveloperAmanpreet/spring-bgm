package com.app.common.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Table("users")
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  Boolean existsByEmail(String email);

  Optional<User> findByUsernameOrEmail(String username, String email);

  boolean existsByUsername(String username);
}
