/**
 *   Interface for implementing Registration service endpoint for the users to register themselves
 */
package com.ngxGeoBI.accounts.services;

import com.ngxGeoBI.accounts.model.User;

import java.util.Optional;

public interface UserService {
    void save(User user);
    Optional<User> findByUsername(String username);
}
