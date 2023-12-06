package com.app.common.user;

import com.app.common.Role.Role;
import com.app.common.Role.RoleService;
import com.app.preRegister.RegisterDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;
  private ModelMapper modelMapper;

  public UserService(
    UserRepository userRepository,
    RoleService roleService,
    PasswordEncoder passwordEncoder,
    ModelMapper modelMapper
  ) {
    this.userRepository = userRepository;
    this.roleService = roleService;
    this.passwordEncoder = passwordEncoder;
    this.modelMapper = modelMapper;
    this.createSuperUser();
  }

  public UserDto getUserById(Long id){
    User user = this.userRepository.findById(id).orElseGet(() -> null);
    if(user == null){
      throw new EntityNotFoundException("User not found with id: " + id);
    }
    return modelMapper.map(user, UserDto.class);
  }

  public UserDto getUserByUsername(String username){
    log.info("Fetching user by username: {}", username);
    try {
      User user = this.userRepository.getUserByUsername(username);
      if(user != null){
        return modelMapper.map(user, UserDto.class);
      } else{
        log.error("User not found: {}", username);
        throw new EntityNotFoundException("User not found: " + username);
      }
    } catch (Exception e){
      log.error("Error while fetching user: {} , Error: {}", username, e.getMessage());
      throw new EntityNotFoundException("User not found: " + username);
    }
  }

  public UserDto createUser(UserDto userDto){
    User user = modelMapper.map(userDto, User.class);
    TypeMap<UserDto, User> typeMap = modelMapper.getTypeMap(UserDto.class, User.class);
    populateRolesFromDB(user);
    processUser(user);
    User savedUser =  this.userRepository.save(user);
    return modelMapper.map(savedUser, UserDto.class);
  }

  private void processUser(User user) {
    user.setUsername(user.getUsername().toLowerCase());
  }

  private void populateRolesFromDB(User user) {
    List<Role> roles = new ArrayList<>();
    for (Role role : user.getRoles()) {
      Role fetchedRole = roleService.getRoleByName(role.getName());
      if(fetchedRole == null) {
        throw new EntityNotFoundException("Role not found with name: " + role.getName());
      }
      roles.add(fetchedRole);
    }
    user.setRoles(roles);
  }

  public void deleteById(Long id) {
    if(this.userRepository.existsById(id)) {
      this.userRepository.deleteById(id);
    } else {
      throw new EntityNotFoundException("User not found with id: " + id);
    }
  }

  public List<UserDto> getAllUsers() {
    List<User> user = this.userRepository.findAll();
    return user.stream().map(user1 -> modelMapper.map(user1, UserDto.class)).collect(Collectors.toList());
  }

  public void setModelMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  private void createSuperUser() {
    User user = new User();
    user.setName("Amanpreet Singh");
    user.setUsername("ADMIN_SUPER");
    user.setEmail("admin@mail.com");
    user.setHashword(this.createHashword("dipper&^%123"));
    user.setStartTime(Instant.now());
    List<Role> roles = new ArrayList<>();
    Role role = new Role();
    role.setName("ADMIN_SUPER");
    role.setDescription("ADMIN_DESC");

    try {
      Role exits = roleService.getRoleByName(role.getName());
      if (exits == null){
        roleService.create(role);
        roles.add(role);
      } else {
        roles.add(exits);
      }
      user.setRoles(roles);
      this.userRepository.save(user);
    } catch (Exception e) {
      log.info("Error creating super user: {}", e.getMessage());
    }
  }

  String createHashword(String password) {
    return passwordEncoder.encode(password);
  }
}
