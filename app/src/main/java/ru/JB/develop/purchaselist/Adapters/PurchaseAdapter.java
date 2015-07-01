package ru.JB.develop.purchaselist.Adapters;

import java.util.ArrayList;

import ru.JB.develop.purchaselist.R;
import ru.JB.develop.purchaselist.Model.PurchaseItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class PurchaseAdapter extends BaseAdapter implements OnClickListener {
	
	PurchaseItems purchases;
	Context context;
	Boolean deleting = false;
	ArrayList<Integer> indexesForDelete = new ArrayList<Integer>();
	
	public PurchaseAdapter(PurchaseItems items, Context cntxt){
		purchases = items;
		context = cntxt;
	}


		 static class ViewHolder{
				TextView purchaseName;
				TextView numberOfPurchase;
				CheckBox purchaseIsBought;
				
			}	
		 
	public void getDeleteCheckBox(){
		deleting = true;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return purchases.size();
	}

	@Override
	public Object getItem(int arg0) {
		return purchases.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void deleteCheckedItems(){
		if(indexesForDelete != null){
			purchases.delete(indexesForDelete);
			indexesForDelete.clear();
			deleting = false;
			notifyDataSetChanged();
		}
	}
	


	@Override
	public View getView(int index, View rowView, ViewGroup arg2) {
		ViewHolder holder;
		
		if(rowView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.purchase_item, arg2, false);
				holder = new ViewHolder();
				holder.purchaseName = (TextView)rowView.findViewById(R.id.purchaseName);
				holder.numberOfPurchase = (TextView)rowView.findViewById(R.id.numberOfPurchase);
				holder.purchaseIsBought = (CheckBox)rowView.findViewById(R.id.purchaseIsBought);
				rowView.setTag(holder);
		} else {
		holder = (ViewHolder) rowView.getTag();
		}
		
		holder.purchaseName.setText(purchases.get(index).getPurchaseName());
		holder.numberOfPurchase.setText(((Integer) purchases.get(index).getNumberOfPurchases()).toString());
		holder.purchaseIsBought.setTag(index);
		holder.purchaseIsBought.setOnClickListener(this);
		
		
		if (deleting){
			holder.purchaseIsBought.setBackgroundResource(R.color.Red);
			if (!indexesForDelete.contains(index))
				holder.purchaseIsBought.setChecked(false);
		} else {
			holder.purchaseIsBought.setChecked(purchases.get(index).getPurchaseIsBought());
			holder.purchaseIsBought.setBackgroundResource(R.color.Transparent);
		}
		
		return rowView;
	}
	

	@Override
	public void onClick(View arg0) {
		Boolean isChecked = ((CheckBox) arg0).isChecked();
		
		if(deleting){
			if(isChecked)
				indexesForDelete.add((Integer) arg0.getTag());
			else{
				indexesForDelete.remove((Integer) arg0.getTag());
				indexesForDelete.trimToSize();
			}
		} else {
			purchases.get((Integer) arg0.getTag()).setPurchaseIsBought(isChecked);
		}	
		notifyDataSetChanged();
	}
}
