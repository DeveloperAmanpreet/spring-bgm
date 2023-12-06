package com.app.preRegister;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreRegisterRepo extends JpaRepository<PreRegister, Long> {

  boolean existsByEmailIgnoreCase(String email);
}
