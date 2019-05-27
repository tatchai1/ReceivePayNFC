package com.paynfc;

public class History {
	@com.google.gson.annotations.SerializedName("ID")
	private String mID;
	
	@com.google.gson.annotations.SerializedName("From")
	private String mFrom;
	
	@com.google.gson.annotations.SerializedName("To")
	private String mTo;	
	
	@com.google.gson.annotations.SerializedName("UserID")
	private String mUserID;		
	
	@com.google.gson.annotations.SerializedName("Status")
	private String mStatus;
	
	@com.google.gson.annotations.SerializedName("Balamce")
	private int mBalance;

	public String getmID() {
		return mID;
	}

	public void setmID(String mID) {
		this.mID = mID;
	}

	public String getmFrom() {
		return mFrom;
	}

	public void setmFrom(String mFrom) {
		this.mFrom = mFrom;
	}

	public String getmTo() {
		return mTo;
	}

	public void setmTo(String mTo) {
		this.mTo = mTo;
	}

	public String getmUserID() {
		return mUserID;
	}

	public void setmUserID(String mUserID) {
		this.mUserID = mUserID;
	}

	public String getmStatus() {
		return mStatus;
	}

	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public int getmBalance() {
		return mBalance;
	}

	public void setmBalance(int mBalance) {
		this.mBalance = mBalance;
	}

	
	
	
	

}
