package com.becoder.service;

import java.util.List;

import com.becoder.dto.SaleBatchResponse;
import com.becoder.dto.SaleMedicineSearchResponse;
import com.becoder.dto.SalePreviewRequest;
import com.becoder.dto.SalePreviewResponse;
import com.becoder.dto.SaleRequest;
import com.becoder.dto.SaleResponse;

public interface SaleService {

	SaleResponse createSale(SaleRequest request);

    List<SaleResponse> getAllSales();

    SaleResponse getSaleById(Long saleId);

    SaleResponse cancelSale(Long saleId);
	
    List<SaleMedicineSearchResponse> searchMedicines(String keyword);
    
    List<SaleBatchResponse> getAvailableBatches(Long medicineId);
    
    SalePreviewResponse previewSale(SalePreviewRequest request);
}
