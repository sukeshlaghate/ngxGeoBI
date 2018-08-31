package com.ngxGeoBI.ngxGeoBI;

import com.ngxGeoBI.accounts.Helper;
import com.ngxGeoBI.accounts.model.Profile;
import com.ngxGeoBI.accounts.model.Role;
import com.ngxGeoBI.accounts.model.User;
import com.ngxGeoBI.accounts.repository.ProfileRepository;
import com.ngxGeoBI.accounts.repository.RoleRepository;
import com.ngxGeoBI.accounts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * THis class is responsible for doing an early setup of Roles and an super user
 * setup int he system.
 * We will tie this to the startup of the application and we will use an
 * ApplicationListerner on ContextRefreshedEvent to load our intial data on server start
 */

@Component
public class ApplicationInitializer
        implements ApplicationListener<ContextRefreshedEvent> {

    private static boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProfileRepository profileRepository;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        // nothing to do if already setup
        if (alreadySetup)
            return;

        Role super_user_role = createRoleIfNotFound("Root");
        Role admin_user_role = createRoleIfNotFound("Admin");
        Role analyst_user_role = createRoleIfNotFound("Analyst");
        Role user_role = createRoleIfNotFound("User");

        User super_user = createSuperUserIfNotFound("super.user", super_user_role);

        alreadySetup = admin_user_role != null && super_user_role !=null
                        && user_role != null && super_user !=null;

    }

    @Transactional
    public User createSuperUserIfNotFound(String username, Role user_role) {
        Optional<User> user = userRepository.findByUsername(username);
        Set<Role>authorities = new HashSet<Role>();
        authorities.add(user_role);
        if( !user.isPresent()){
            user = Optional.of( new User(username,"super", "user",
                    "Root@123", "Root@123", authorities));
            User userObj = user.get();
            userObj.setEmail("root@root.com");
            userObj.setPassword(Helper.PASSWORD_ENCODER.encode(userObj.getPassword()));
            // userRepository.save(userObj);

            // create an initial empty profile
            Profile profile = new Profile(userObj);
            Profile savedProfile = profileRepository.save(profile);
            userObj.setUserProfile(savedProfile);
            userRepository.save(userObj);
        }
        return user.get();
    }

    @Transactional
    public Role createRoleIfNotFound(String roleName) {
        Optional<Role> role = roleRepository.findByRole(roleName);
        if (!role.isPresent()) {
            role = Optional.of(new Role(roleName));
            roleRepository.save(role.get());
        }
        return role.get();
    }
}
