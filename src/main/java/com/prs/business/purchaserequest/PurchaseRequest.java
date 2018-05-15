package com.prs.business.purchaserequest;


import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
//import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.prs.business.user.User;

@Entity
public class PurchaseRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name="userID")
	private User user;
	private String description;
	private String justification;
	private Date dateNeeded;
	private String deliveryMode;
	private String status;
	private double total;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd")
	private Timestamp submittedDate;
	private String reasonForRejection;
	
	public static final String STATUS_NEW = "New";
	public static final String STATUS_REVIEW = "Review";
	public static final String STATUS_EDIT = "Edit";
	public static final String STATUS_APPROVED = "Approved";
	public static final String STATUS_REJECTED = "Rejected";
	//@OneToMany
	//private ArrayList<PurchaseRequestLineItem> prLineItems;
	
	public PurchaseRequest() {
		
	}
	
	public PurchaseRequest(int id, User user, String description, String justification, 
							Date dateNeeded, String deliveryMode, String status, double total,
							Timestamp submittedDate, String reasonForRejection) {
		this.id = id;
		this.user = user;
		this.description = description;
		this.justification = justification;
		this.dateNeeded = dateNeeded;
		this.deliveryMode = deliveryMode;
		this.status = status;
		this.total = total;
		this.submittedDate = submittedDate;
		this.reasonForRejection = reasonForRejection;
		//prLineItems = new ArrayList<>();	
		
	}
	
	
	public PurchaseRequest(User user, String description, String justification, Date dateNeeded,
			String deliveryMode, String status, double total, Timestamp submittedDate, String reasonForRejection) {
		this.user = user;
		this.description = description;
		this.justification = justification;
		this.dateNeeded = dateNeeded;
		this.deliveryMode = deliveryMode;
		this.status = status;
		this.total = total;
		this.submittedDate = submittedDate;
		this.reasonForRejection = reasonForRejection;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public Date getDateNeeded() {
		return dateNeeded;
	}
	public void setDateNeeded(Date dateNeeded) {
		this.dateNeeded = dateNeeded;
	}
	public String getDeliveryMode() {
		return deliveryMode;
	}
	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public Timestamp getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(Timestamp submittedDate) {
		this.submittedDate = submittedDate;
	}
	
	
	/*public ArrayList<PurchaseRequestLineItem> getPrLineItems() {
		return prLineItems;
	}
	public void setPrLineItems(ArrayList<PurchaseRequestLineItem> prLineItems) {
		this.prLineItems = prLineItems;
	}*/
	
	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

	@Override
	public String toString() {
		return "PurchaseRequest [id=" + id + ", " + "user=" + user + ", description=" + description + ", justification="
				+ justification + ", dateNeeded=" + dateNeeded + ", deliveryMode=" + deliveryMode + ", status="
				+ status + ", total=" + total + ", submittedDate=" + submittedDate + ", prLineItems=" //+ prLineItems
				+ "]";
	}
	

}

