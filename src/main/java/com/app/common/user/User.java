package com.app.common.user;

import com.app.common.user.Role.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

// https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/howto-database-initialization.html

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    String hashword;

    @Column(name = "start_time")
    Instant startTime;

  @ManyToMany
  @JoinTable(
      name = "users_role",
      joinColumns = @JoinColumn(
          name = "id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles;

  /*
  // Multiple Roles
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;
   */

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", hashword='" + hashword + '\'' +
                ", startTime=" + startTime +
                ", role=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object anObject) {
      if(this == anObject) return true;
      if(anObject instanceof User user) {
        return Objects.equals(user.getId(), id)
            && Objects.equals(user.getName(), name)
            && Objects.equals(user.getUsername(), username)
            && Objects.equals(user.getStartTime(), startTime);
      }
      return false;
    }

    private boolean equalsRole(Collection<Role> other) {
        if (this.getRoles().size() == other.size()){
          for(Role role : this.getRoles()){
              if(!other.contains(role)){
                  return false;
              }
          }
          return true;
      }
      return false;
    }
}
