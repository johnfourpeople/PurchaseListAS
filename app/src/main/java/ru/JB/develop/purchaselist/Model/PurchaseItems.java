package ru.JB.develop.purchaselist.Model;

import java.util.ArrayList;
import android.content.Context;
import ru.JB.develop.purchaselist.Database.DBWorker;

public class PurchaseItems {

	private ArrayList<PurchaseItem> purchases;
	private DBWorker database;
	
	public PurchaseItems(Context context){
		database = new DBWorker(context);
		purchases = database.readFromPurchases();
	}
	
	// Must staing before adapter initialization. Cause - notifing that data set changing
	public void add(ArrayList<Integer> productsIds){
		database.writeToPurchases(productsIds);
		purchases = database.readFromPurchases();
	}

	
	public int size(){
		return purchases.size();
	}
	public PurchaseItem get(int index){
		return purchases.get(index);
	}
	
	public void delete(ArrayList<Integer> indexesForDelete){
		
		Integer[] sortedIndexes = sort(indexesForDelete);
		for(int i=0;i<sortedIndexes.length;i++){
			database.deleteFormPurchases(purchases.get((int)sortedIndexes[i]).getPurchaseName());
			purchases.remove((int)sortedIndexes[i]);
		}
		
	}
	//refactor int[]
	public Integer[] sort(ArrayList<Integer> list){
		Integer max = 0;
		int ind = 0;
		Integer[] newList = (Integer[]) list.toArray(new Integer[list.size()]);                     
		for(Integer i = 0; i<newList.length;i++){
			for(Integer j = i; j < newList.length; j++){
				if (newList[j] > max){
					max = newList[j];
					ind = j;
				}
			}
		
			newList[ind] = newList[i];
			newList[i] = max;
			max = 0;
		}
		return newList;
	}
	
	
}
