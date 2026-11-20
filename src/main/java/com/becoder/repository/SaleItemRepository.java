package com.becoder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.becoder.entity.SaleItem;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

	List<SaleItem> findBySale_IdAndSale_Pharmacy_Id(Long saleId, Long pharmacyId);

}
