package ru.JB.develop.purchaselist.Model;

public class PurchaseItem {
 
	private int id;
	private int numberOfPurchases;
	private String purchaseName;
	private boolean purchaseIsBought;
	
	
	public PurchaseItem(int newId, String newName, Integer newNumber, Boolean isBought, int newProductId){
		id = newId;
		purchaseName = newName;
		numberOfPurchases = newNumber;
		purchaseIsBought = isBought;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int newId){
		id = newId;
	}
		
	public void setPurchaseName(String newName){
		purchaseName= newName;
	}
	
	public String getPurchaseName(){
		return purchaseName;
	}
	
	public void setNumberOfPurchases(int newNumber){
		numberOfPurchases = newNumber;
	}
	
	public int getNumberOfPurchases(){
		return numberOfPurchases;
	}
	
	public boolean getPurchaseIsBought(){
		return purchaseIsBought;
	}
	
	public void setPurchaseIsBought(boolean isBought){
		purchaseIsBought = isBought;
	}
	

}
