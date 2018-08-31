package com.ngxGeoBI.accounts.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.MediaType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@Entity
@Table(name= "user_profiles")
@ToString
@Setter
@Getter
public class Profile implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    @OneToOne(mappedBy="userProfile")
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private User user;

//    @Column(name = "has_avatar")
//    private boolean hasAvatar = false;

    @Column
    private String address;

    @Column
    private String address2;

    @Column
    private String address3;

    @Column
    private String City;

    @Column
    @Size(min=2)
    private String state;

    @Column
    @Size(min=5, max=10)
    private String pin;

    @Column
    private String phone;

    @Column
    private String blog_link;

    @Column
    private String linkedin;

    @Lob
    @Column
    private byte[] avatar;

    private MediaType mediaType;


    public Profile() {
    }

    public Profile(User user) {
        this.user = user;
    }



}
