package ru.JB.develop.purchaselist.Model;

public class PurchaseItem {

    private int id;
    private int productId;
   private String purchaseName;
    private boolean purchaseIsBought;

    public PurchaseItem(int newId, String newName,  Boolean isBought, int newProductId) {
        id = newId;
        purchaseName = newName;
        purchaseIsBought = isBought;
        productId = newProductId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int newId) {
        productId = newId;
    }

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setPurchaseName(String newName) {
        purchaseName= newName;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public boolean getPurchaseIsBought() {
        return purchaseIsBought;
    }

    public void setPurchaseIsBought(boolean isBought) {
        purchaseIsBought = isBought;
    }
}