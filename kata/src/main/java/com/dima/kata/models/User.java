package com.dima.kata.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 2, max = 50, message = "Имя пользователя должно быть от 2 до 50 символов")
    private String username;

    @Column(name = "age", nullable = false)
    @NotNull(message = "Возраст обязателен")
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Max(value = 150, message = "Возраст должен быть реалистичным")
    private Integer age;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Пароль обязателен")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    public User() {};
    public User(String username, Integer age, String email, String password) {
        this.username = username;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    @Override
    @Transactional(readOnly=true)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>(roles);
    }

    @Override
    public String getPassword() { return password;}
    public void setPassword(String password) { this.password = password;}

    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return true;}

    @Override
    public boolean isAccountNonLocked() { return true;}

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }

    @Override
    public boolean isEnabled() { return true; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }

}
