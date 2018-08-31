package com.ngxGeoBI.accounts.repository;

import com.ngxGeoBI.accounts.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByRole(@Param("role") String role);
}
