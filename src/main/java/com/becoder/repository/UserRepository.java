
package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
    
    List<User> findByPharmacyId(Long pharmacyId);

    Optional<User> findByIdAndPharmacyId(Long id, Long pharmacyId);
}
