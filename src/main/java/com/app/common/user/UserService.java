package com.app.common.user;

import com.app.common.user.Role.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@Slf4j
public class UserService {

  private final UserRepository userRepository;


  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  public User getUserById(Long id){
    return this.userRepository.findById(id).orElseGet(() -> null);
  }

  public User getUserByUsername(String username){
    return this.userRepository.findByUsername(username);
  }

  public User createUser(User user){
    return this.userRepository.save(user);
  }


  public void deleteByid(Long id){
    if(this.userRepository.existsById(id)){
      this.userRepository.deleteById(id);
    } else {
      throw new EntityNotFoundException("User not found with id: " + id);
    }
  }

  public List<User> getAllUsers() {
    return this.userRepository.findAll();
  }
}
