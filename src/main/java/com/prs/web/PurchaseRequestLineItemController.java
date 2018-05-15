package com.prs.web;

import java.util.ArrayList;
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
import com.prs.business.purchaserequest.PurchaseRequest;
import com.prs.business.purchaserequest.PurchaseRequestLineItem;
import com.prs.business.purchaserequest.PurchaseRequestLineItemRepository;
import com.prs.business.purchaserequest.PurchaseRequestRepository;
import com.prs.util.PRSMaintenanceReturn;

@CrossOrigin
@Controller    
@RequestMapping(path="/PurchaseRequestLineItem") 
public class PurchaseRequestLineItemController extends BaseController {
	@Autowired 
	private PurchaseRequestLineItemRepository prliRepository;
	@Autowired
	private PurchaseRequestRepository prRepository;


	@GetMapping(path="/List")
	public @ResponseBody Iterable<PurchaseRequestLineItem> getAllPurchaseRequestLineItems() {
		return prliRepository.findAll();
		
		
	}
	
	@GetMapping(path="/LinesForPR")
	public @ResponseBody Iterable<PurchaseRequestLineItem> getAllLineItemsForPR(@RequestParam int id) {
		// This returns a JSON or XML with the users
		return prliRepository.findAllByPurchaseRequestId(id);
	}
	
	@GetMapping(path="/Get")
	public @ResponseBody List<PurchaseRequestLineItem> getPurchaseRequestLineItem(@RequestParam int id) {
		Optional<PurchaseRequestLineItem> prli = prliRepository.findById(id);
		return getReturnArray(prli);
	}

	@PostMapping(path="/Add")
	public @ResponseBody PRSMaintenanceReturn addNewPurchaseRequestLineItem(@RequestBody PurchaseRequestLineItem purchaseRequestLineItem) {
		try {
			prliRepository.save(purchaseRequestLineItem);
			updatePRTotal(purchaseRequestLineItem);
			return PRSMaintenanceReturn.getMaintReturn(purchaseRequestLineItem);
		}
		catch (DataIntegrityViolationException dive) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequestLineItem, dive.getRootCause().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequestLineItem, e.getMessage());
		}
	}
	
	@GetMapping(path="/Remove")
	public @ResponseBody PRSMaintenanceReturn deletePurchaseRequestLineItem(@RequestParam int id) {
		Optional<PurchaseRequestLineItem> purchaseRequestLineItem = prliRepository.findById(id);
		try {
			prliRepository.delete(purchaseRequestLineItem.get());
			updatePRTotal(purchaseRequestLineItem.get());
			return PRSMaintenanceReturn.getMaintReturn(purchaseRequestLineItem.get());
		}
		catch (DataIntegrityViolationException dive) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequestLineItem, dive.getRootCause().toString());
		}
		catch (Exception e) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequestLineItem, e.toString());
		}
	}
	
	@PostMapping(path="/Change")
	public @ResponseBody PRSMaintenanceReturn updatePurchaseRequestLineItem(@RequestBody PurchaseRequestLineItem purchaseRequestLineItem) {
		try {
			prliRepository.save(purchaseRequestLineItem);
			updatePRTotal(purchaseRequestLineItem);
			return PRSMaintenanceReturn.getMaintReturn(purchaseRequestLineItem);
		}
		catch (DataIntegrityViolationException dive) {
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequestLineItem, dive.getRootCause().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			return PRSMaintenanceReturn.getMaintReturnError(purchaseRequestLineItem, e.getMessage());
		}
	}
	
	private void updatePRTotal(PurchaseRequestLineItem prli) throws Exception {
		Optional<PurchaseRequest> prOpt = prRepository.findById(prli.getPurchaseRequest().getId());
		PurchaseRequest pr = prOpt.get();
		List<PurchaseRequestLineItem> lines = new ArrayList<>();
		lines = prliRepository.findAllByPurchaseRequestId(pr.getId());
		double total = 0;
		for (PurchaseRequestLineItem line : lines) {
			Product p = line.getProduct();
			double lineTotal = line.getQuantity()*p.getPrice();
			total += lineTotal;
		}
		pr.setTotal(total);
		prRepository.save(pr);
		
	}
	
}