package com.ngxGeoBI.accounts.repository;

import com.ngxGeoBI.accounts.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
     Optional<User> findByUsername(@Param("user") String Username);
}
