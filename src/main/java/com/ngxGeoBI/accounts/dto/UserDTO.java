/**
 * Created by : Sukesh Laghate
 * Created on : 06-12-2017
 **/

package com.ngxGeoBI.accounts.dto;

import lombok.Data;

@Data
public class UserDTO {
    //    @Data annotation will take care of generating the appropriate setters and getters
    private String username;
    private String password;
    private String confirmpassword;
    private String first_name;
    private String last_name;
    private String email;

    public UserDTO(){
        //default
    }

    public UserDTO(String _username, String _password, String _confirmpassword,String _first_name, String _last_name, String _email){
        username = _username;
        password = _password;
        confirmpassword = _confirmpassword;
        first_name = _first_name;
        last_name = _last_name;
        email = _email;
    }
}
