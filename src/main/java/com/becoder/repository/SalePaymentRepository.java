package com.becoder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.becoder.entity.SalePayment;

@Repository
public interface SalePaymentRepository extends JpaRepository<SalePayment, Long> {

	List<SalePayment> findBySale_IdAndSale_Pharmacy_Id(Long saleId, Long pharmacyId);

}
