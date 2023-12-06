package com.app.preRegister;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PreRegister {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  @Column(unique = true)
  String email;

  boolean status;
}
