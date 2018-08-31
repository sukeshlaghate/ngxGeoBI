package com.ngxGeoBI.accounts.model;


import javax.persistence.*;

import java.util.Collection;
//import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ngxGeoBI.accounts.dto.RegisterUserDTO;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.ngxGeoBI.accounts.Helper;



@Data
@Entity
@Table(name="users")
@ToString(exclude = {"password","passwordConfirm"})
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue
    @Column( nullable = false)
    private Long id;

    @Column(unique = true)
    @NotEmpty
    @Length(min = Helper.MIN_LENGTH_USERNAME, max = Helper.MAX_LENGTH_USERNAME)
    private String username;


    @Column
    @NotEmpty
    @Length(min = Helper.MIN_LENGTH_PASSWORD)
    private String password;


    @Transient
    private String passwordConfirm;

    @Column
    @NotEmpty
    private String email;

    @Column(name = "first_name")
    @NotEmpty
    @Length(min = Helper.MIN_LENGTH_FIRST_NAME, max = Helper.MAX_LENGTH_FIRST_NAME)
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    @Length(min = Helper.MIN_LENGTH_LAST_NAME, max = Helper.MAX_LENGTH_LAST_NAME)
    private String lastName;

    @Column(name = "account_expired")
    private boolean accountExpired = false;

    @Column(name = "account_locked")
    private boolean accountLocked = false;

    @Column(name = "credentials_expired")
    private boolean credentialsExpired = false;

    @Column
    private boolean enabled = true;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Collection<Role> roles;

    // include @JsonBackReference to avoid infinite recurssion when writing the output to JSON
    // refer SO question
    // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="profile_id")
    @JsonBackReference
    private Profile userProfile;

//    @SuppressWarnings("unused")
//    public User(){
//        // JPA Only
//        this.roles = new LinkedHashSet<>();
//
//    }

    public User(String name, String firstName, String lastName, String password, String passwordConfirm, Set<Role> roles){
        this.username = name;
        this.firstName = firstName;
        this.lastName = lastName;

        this.setPassword(password);
        this.email= "";
        this.passwordConfirm = passwordConfirm;

        this.roles = roles;

    }

    // overridden constructor to support population from DTO object
    @SuppressWarnings("unused")
    public User(RegisterUserDTO userDto){
        this.username = userDto.getUsername();
        this.setPassword(userDto.getPassword());
        this.firstName = userDto.getFirst_name();
        this.lastName = userDto.getLast_name();
        this.email = userDto.getEmail();
    }

    // public void setPassword(String password) {    }

    public boolean hasRole(String targetRole){
        if(targetRole == null)
            return false;

        if(roles == null){
            logger.debug("Roles not set for user " + this);
            return false;
        }
        for(Role role: roles){
            if(targetRole.equals(role.getRole())){
                return true;
            }
        }
        // no matching roles found
        return false;
    }

//    public void setRoles(LinkedHashSet<Role> roles) {
//        this.roles = roles;
//    }


//    public LinkedHashSet<Role> getRoles() {
//        return roles;
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
