package ru.JB.develop.purchaselist.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.util.Log;

import ru.JB.develop.purchaselist.Database.DBWorker;

public class ProductItems  {

	private ArrayList<ProductItem> products;
	private DBWorker database;
	
	public ProductItems(Context context){
		database = new DBWorker(context);
		products = database.readFromProducts();
		for(ProductItem product : products){
			Log.d("debug id ProductItems constructor", String.valueOf(product.getID()));
		}
	}
	
	public int size(){
		return products.size();
	}
	
	// refactor delete purchases with no existing Id 
	public void deleteItems(ArrayList<Integer> indexesForDelete){	
		Integer[] sortedIndexes = sort(indexesForDelete);
		for(int i=0;i<sortedIndexes.length;i++){
			String productName = products.get((int)sortedIndexes[i]).getName();
			database.deleteFromProductsByName(productName);
			products.remove((int)sortedIndexes[i]);
			}
		}
		
	public int findIndexOfProductById(Integer id){
		Log.d("debug items id",id.toString());
		Comparator<ProductItem> comparator = new Comparator<ProductItem>(){

			@Override
			public int compare(ProductItem arg0, ProductItem arg1) {
				
				return arg0.getID()-arg1.getID();
			}
			
		};
		Collections.sort(products, comparator);
		int index = Collections.binarySearch(products, new ProductItem("",0,id), comparator);
		Log.d("debug items index", String.valueOf(index));
		return index;
	}
	
	public void deleteItemById(int idForDelete){
		int index = findIndexOfProductById(idForDelete);
		if (index >= 0){
			products.remove(index);
			database.deleteFromProductsById(idForDelete);
		}	
	}
	
	// refactor
	public ProductItem add(ProductItem newItem){
		long ID = database.writeToProducts(newItem);;
		newItem.setID(ID);
		Log.d("debug ProductItems item added with id= ",String.valueOf(ID));
		products.add(newItem);
		return newItem;
	}
	
	public boolean containsName(String name){
		return products.contains(new ProductItem(name,0));
	}
	
	public void edit(int id, String newName, String newPrice){
		int index = findIndexOfProductById(id);
		Log.d("debug ProductItem edit index = ", String.valueOf(index));
		if(index <0)
			return;
		products.get(index).setName(newName);
		products.get(index).setFormatedPrice(newPrice);
		database.editProduct(id, newName, String.valueOf(products.get(index).getPrice()));
	}
	
	public ArrayList<ProductItem> getAll(){
		ArrayList<ProductItem> copyProducts = new ArrayList<ProductItem>();
		Comparator<ProductItem> comparatorByName = new Comparator<ProductItem>(){
			@Override
			public int compare(ProductItem lhs, ProductItem rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}	
		};
		copyProducts.addAll(products);
		Collections.sort(copyProducts, comparatorByName);
		return copyProducts;
	}
	
	public ProductItem get(int index){
		return products.get(index);
	}

	public ArrayList<Integer> find(String name){
		ArrayList<Integer> findedProducts = new ArrayList<Integer>();
		for(ProductItem product : products){
			if(product.getName().equals(name)){
				findedProducts.add(product.getID());
			}
		}
		return findedProducts;
	}
	
	public void close(){
		database.close();
	}
	
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
