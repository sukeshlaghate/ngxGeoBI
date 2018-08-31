package com.ngxGeoBI.accounts.services;

public interface SecurityService {

    String findLoggedInUsername();

    void autologin(String username, String password);
}
