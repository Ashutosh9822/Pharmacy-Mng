package com.becoder.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.BatchAllocationResponse;
import com.becoder.dto.SaleBatchResponse;
import com.becoder.dto.SaleItemResponse;
import com.becoder.dto.SaleMedicineSearchResponse;
import com.becoder.dto.SalePaymentResponse;
import com.becoder.dto.SalePreviewRequest;
import com.becoder.dto.SalePreviewResponse;
import com.becoder.dto.SaleRequest;
import com.becoder.dto.SaleResponse;
import com.becoder.entity.Customer;
import com.becoder.entity.Medicine;
import com.becoder.entity.MedicineBatch;
import com.becoder.entity.Pharmacy;
import com.becoder.entity.Sale;
import com.becoder.entity.SaleItem;
import com.becoder.entity.SalePayment;
import com.becoder.entity.SaleStatus;
import com.becoder.entity.StockMovement;
import com.becoder.entity.StockMovementType;
import com.becoder.repository.CustomerRepository;
import com.becoder.repository.MedicineBatchRepository;
import com.becoder.repository.MedicineRepository;
import com.becoder.repository.SaleItemRepository;
import com.becoder.repository.SalePaymentRepository;
import com.becoder.repository.SaleRepository;
import com.becoder.repository.StockMovementRepository;
import com.becoder.service.CurrentUserService;
import com.becoder.service.SaleService;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {

	private static final BigDecimal ZERO = BigDecimal.ZERO;
	private static final BigDecimal HUNDRED = new BigDecimal("100");

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private SaleItemRepository saleItemRepository;

	@Autowired
	private SalePaymentRepository salePaymentRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private MedicineRepository medicineRepository;

	@Autowired
	private MedicineBatchRepository medicineBatchRepository;

	@Autowired
	private StockMovementRepository stockMovementRepository;

	@Autowired
	private CurrentUserService currentUserService;

	/*
	 * Internal object representing the quantity allocated from one particular
	 * batch.
	 */
	private static class BatchAllocation {

		private Medicine medicine;
		private MedicineBatch batch;
		private Integer quantity;
		private BigDecimal unitPrice;
		private BigDecimal gstRate;
		private String hsnCode;
		private BigDecimal grossAmount;
		private BigDecimal discountAmount;
		private BigDecimal taxAmount;
		private BigDecimal lineTotal;

		public BatchAllocation(Medicine medicine, MedicineBatch batch, Integer quantity) {

			this.medicine = medicine;
			this.batch = batch;
			this.quantity = quantity;
			this.unitPrice = batch.getSellingPrice();
			this.gstRate = medicine.getGstRate();
			this.hsnCode = medicine.getHsnCode();

			this.grossAmount = unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);

			this.discountAmount = ZERO;
			this.taxAmount = ZERO;
			this.lineTotal = ZERO;
		}
	}

	@Override
	public SaleResponse createSale(SaleRequest request) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();
		Long userId = currentUserService.getCurrentUser().getId();

		// 1. Validate optional customer.
		Customer customer = null;

		if (request.getCustomerId() != null) {
			customer = customerRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(request.getCustomerId(), pharmacyId)
					.orElseThrow(() -> new EntityNotFoundException("Customer not found in your pharmacy."));
		}

		// 2. Validate discount.
		BigDecimal discount = request.getDiscountAmount() == null ? ZERO
				: request.getDiscountAmount().setScale(2, RoundingMode.HALF_UP);

		if (discount.compareTo(ZERO) < 0) {
			throw new IllegalArgumentException("Discount amount cannot be negative.");
		}

		// 3. Prepare all batch allocations.
		List<BatchAllocation> allocations = new ArrayList<>();

		for (var itemRequest : request.getItems()) {

			Medicine medicine = medicineRepository
					.findByIdAndPharmacy_IdAndIsDeletedFalse(itemRequest.getMedicineId(), pharmacyId)
					.orElseThrow(() -> new EntityNotFoundException(
							"Medicine not found in your pharmacy: " + itemRequest.getMedicineId()));

			if (!medicine.isActive()) {
				throw new IllegalArgumentException("Medicine is inactive: " + medicine.getName());
			}

			if (medicine.getGstRate() == null || medicine.getGstRate().compareTo(ZERO) < 0) {
				throw new IllegalArgumentException("Invalid GST rate configured for " + medicine.getName());
			}

			if (itemRequest.getBatchSelectionMode() == null) {
				throw new IllegalArgumentException("Batch selection mode is required.");
			}

			switch (itemRequest.getBatchSelectionMode()) {

			case AUTOMATIC_FEFO:
				allocations.addAll(allocateAutomatically(medicine, itemRequest.getQuantity(), pharmacyId));
				break;

			case MANUAL:
				allocations.addAll(allocateManually(medicine, itemRequest.getQuantity(),
						itemRequest.getManualAllocations(), pharmacyId));
				break;

			default:
				throw new IllegalArgumentException("Unsupported batch selection mode.");
			}
		}

		// 4. Calculate gross subtotal.
		BigDecimal subtotal = allocations.stream().map(a -> a.grossAmount).reduce(ZERO, BigDecimal::add).setScale(2,
				RoundingMode.HALF_UP);

		if (discount.compareTo(subtotal) > 0) {
			throw new IllegalArgumentException("Discount cannot be greater than the subtotal.");
		}

		// 5. Distribute discount proportionally across batch allocations.
		calculateDiscountAndTax(allocations, discount);

		BigDecimal totalTax = allocations.stream().map(a -> a.taxAmount).reduce(ZERO, BigDecimal::add).setScale(2,
				RoundingMode.HALF_UP);

		BigDecimal totalAmount = subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);

		// 6. Validate payment records.
		validatePayments(request, totalAmount);

		// 7. Generate a pharmacy-specific invoice number.
		String invoiceNumber = generateInvoiceNumber(pharmacyId);

		// 8. Create sale.
		Sale sale = new Sale();
		sale.setPharmacy(currentUserService.getCurrentUser().getPharmacy());
		sale.setCustomer(customer);
		sale.setInvoiceNumber(invoiceNumber);
		sale.setSubtotal(subtotal);
		sale.setDiscountAmount(discount);
		sale.setTaxAmount(totalTax);
		sale.setTotalAmount(totalAmount);
		sale.setSaleStatus(SaleStatus.COMPLETED);
		sale.setCreatedBy(userId);

		sale = saleRepository.save(sale);

		// 9. Save sale items, deduct stock and record movements.
		for (BatchAllocation allocation : allocations) {

			SaleItem saleItem = new SaleItem();

			saleItem.setSale(sale);
			saleItem.setMedicine(allocation.medicine);
			saleItem.setBatch(allocation.batch);
			saleItem.setQuantity(allocation.quantity);
			saleItem.setUnitPrice(allocation.unitPrice);
			saleItem.setDiscountAmount(allocation.discountAmount);
			saleItem.setGstRate(allocation.gstRate);
			saleItem.setHsnCode(allocation.hsnCode);
			saleItem.setTaxAmount(allocation.taxAmount);
			saleItem.setLineTotal(allocation.lineTotal);

			saleItemRepository.save(saleItem);

			int remainingQuantity = allocation.batch.getQuantity() - allocation.quantity;

			allocation.batch.setQuantity(remainingQuantity);
			medicineBatchRepository.save(allocation.batch);

			StockMovement movement = new StockMovement();
			movement.setBatch(allocation.batch);
			movement.setMovementType(StockMovementType.STOCK_OUT);
			movement.setQuantity(allocation.quantity);
			movement.setReason("Medicine sold");
			movement.setReferenceNumber(invoiceNumber);

			stockMovementRepository.save(movement);
		}

		// 10. Save split payments.
		for (var paymentRequest : request.getPayments()) {

			SalePayment payment = new SalePayment();
			payment.setSale(sale);
			payment.setPaymentMethod(paymentRequest.getPaymentMethod());
			payment.setAmount(paymentRequest.getAmount().setScale(2, RoundingMode.HALF_UP));
			payment.setReferenceNumber(paymentRequest.getReferenceNumber());

			salePaymentRepository.save(payment);
		}

		return buildSaleResponse(sale, pharmacyId);
	}

	/*
	 * Automatic FEFO: Earliest expiry date is selected first.
	 */
	private List<BatchAllocation> allocateAutomatically(Medicine medicine, Integer requestedQuantity, Long pharmacyId) {

		if (requestedQuantity == null || requestedQuantity <= 0) {
			throw new IllegalArgumentException("Requested quantity must be greater than zero.");
		}

		List<MedicineBatch> batches = medicineBatchRepository
				.findByMedicine_IdAndPharmacy_IdAndIsDeletedFalse(medicine.getId(), pharmacyId);

		batches.sort(Comparator.comparing(MedicineBatch::getExpiryDate).thenComparing(MedicineBatch::getId));

		int remaining = requestedQuantity;

		List<BatchAllocation> result = new ArrayList<>();

		LocalDate today = LocalDate.now();

		for (MedicineBatch candidate : batches) {

			if (remaining == 0) {
				break;
			}

			// Lock the batch before changing stock.
			MedicineBatch batch = medicineBatchRepository
					.findByIdAndPharmacyIdAndIsDeletedFalseForUpdate(candidate.getId(), pharmacyId)
					.orElseThrow(() -> new EntityNotFoundException("Medicine batch is no longer available."));

			if (!batch.getMedicine().getId().equals(medicine.getId())) {
				continue;
			}

			if (batch.getExpiryDate() == null || batch.getExpiryDate().isBefore(today) || batch.getQuantity() == null
					|| batch.getQuantity() <= 0) {
				continue;
			}

			int allocatedQuantity = Math.min(remaining, batch.getQuantity());

			result.add(new BatchAllocation(medicine, batch, allocatedQuantity));

			remaining -= allocatedQuantity;
		}

		if (remaining > 0) {
			throw new IllegalArgumentException(
					"Insufficient usable stock for " + medicine.getName() + ". Shortage: " + remaining);
		}

		return result;
	}

	/*
	 * Manual batch allocation: Every batch must belong to the current pharmacy and
	 * medicine.
	 */

	private List<BatchAllocation> allocateManually(Medicine medicine, Integer requestedQuantity,
			List<com.becoder.dto.ManualBatchAllocationRequest> manualAllocations, Long pharmacyId) {

		if (requestedQuantity == null || requestedQuantity <= 0) {
			throw new IllegalArgumentException("Requested quantity must be greater than zero.");
		}

		if (manualAllocations == null || manualAllocations.isEmpty()) {
			throw new IllegalArgumentException("Manual batch allocations are required.");
		}

		Set<Long> batchIds = new HashSet<>();

		int allocatedTotal = 0;

		List<BatchAllocation> result = new ArrayList<>();

		LocalDate today = LocalDate.now();

		// Validate duplicate IDs and allocation quantities first.
		for (var manual : manualAllocations) {

			if (manual == null || manual.getBatchId() == null) {
				throw new IllegalArgumentException("Batch ID is required.");
			}

			if (!batchIds.add(manual.getBatchId())) {
				throw new IllegalArgumentException("Duplicate batch selected: " + manual.getBatchId());
			}

			if (manual.getQuantity() == null || manual.getQuantity() <= 0) {
				throw new IllegalArgumentException("Manual allocation quantity must be greater than zero.");
			}

			allocatedTotal = Math.addExact(allocatedTotal, manual.getQuantity());
		}

		if (allocatedTotal != requestedQuantity) {
			throw new IllegalArgumentException(
					"Manual batch allocation total must equal the requested medicine quantity.");
		}

		// Sort batch selections to ensure consistent database lock ordering.
		List<com.becoder.dto.ManualBatchAllocationRequest> sortedAllocations = new ArrayList<>(manualAllocations);

		sortedAllocations.sort(Comparator.comparing(com.becoder.dto.ManualBatchAllocationRequest::getBatchId));

		// Lock and validate batches in ascending ID order.
		for (var manual : sortedAllocations) {

			MedicineBatch batch = medicineBatchRepository
					.findByIdAndPharmacyIdAndIsDeletedFalseForUpdate(manual.getBatchId(), pharmacyId)
					.orElseThrow(() -> new EntityNotFoundException("Selected batch not found in your pharmacy."));

			if (!batch.getMedicine().getId().equals(medicine.getId())) {
				throw new IllegalArgumentException("Selected batch does not belong to the requested medicine.");
			}

			if (batch.getExpiryDate() == null || batch.getExpiryDate().isBefore(today)) {
				throw new IllegalArgumentException("Expired batches cannot be sold.");
			}

			if (batch.getQuantity() == null || batch.getQuantity() < manual.getQuantity()) {
				throw new IllegalArgumentException("Insufficient stock in batch: " + batch.getBatchNumber());
			}

			result.add(new BatchAllocation(medicine, batch, manual.getQuantity()));
		}

		return result;
	}

	/*
	 * GST-inclusive calculation:
	 *
	 * Tax = discounted gross amount * GST rate / (100 + GST rate)
	 *
	 * Discount is distributed proportionally. The final allocation receives any
	 * rounding remainder so the discount reconciles exactly.
	 */
	private void calculateDiscountAndTax(List<BatchAllocation> allocations, BigDecimal totalDiscount) {

		if (allocations == null || allocations.isEmpty()) {
			throw new IllegalArgumentException("At least one batch allocation is required.");
		}

		if (totalDiscount == null || totalDiscount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Discount cannot be negative.");
		}

		// Convert all gross amounts into exact paise.
		List<BigInteger> grossAmounts = new ArrayList<>();

		BigInteger subtotalPaise = BigInteger.ZERO;

		for (BatchAllocation allocation : allocations) {

			if (allocation.grossAmount == null || allocation.grossAmount.compareTo(BigDecimal.ZERO) < 0) {
				throw new IllegalArgumentException("Invalid batch allocation amount.");
			}

			BigInteger grossPaise = allocation.grossAmount.setScale(2, RoundingMode.HALF_UP).movePointRight(2)
					.toBigIntegerExact();

			grossAmounts.add(grossPaise);
			subtotalPaise = subtotalPaise.add(grossPaise);
		}

		BigInteger discountPaise = totalDiscount.setScale(2, RoundingMode.HALF_UP).movePointRight(2)
				.toBigIntegerExact();

		if (discountPaise.compareTo(subtotalPaise) > 0) {
			throw new IllegalArgumentException("Discount cannot exceed subtotal.");
		}

		if (subtotalPaise.compareTo(BigInteger.ZERO) == 0 && discountPaise.compareTo(BigInteger.ZERO) > 0) {
			throw new IllegalArgumentException("Discount cannot be applied to a zero subtotal.");
		}

		// Calculate each allocation's proportional discount.
		List<BigInteger> discounts = new ArrayList<>();
		List<BigInteger> remainders = new ArrayList<>();

		BigInteger allocatedPaise = BigInteger.ZERO;

		for (BigInteger grossPaise : grossAmounts) {

			if (subtotalPaise.equals(BigInteger.ZERO)) {
				discounts.add(BigInteger.ZERO);
				remainders.add(BigInteger.ZERO);
				continue;
			}

			BigInteger numerator = discountPaise.multiply(grossPaise);

			BigInteger[] division = numerator.divideAndRemainder(subtotalPaise);

			BigInteger allocatedDiscount = division[0];

			discounts.add(allocatedDiscount);
			remainders.add(division[1]);

			allocatedPaise = allocatedPaise.add(allocatedDiscount);
		}

		// Distribute remaining paise using largest remainders.
		BigInteger remainingPaise = discountPaise.subtract(allocatedPaise);

		List<Integer> indexes = new ArrayList<>();

		for (int i = 0; i < allocations.size(); i++) {
			indexes.add(i);
		}

		indexes.sort(Comparator.comparing((Integer i) -> remainders.get(i)).reversed());

		for (Integer index : indexes) {

			if (remainingPaise.equals(BigInteger.ZERO)) {
				break;
			}

			BigInteger currentDiscount = discounts.get(index);
			BigInteger grossPaise = grossAmounts.get(index);

			if (currentDiscount.compareTo(grossPaise) < 0) {

				discounts.set(index, currentDiscount.add(BigInteger.ONE));

				remainingPaise = remainingPaise.subtract(BigInteger.ONE);
			}
		}

		if (!remainingPaise.equals(BigInteger.ZERO)) {
			throw new IllegalStateException("Unable to distribute the complete discount.");
		}

		// Calculate final discount, GST and line total.
		for (int i = 0; i < allocations.size(); i++) {

			BatchAllocation allocation = allocations.get(i);

			BigDecimal allocatedDiscount = new BigDecimal(discounts.get(i), 2);

			BigDecimal grossAmount = new BigDecimal(grossAmounts.get(i), 2);

			BigDecimal discountedGross = grossAmount.subtract(allocatedDiscount);

			if (allocation.gstRate == null || allocation.gstRate.compareTo(BigDecimal.ZERO) < 0
					|| allocation.gstRate.compareTo(new BigDecimal("100")) > 0) {
				throw new IllegalArgumentException("Invalid GST rate for medicine.");
			}

			BigDecimal denominator = new BigDecimal("100").add(allocation.gstRate);

			allocation.discountAmount = allocatedDiscount;
			allocation.taxAmount = discountedGross.multiply(allocation.gstRate).divide(denominator, 2,
					RoundingMode.HALF_UP);
			allocation.lineTotal = discountedGross.setScale(2, RoundingMode.HALF_UP);
		}
	}

	private void validatePayments(SaleRequest request, BigDecimal totalAmount) {

		if (request.getPayments() == null || request.getPayments().isEmpty()) {
			throw new IllegalArgumentException("At least one payment is required.");
		}

		BigDecimal paymentTotal = ZERO;

		for (var payment : request.getPayments()) {

			if (payment.getPaymentMethod() == null) {
				throw new IllegalArgumentException("Payment method is required.");
			}

			if (payment.getAmount() == null || payment.getAmount().compareTo(ZERO) <= 0) {
				throw new IllegalArgumentException("Payment amount must be greater than zero.");
			}

			paymentTotal = paymentTotal.add(payment.getAmount().setScale(2, RoundingMode.HALF_UP));
		}

		paymentTotal = paymentTotal.setScale(2, RoundingMode.HALF_UP);

		if (paymentTotal.compareTo(totalAmount) != 0) {
			throw new IllegalArgumentException("Total payment amount (" + paymentTotal
					+ ") must exactly match bill amount (" + totalAmount + ").");
		}
	}

	private String generateInvoiceNumber(Long pharmacyId) {

		String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

		String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

		String invoiceNumber = "INV-" + pharmacyId + "-" + datePart + "-" + randomPart;

		if (saleRepository.existsByPharmacy_IdAndInvoiceNumber(pharmacyId, invoiceNumber)) {
			return generateInvoiceNumber(pharmacyId);
		}

		return invoiceNumber;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SaleResponse> getAllSales() {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		return saleRepository.findByPharmacy_IdOrderByCreatedOnDesc(pharmacyId).stream()
				.map(sale -> buildSaleResponse(sale, pharmacyId)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public SaleResponse getSaleById(Long saleId) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		Sale sale = saleRepository.findByIdAndPharmacy_Id(saleId, pharmacyId)
				.orElseThrow(() -> new EntityNotFoundException("Sale not found."));

		return buildSaleResponse(sale, pharmacyId);
	}

	@Override
	public SaleResponse cancelSale(Long saleId) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		Sale sale = saleRepository.findByIdAndPharmacyIdForUpdate(saleId, pharmacyId)
				.orElseThrow(() -> new IllegalArgumentException("Sale not found with id: " + saleId));

		if (sale.getSaleStatus() == SaleStatus.CANCELLED) {
			throw new IllegalArgumentException("This sale has already been cancelled.");
		}

		List<SaleItem> saleItems = saleItemRepository.findBySale_IdAndSale_Pharmacy_Id(saleId, pharmacyId);

		for (SaleItem item : saleItems) {

			MedicineBatch batch = medicineBatchRepository
					.findByIdAndPharmacyIdAndIsDeletedFalseForUpdate(item.getBatch().getId(), pharmacyId)
					.orElseThrow(() -> new EntityNotFoundException("Batch not found while cancelling sale."));

			int restoredQuantity = Math.addExact(batch.getQuantity(), item.getQuantity());

			batch.setQuantity(restoredQuantity);
			medicineBatchRepository.save(batch);

			StockMovement movement = new StockMovement();
			movement.setBatch(batch);
			movement.setMovementType(StockMovementType.STOCK_IN);
			movement.setQuantity(item.getQuantity());
			movement.setReason("Sale cancelled - stock restored");
			movement.setReferenceNumber(sale.getInvoiceNumber());

			stockMovementRepository.save(movement);
		}

		sale.setSaleStatus(SaleStatus.CANCELLED);
		saleRepository.save(sale);

		return buildSaleResponse(sale, pharmacyId);
	}

	/*
	 * Convert the saved sale and its related records into the API response. Each
	 * SaleItem row represents one batch allocation, so its actual batch-specific
	 * selling price is preserved.
	 */
	
	private SaleResponse buildSaleResponse(Sale sale, Long pharmacyId) {

	    List<SaleItemResponse> itemResponses = saleItemRepository
	            .findBySale_IdAndSale_Pharmacy_Id(sale.getId(), pharmacyId)
	            .stream()
	            .map(item -> {

	                MedicineBatch batch = item.getBatch();

	                BatchAllocationResponse batchResponse =
	                        new BatchAllocationResponse(
	                                batch.getId(),
	                                batch.getBatchNumber(),
	                                item.getQuantity(),
	                                batch.getExpiryDate());

	                SaleItemResponse response = new SaleItemResponse(
	                        item.getMedicine().getId(),
	                        item.getMedicine().getName(),
	                        item.getQuantity(),
	                        item.getUnitPrice(),
	                        item.getDiscountAmount(),
	                        item.getGstRate(),
	                        item.getTaxAmount(),
	                        item.getLineTotal(),
	                        List.of(batchResponse));

	                response.setHsnCode(item.getHsnCode());

	                return response;
	            })
	            .toList();

	    List<SalePaymentResponse> paymentResponses = salePaymentRepository
	            .findBySale_IdAndSale_Pharmacy_Id(sale.getId(), pharmacyId)
	            .stream()
	            .map(payment -> new SalePaymentResponse(
	                    payment.getId(),
	                    payment.getPaymentMethod(),
	                    payment.getAmount(),
	                    payment.getReferenceNumber(),
	                    payment.getPaymentDate()))
	            .toList();

	    Customer customer = sale.getCustomer();

	    // Get pharmacy details.
	    Pharmacy pharmacy = sale.getPharmacy();

	    SaleResponse response = new SaleResponse(
	            sale.getId(),
	            sale.getInvoiceNumber(),
	            customer == null ? null : customer.getId(),
	            customer == null ? null : customer.getFullName(),
	            customer == null ? null : customer.getPhone(),
	            sale.getSubtotal(),
	            sale.getDiscountAmount(),
	            sale.getTaxAmount(),
	            sale.getTotalAmount(),
	            sale.getSaleStatus(),
	            itemResponses,
	            paymentResponses,
	            sale.getCreatedOn());

	    // Populate pharmacy details.
	    response.setPharmacyName(pharmacy.getPharmacyName());
	    response.setPharmacyEmail(pharmacy.getEmail());
	    response.setPharmacyPhone(pharmacy.getPhone());
	    response.setPharmacyAddress(pharmacy.getAddress());

	    return response;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SaleMedicineSearchResponse> searchMedicines(String keyword) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		if (keyword == null || keyword.trim().isEmpty()) {
			return List.of();
		}

		String searchKeyword = keyword.trim();

		return medicineRepository.searchForBilling(pharmacyId, searchKeyword).stream()
				.map(medicine -> new SaleMedicineSearchResponse(medicine.getId(), medicine.getName(),
						medicine.getGenericName(), medicine.getBrand(), medicine.getDosageForm(),
						medicine.getStrength(), medicine.getBarcode(), medicine.getHsnCode(), medicine.getGstRate()))
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<SaleBatchResponse> getAvailableBatches(Long medicineId) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		// Verify that the medicine belongs to the current pharmacy
		Medicine medicine = medicineRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(medicineId, pharmacyId)
				.filter(Medicine::isActive).orElseThrow(() -> new RuntimeException("Medicine not found or inactive"));

		// Fetch eligible batches
		return medicineBatchRepository.findByMedicine_IdAndPharmacy_IdAndIsDeletedFalse(medicine.getId(), pharmacyId)
				.stream().filter(batch -> batch.getExpiryDate() != null)
				.filter(batch -> !batch.getExpiryDate().isBefore(LocalDate.now()))
				.filter(batch -> batch.getQuantity() != null && batch.getQuantity() > 0)
				.sorted(Comparator.comparing(MedicineBatch::getExpiryDate).thenComparing(MedicineBatch::getId))
				.map(batch -> new SaleBatchResponse(batch.getId(), batch.getBatchNumber(), batch.getExpiryDate(),
						batch.getQuantity(), batch.getSellingPrice()))
				.toList();
	}

	@Override
	@Transactional
	public SalePreviewResponse previewSale(SalePreviewRequest request) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		// 1. Validate optional customer.
		if (request.getCustomerId() != null) {

			customerRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(request.getCustomerId(), pharmacyId)
					.orElseThrow(() -> new EntityNotFoundException("Customer not found in your pharmacy."));
		}

		// 2. Validate discount.
		BigDecimal discount = request.getDiscountAmount() == null ? ZERO
				: request.getDiscountAmount().setScale(2, RoundingMode.HALF_UP);

		if (discount.compareTo(ZERO) < 0) {
			throw new IllegalArgumentException("Discount amount cannot be negative.");
		}

		// 3. Prepare batch allocations.
		List<BatchAllocation> allocations = new ArrayList<>();

		for (var itemRequest : request.getItems()) {

			Medicine medicine = medicineRepository
					.findByIdAndPharmacy_IdAndIsDeletedFalse(itemRequest.getMedicineId(), pharmacyId)
					.orElseThrow(() -> new EntityNotFoundException(
							"Medicine not found in your pharmacy: " + itemRequest.getMedicineId()));

			if (!medicine.isActive()) {
				throw new IllegalArgumentException("Medicine is inactive: " + medicine.getName());
			}

			if (medicine.getGstRate() == null || medicine.getGstRate().compareTo(ZERO) < 0) {
				throw new IllegalArgumentException("Invalid GST rate configured for " + medicine.getName());
			}

			if (itemRequest.getBatchSelectionMode() == null) {
				throw new IllegalArgumentException("Batch selection mode is required.");
			}

			switch (itemRequest.getBatchSelectionMode()) {

			case AUTOMATIC_FEFO:
				allocations.addAll(allocateAutomatically(medicine, itemRequest.getQuantity(), pharmacyId));
				break;

			case MANUAL:
				allocations.addAll(allocateManually(medicine, itemRequest.getQuantity(),itemRequest.getManualAllocations(), pharmacyId));
				break;

			default:
				throw new IllegalArgumentException("Unsupported batch selection mode.");
			}
		}

		// 4. Calculate subtotal.
		BigDecimal subtotal = allocations.stream().map(a -> a.grossAmount).reduce(ZERO, BigDecimal::add).setScale(2,
				RoundingMode.HALF_UP);

		if (discount.compareTo(subtotal) > 0) {
			throw new IllegalArgumentException("Discount cannot be greater than the subtotal.");
		}

		// 5. Reuse the existing discount and GST calculation.
		calculateDiscountAndTax(allocations, discount);

		BigDecimal totalTax = allocations.stream().map(a -> a.taxAmount).reduce(ZERO, BigDecimal::add).setScale(2,
				RoundingMode.HALF_UP);

		BigDecimal totalAmount = subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);

		// 6. Build preview item responses.
		List<SaleItemResponse> itemResponses = allocations.stream().map(allocation -> {

			MedicineBatch batch = allocation.batch;

			BatchAllocationResponse batchResponse = new BatchAllocationResponse(batch.getId(), batch.getBatchNumber(),
					allocation.quantity, batch.getExpiryDate());

			SaleItemResponse response = new SaleItemResponse(allocation.medicine.getId(), allocation.medicine.getName(),
					allocation.quantity, allocation.unitPrice, allocation.discountAmount, allocation.gstRate,
					allocation.taxAmount, allocation.lineTotal, List.of(batchResponse));

			response.setHsnCode(allocation.hsnCode);

			return response;

		}).toList();

		// 7. Return preview without creating a sale.
		return new SalePreviewResponse(subtotal, discount, totalTax, totalAmount, itemResponses);
	}

}