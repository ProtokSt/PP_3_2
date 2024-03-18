package ru.kata.spring.boot_security.demo.models;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Заполните поле имя, оно же будет использоваться как логин")
    @Size(min = 2, max = 15, message = "Введите от 2 до 15 символов")
    @Column(name = "username", length = 15)
    private String username;

    @Column(name = "password", length = 15)
    @Size(min = 2, max = 15, message = "Введите от 2 до 15 символов")
    private String password;

    @Column(name = "department", length = 15)
    @Size(min = 2, max = 15, message = "Введите от 2 до 15 символов")
    private String department;

    @Column(name = "salary")
    private int salary;

    // исключение Type ALL и REMOVE нужно, чтобы корректно обрабатывалось удаление ролей или пользователей. Трегулов.
    // чтобы удаление роли не задевало связаных пользователей и наоборот.
    @ManyToMany(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;     // нет смысла в двух одинаковых ролях. возможно переделать на Set

    public User() {
    }

    public User(String username, String password, String department, int salary) {
        this.username = username;
        this.password = password;
        this.department = department;
        this.salary = salary;
    }

    public User(String username, String password, String department, int salary, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.department = department;
        this.salary = salary;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }

    //// UserDetails - Паттерн Spring Security - обёртка над сущностью
    //// Использование внутреннего интерфейса для стандартизации получения данных из сущности
    //// В данном случае встраиваем внутренний интерфейс в саму сущность через реализацию методов
    //// второе главное назначение - возвращать роли=доступные действия, Authorities
    //// хотя судя по "правильности" вынесения инициации ролей в отдельный утиль класс,
    //// реализацию UserDetails тоже лучше делать отдельным классом

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
