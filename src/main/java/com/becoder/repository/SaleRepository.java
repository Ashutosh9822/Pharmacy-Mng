
package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import com.becoder.entity.Sale;

import jakarta.persistence.LockModeType;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	List<Sale> findByPharmacy_IdOrderByCreatedOnDesc(Long pharmacyId);

	Optional<Sale> findByIdAndPharmacy_Id(Long id, Long pharmacyId);

	boolean existsByPharmacy_IdAndInvoiceNumber(Long pharmacyId, String invoiceNumber);

	List<Sale> findByCustomer_IdAndPharmacy_IdOrderByCreatedOnDesc(Long customerId, Long pharmacyId);

	// Lock sale during cancellation to prevent duplicate stock restoration.
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			SELECT s FROM Sale s
			WHERE s.id = :saleId
			  AND s.pharmacy.id = :pharmacyId
			""")
	Optional<Sale> findByIdAndPharmacyIdForUpdate(@Param("saleId") Long saleId, @Param("pharmacyId") Long pharmacyId);
	
	
	
}
