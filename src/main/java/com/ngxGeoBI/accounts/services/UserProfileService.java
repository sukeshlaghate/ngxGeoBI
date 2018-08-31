package com.ngxGeoBI.accounts.services;

import com.ngxGeoBI.accounts.model.Profile;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileService {
    void updateByUserId(Profile updatedProfile, Long user_id);
    void updateByUsername(Profile updatedProfile, String userName);
    void save(Profile profile);
    // void save(UserProfileDTO profile);
    Optional<Profile> findByProfileId(@Param("Id") Long profileId);
    Optional<Profile> findByUsername(@Param("user") String Username);
    Optional<Profile> findByUserId(@Param("user") Long userId);
}
