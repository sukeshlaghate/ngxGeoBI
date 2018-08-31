package com.ngxGeoBI.accounts.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue
    Long Id;

    private String role;

//    @OneToMany(cascade=CascadeType.ALL)
//    @JoinTable(name="user_roles",
//            joinColumns={@JoinColumn(name="role_id", referencedColumnName="id")},
//            inverseJoinColumns={@JoinColumn(name="user_id", referencedColumnName="id")})
//    private List<User> userList;

    private Role(){
        // JPA Only
    }

    public Role(String role){
        this.role = role;
    }

    @Override
    public String toString(){
        return role;
    }

    @Override
    public boolean equals(Object obj){

        if(this == obj)
            return true;

        if(obj == null)
            return false;

        if(getClass() != obj.getClass())
            return false;

        Role other = (Role) obj;
        if(role == null) {
            if(other.role !=null)
                return false;
        } else if (!role.equals(other.role))
            return false;

        return true;
    }
}
