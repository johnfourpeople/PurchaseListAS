package ru.JB.develop.purchaselist.Adapters;

import java.util.ArrayList;

import ru.JB.develop.purchaselist.*;
import ru.JB.develop.purchaselist.Model.ProductItem;
import ru.JB.develop.purchaselist.Model.ProductItems;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ProductsAdapter extends BaseAdapter implements   OnClickListener, Filterable {
	
	private static final int VIEW_ITEM = 0;
	private static final int EDIT_ITEM = 1;
	private static final int TYPE_MAX = EDIT_ITEM + 1;
	
	
	ProductItems products;
	Context context;
	boolean checking = false;
	int editionPosition = -1;
	boolean editPriceFocused = false;
	
	ProductFilter filter = new ProductFilter();
	EditionSaver priceEditionSaver = new EditionSaver();
	EditionSaver nameEditionSaver = new EditionSaver();
	ArrayList<Integer> checkedProductIds = new ArrayList<Integer>();
	ArrayList<ProductItem> filteredProducts = new ArrayList<ProductItem>();
	
	
	public ProductsAdapter(ProductItems items,Context context){
		products = items;
		filteredProducts.addAll(products.getAll());
		this.context = context;
		
	}
	
	private class EditionSaver implements TextWatcher{

		private String currentText;
		@Override
		public void afterTextChanged(Editable s) {
			currentText = s.toString();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}
		void reset(){currentText = null;}
		String getCurrentText(){
			return currentText;
		}		
	}
	//refactor sort
	private class ProductFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			
			int count = products.size();
			final ArrayList<ProductItem> nlist = new ArrayList<ProductItem>(count);
			
			String filterableString ;
			
			for (int i = 0; i < count; i++) {
				filterableString = products.get(i).getName();
				if (filterableString.toLowerCase().contains(filterString)) {
					nlist.add(products.get(i));
				}
			}
			results.values = nlist;
			results.count = nlist.size();
			
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredProducts = (ArrayList<ProductItem>) results.values;
			notifyDataSetChanged();	
		}
	}
	
	private static class ViewHolder{
		TextView viewProductName;
		TextView viewProductUnit;
		TextView viewProductPrice;
		CheckBox checkProduct;
	}
	
	@Override
	public int getCount() {
		return filteredProducts.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredProducts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;	
	}
	
	@Override
	public int getItemViewType(int position){
		return position == editionPosition ? EDIT_ITEM : VIEW_ITEM;
	}
	
	@Override
	public int getViewTypeCount(){
		return TYPE_MAX;
		
	}
	
	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		switch(getItemViewType(position)){
		case VIEW_ITEM:
			ViewHolder holder;	
			if(rowView == null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.product_item, parent, false);
					holder = new ViewHolder();
					holder.viewProductName = (TextView) rowView.findViewById(R.id.viewProductName);
					holder.viewProductUnit = (TextView) rowView.findViewById(R.id.productUnit);
					holder.viewProductPrice = (TextView)rowView.findViewById(R.id.productPrice);
					holder.checkProduct = (CheckBox)rowView.findViewById(R.id.checkProduct);
					rowView.setTag(holder);
			} else {
				holder = (ViewHolder) rowView.getTag();
				}
			holder.viewProductName.setText(filteredProducts.get(position).getName());
			holder.viewProductUnit.setText(filteredProducts.get(position).getUnit());
			holder.viewProductPrice.setText(filteredProducts.get(position).getFormatedPrice());
			holder.checkProduct.setTag(filteredProducts.get(position).getID());
			holder.checkProduct.setOnClickListener(this);
			if(checking){
				holder.checkProduct.setVisibility(View.VISIBLE);
			} else {
				holder.checkProduct.setVisibility(View.GONE);
			}
			holder.checkProduct.setChecked(checkedProductIds.contains(position));
			break;
		case EDIT_ITEM:
			//TODO make save button on soft keyboard
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.edit_product_item, parent, false);
			EditText editName = (EditText)rowView.findViewById(R.id.editProductName);
			EditText editPrice = (EditText)rowView.findViewById(R.id.editProductPrice);

            Log.d("debug getView", filteredProducts.get(position).getName().toString());
            editName.setText(filteredProducts.get(position).getName());
            editName.addTextChangedListener(nameEditionSaver);

            if(nameEditionSaver.currentText == null) {
                nameEditionSaver.currentText = editName.getText().toString();
            } else {
                editName.setText(nameEditionSaver.getCurrentText());
            }

            editName.setSelection(editName.getText().length());
            editName.setOnFocusChangeListener(new OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    editPriceFocused = hasFocus;
                }

            });

            editPrice.setText(filteredProducts.get(position).getFormatedPrice());
			editPrice.addTextChangedListener(priceEditionSaver);

            if(priceEditionSaver.currentText == null){
                priceEditionSaver.currentText = editPrice.getText().toString();
            } else {
                editPrice.setText(priceEditionSaver.getCurrentText());
            }

            editPrice.setSelection(editPrice.getText().length());
			editPrice.setOnFocusChangeListener(new OnFocusChangeListener(){

				@Override
				public void onFocusChange(View arg0, boolean hasFocus) {
					editPriceFocused = !hasFocus;
				}
				
			});


            if(editPriceFocused){
				editPrice.requestFocus();
			} else {
				editName.requestFocus();
			}
			
			break;
		}
		return rowView;
	}		
	
	@Override
	public void onClick(View v) {
		Boolean isChecked = ((CheckBox) v).isChecked();
		if(isChecked){
			checkedProductIds.add((Integer) v.getTag());
		}
		else{
			checkedProductIds.remove((Integer) v.getTag());
			checkedProductIds.trimToSize();
		}	
	}
	

	public void onItemEdition(int position) {
        Log.d("debug ", String.valueOf(position));
		editionPosition = position;
		notifyDataSetChanged();
	}
		
	public void saveEdition(){
		filteredProducts.get(editionPosition).setName(nameEditionSaver.getCurrentText());
		products.edit(filteredProducts.get(editionPosition).getID(),nameEditionSaver.getCurrentText(),priceEditionSaver.getCurrentText());
		editionPosition = -1;
        nameEditionSaver.reset();
        priceEditionSaver.reset();
		notifyDataSetChanged();
	}
	
	public void cancelEdition(){
		editionPosition = -1;
        nameEditionSaver.reset();
        priceEditionSaver.reset();
		notifyDataSetChanged();
	}
	
	@Override
	public Filter getFilter() {
		return filter;
	}
	
	public void getOrHideCheckBox(){
		checking = !checking;
		notifyDataSetChanged();
	}
	
	public void getCheckBox(){
		checking = true;
		notifyDataSetChanged();
	}
	
	public void hideCheckBox(){
		checking = false;
		notifyDataSetChanged();
	}
	
	public void deleteChecked(){
		if(checkedProductIds != null){
			for(int id : checkedProductIds){
				products.deleteItemById(id);
			}
			filteredProducts.clear();
			filteredProducts.addAll(products.getAll());
			checkedProductIds.clear();
			notifyDataSetChanged();
		}
	}	
	
	public ArrayList<Integer> getCheckedProductsId(){
			return checkedProductIds;
	}
	
	public boolean addProduct(String newProductName, String unit , double newProductPrice){
		ProductItem newProduct = products.add(new ProductItem(newProductName, unit, newProductPrice));
		if(newProduct != null) {
            filteredProducts.clear();
            filteredProducts.addAll(products.getAll());
            notifyDataSetChanged();
            return true;
        }
        return false;
	}
}
