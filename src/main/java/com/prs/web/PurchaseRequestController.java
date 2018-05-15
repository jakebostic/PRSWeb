package com.prs.web;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.prs.business.product.Product;
import com.prs.business.product.ProductRepository;
import com.prs.business.purchaserequest.PurchaseRequest;
import com.prs.business.purchaserequest.PurchaseRequestRepository;
import com.prs.util.PRSMaintenanceReturn;

@CrossOrigin
@Controller
@RequestMapping(path = "/PurchaseRequests")
public class PurchaseRequestController extends BaseController {
	@Autowired
	private PurchaseRequestRepository purchaseRequestRepository;
	/*
	 * @Autowired private PurchaseRequestRepository prRepository;
	 */

	@GetMapping(path = "/List")
	public @ResponseBody Iterable<PurchaseRequest> getAllPurchaseRequests() {
		return purchaseRequestRepository.findAll();

	}

	@GetMapping(path = "/ListReview")
	public @ResponseBody Iterable<PurchaseRequest> getAllPurchaseRequestsForReview(@RequestParam int id) {
		Iterable<PurchaseRequest> reviewPRs = purchaseRequestRepository.findAllByUserIdNotAndStatus(id, "Review");
		return reviewPRs;
	}

	@PostMapping(path = "/ApprovePR")
	public @ResponseBody PRSMaintenanceReturn approvePR(@RequestBody PurchaseRequest purchaseRequest) {
		purchaseRequest.setStatus(PurchaseRequest.STATUS_APPROVED);
		return savePR(purchaseRequest);
	}

	@PostMapping(path = "/RejectPR")
	public @ResponseBody PRSMaintenanceReturn rejectPR(@RequestBody PurchaseRequest purchaseRequest) {
		purchaseRequest.setStatus(PurchaseRequest.STATUS_REJECTED);
		return savePR(purchaseRequest);
	}

	public @ResponseBody PRSMaintenanceReturn savePR(@RequestBody PurchaseRequest purchaseRequest) {
		try {
			purchaseRequestRepository.save(purchaseRequest);
			return PRSMaintenanceReturn.getMaintReturn(purchaseRequest);
		} catch (DataIntegrityViolationException dive) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, dive.getRootCause().toString());
		} catch (Exception e) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, e.toString());
		}
	}

	@GetMapping(path = "/Get")
	public @ResponseBody List<PurchaseRequest> getPurchaseRequest(@RequestParam int id) {
		Optional<PurchaseRequest> pr = purchaseRequestRepository.findById(id);
		return getReturnArray(pr.get());
	}

	@PostMapping(path = "/Add")
	public @ResponseBody PRSMaintenanceReturn addNewPurchaseRequest(@RequestBody PurchaseRequest purchaseRequest) {
		try {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			purchaseRequest.setSubmittedDate(ts);
			purchaseRequest.setStatus(PurchaseRequest.STATUS_NEW);
			purchaseRequestRepository.save(purchaseRequest);
			return PRSMaintenanceReturn.getMaintReturn(purchaseRequest);
		} catch (DataIntegrityViolationException dive) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, dive.getRootCause().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, e.getMessage());
		}
	}

	@GetMapping(path = "/Remove")
	public @ResponseBody PRSMaintenanceReturn deletePurchaseRequest(@RequestParam int id) {
		Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(id);
		try {
			purchaseRequestRepository.delete(purchaseRequest.get());
			return PRSMaintenanceReturn.getMaintReturn(purchaseRequest.get());
		}
		catch (DataIntegrityViolationException dive) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, dive.getRootCause().toString());
		}
		catch (Exception e) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, e.toString());
		}
	}

	@PostMapping(path = "/Change")
	public @ResponseBody PRSMaintenanceReturn updatePurchaseRequest(@RequestBody PurchaseRequest purchaseRequest) {
		try {
			purchaseRequestRepository.save(purchaseRequest);
			return PRSMaintenanceReturn.getMaintReturn(purchaseRequest);
		} catch (DataIntegrityViolationException dive) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, dive.getRootCause().toString());
		} catch (Exception e) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, e.toString());
		}
	}

	@PostMapping(path = "/Submit")
	public @ResponseBody PRSMaintenanceReturn submitForReview(@RequestBody PurchaseRequest purchaseRequest) {
		Optional<PurchaseRequest> prOpt = purchaseRequestRepository.findById(purchaseRequest.getId());
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		purchaseRequest = prOpt.get();
		if (purchaseRequest.getTotal() < 50.0) {
			purchaseRequest.setStatus(PurchaseRequest.STATUS_APPROVED);
		} else {
			purchaseRequest.setStatus(PurchaseRequest.STATUS_REVIEW);
		}
		purchaseRequest.setSubmittedDate(ts);
		try {
			purchaseRequestRepository.save(purchaseRequest);
			return PRSMaintenanceReturn.getMaintReturn(purchaseRequest);
		} catch (DataIntegrityViolationException dive) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, dive.getRootCause().toString());
		} catch (Exception e) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequest, e.toString());
		}
	} 
	
	
}
