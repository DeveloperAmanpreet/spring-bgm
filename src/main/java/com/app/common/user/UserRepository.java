package com.app.common.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Table("users")
public interface UserRepository extends JpaRepository<User, Long> {
  User getUserByUsername(String username);

  Boolean existsByEmail(String email);

  Optional<User> findByUsernameOrEmail(String username, String email);

}
