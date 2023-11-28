package com.app.common.user.Role;


import com.app.common.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  String name;
  String description;

  @ManyToMany(mappedBy = "roles")
  private Collection<User> users;

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
