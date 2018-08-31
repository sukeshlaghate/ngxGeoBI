package com.ngxGeoBI.accounts.services;

import com.ngxGeoBI.accounts.model.Role;
import com.ngxGeoBI.accounts.model.User;
import com.ngxGeoBI.accounts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByUsername(username);
        User validUser = null;
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if(user.isPresent()){
            validUser = user.get();
            for (Role role : validUser.getRoles()){
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
            }
            return new org.springframework.security.core.userdetails.User(validUser.getUsername(), validUser.getPassword(), grantedAuthorities);

        } else
            throw  new UsernameNotFoundException("Bad credentials provided");

    }
}
