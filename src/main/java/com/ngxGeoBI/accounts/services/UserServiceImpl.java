/**
 * Created by : Sukesh Laghate
 * Created on : 28-12-2017
 * Concrete  implementation of the registration serivces used by Registration endpoint
 **/
package com.ngxGeoBI.accounts.services;

import com.ngxGeoBI.accounts.Helper;
import com.ngxGeoBI.accounts.model.Profile;
import com.ngxGeoBI.accounts.model.Role;
import com.ngxGeoBI.accounts.model.User;
import com.ngxGeoBI.accounts.repository.ProfileRepository;
import com.ngxGeoBI.accounts.repository.RoleRepository;
import com.ngxGeoBI.accounts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void save(User user) {

        user.setPassword(Helper.PASSWORD_ENCODER.encode(user.getPassword()));

        Set<Role> roleSet = new HashSet<Role>();
        //Default role is that of Analyst
        Optional<Role> userRole = roleRepository.findByRole("Analyst");
        if (userRole.isPresent()) {
            roleSet.add(userRole.get());
        }
        user.setRoles(roleSet);
        userRepository.save(user);

        //create an initial empty profile
        Profile profile = new Profile(user);
        Profile savedProfile = profileRepository.save(profile);
        user.setUserProfile(savedProfile);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
