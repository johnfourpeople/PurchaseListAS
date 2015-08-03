package ru.JB.develop.purchaselist.Adapters;

import java.util.ArrayList;
import java.util.List;

import ru.JB.develop.purchaselist.Model.PurchaseItem;
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

    static final String TAG = "PurchaseAdapter";

	PurchaseItems purchases;
    List<PurchaseItem> viewItems;
	Context context;
	Boolean deleting = false;
	ArrayList<Integer> idsForDelete = new ArrayList<Integer>();
    ArrayList<Integer> checkedIds = new ArrayList<Integer>();

	public PurchaseAdapter(PurchaseItems items, Context cntxt){
		purchases = items;
		context = cntxt;
        viewItems = purchases.getAll();
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
		return viewItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return viewItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void deleteCheckedItems(){
		if(idsForDelete != null){
			purchases.delete(idsForDelete);
            viewItems.clear();
            viewItems.addAll(purchases.getAll());
			idsForDelete.clear();
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
		
		holder.purchaseName.setText(viewItems.get(index).getPurchaseName());
		holder.numberOfPurchase.setText(String.valueOf(viewItems.get(index).getNumberOfPurchases()));
		holder.purchaseIsBought.setTag(viewItems.get(index).getProductId());
		holder.purchaseIsBought.setOnClickListener(this);
		
		
		if (deleting){
			holder.purchaseIsBought.setBackgroundResource(R.color.Red);
			holder.purchaseIsBought.setChecked(idsForDelete.contains(viewItems.get(index).getProductId()));
		} else {
            holder.purchaseIsBought.setChecked(viewItems.get(index).getPurchaseIsBought());
			holder.purchaseIsBought.setBackgroundResource(R.color.Transparent);
		}
		
		return rowView;
	}

	@Override
	public void onClick(View arg0) {
		Boolean isChecked = ((CheckBox) arg0).isChecked();
		
		if(deleting){
			if(isChecked) {
                idsForDelete.add((Integer) arg0.getTag());
            } else {
				idsForDelete.remove(arg0.getTag());
			}
		} else {
			purchases.getById((Integer) arg0.getTag()).setPurchaseIsBought(isChecked);
		}
		notifyDataSetChanged();
	}
}
