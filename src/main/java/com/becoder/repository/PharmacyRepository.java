package com.becoder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.Pharmacy;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

	Optional<Pharmacy> findByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCase(String email);

	Optional<Pharmacy> findByIdAndActiveTrue(Long id);

}
