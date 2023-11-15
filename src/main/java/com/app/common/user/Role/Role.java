package com.app.common.user.Role;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role {

  @Id
  Long id;

  String name;
  String description;

  @Override
  public String toString() {
    return "Role{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object anObject) {
    if (this == anObject) return true;
    if (anObject instanceof Role role) {
      return Objects.equals(role.getId(), id)
          && Objects.equals(role.getName(), name)
          && Objects.equals(role.getDescription(), description);
    }
    return false;
  }
}
