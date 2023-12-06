package com.app.preRegister;


import org.springframework.stereotype.Service;

@Service
public class PreRegisterService {

  PreRegisterRepo preRegisterRepo;

  public PreRegisterService(PreRegisterRepo preRegisterRepo) {
    this.preRegisterRepo = preRegisterRepo;
  }

  public boolean existsByEmailIgnoreCase(String email) {
    return preRegisterRepo.existsByEmailIgnoreCase(email);
  }

  public boolean registerUser(String  email) throws Exception {
    PreRegister preRegister = new PreRegister();
    preRegister.setEmail(email);
    preRegister.setStatus(true);
    if(preRegisterRepo.existsByEmailIgnoreCase(email)) {
      throw new IllegalArgumentException("Email already exists");
    }
    preRegisterRepo.save(preRegister);
    return true;
  }
}
