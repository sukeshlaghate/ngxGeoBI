package com.ngxGeoBI.accounts.repository;

import com.ngxGeoBI.accounts.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
        Optional<Profile> findProfileByUser_Username(String username);
        Optional<Profile> findProfileByUser_Id(Long id);
}
