package com.midokter.app.repositary.model;

import com.google.gson.annotations.SerializedName;

public class ChatPromoResponse{

	@SerializedName("message")
	private String message;

	@SerializedName("promo_discount")
	private int promoDiscount;

	@SerializedName("final_fees")
	private int finalFees;

	@SerializedName("status")
	private int status;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setPromoDiscount(int promoDiscount){
		this.promoDiscount = promoDiscount;
	}

	public int getPromoDiscount(){
		return promoDiscount;
	}

	public void setFinalFees(int finalFees){
		this.finalFees = finalFees;
	}

	public int getFinalFees(){
		return finalFees;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}