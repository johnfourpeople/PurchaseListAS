package ru.JB.develop.purchaselist;


import java.util.ArrayList;

import ru.JB.develop.purchaselist.Adapters.ProductsAdapter;
import ru.JB.develop.purchaselist.Database.DBWorker;
import ru.JB.develop.purchaselist.Dialogs.AddProductDialog;
import ru.JB.develop.purchaselist.Model.ProductItems;
import ru.JB.develop.purchaselist.Model.PurchaseItem;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;

public class Products extends AppCompatActivity implements OnClickListener {
	
	enum Actions{
		Add, Delete, Edit, None
	}
	
	Actions action = Actions.None;
	
	DialogFragment addProductDialog;
	DialogFragment deleteProductDialog;
	
	RelativeLayout doneCancelLayout;

	EditText newProduct;
	Button addProductButton;
	Button acceptProductActionButton;
	Button cancelToPurchaseButton;
	
	DBWorker database;
	
	ListView productList;
	ProductItems products;
	ProductsAdapter adapter;
	
	ArrayList<PurchaseItem> selectedPurchases;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		
		addProductDialog = new AddProductDialog();
		
		products = new ProductItems(this);
		
		init();
		handleIntent(getIntent());

	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
		products.close();
	}
	
	private void init(){
	   	productList = (ListView) findViewById(R.id.purchaseList);
	   	newProduct = (EditText) findViewById(R.id.newProduct);
	    
	   	doneCancelLayout = (RelativeLayout) findViewById(R.id.done_cancel_id);
	   	
	   	addProductButton = (Button) findViewById(R.id.searchProduct);
	   	addProductButton.setOnClickListener(this);
	   	acceptProductActionButton = (Button) findViewById(R.id.accept_product_action);		
	   	acceptProductActionButton.setOnClickListener(this);
	    cancelToPurchaseButton = (Button) findViewById(R.id.cancel_product_action); 
	    cancelToPurchaseButton.setOnClickListener(this);
	    
	    adapter = new ProductsAdapter(products ,this);
	    productList.setAdapter(adapter);
	    
	    productList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> listView, View arg1,
										   int position, long id) {
				Log.d("debug ", String.valueOf(position));
				Log.d("debug ", String.valueOf(id));
				Log.d("debug ", String.valueOf(R.id.productPrice == arg1.getId()));
				Log.d("debug ", "");
				adapter.onItemEdition(position);
				action = Actions.Edit;
				acceptProductActionButton.setText(getString(R.string.save_product_edition));
				doneCancelLayout.setVisibility(View.VISIBLE);
				return true;
			}
		});
	    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.products, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_item_id).getActionView();

        if(searchView != null) {
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

				@Override
				public boolean onQueryTextSubmit(String query) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					adapter.getFilter().filter(newText);
					return false;
				}
			};

			searchView.setOnQueryTextListener(queryTextListener);
		}
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_settings:
			return true;
		case R.id.action_delete:
			onDeleteMenuButtonClick();
			return true;
		case R.id.action_add:
			Bundle args = new Bundle();

			addProductDialog.setArguments(args);
			addProductDialog.show(getFragmentManager(), "add Prod");
			return true;
		case R.id.action_add_to_purchase:
			onAddMenuButtonClick();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.accept_product_action:
			processingMenuAction(action);
			break;
		case R.id.cancel_product_action:
			doneCancelLayout.setVisibility(View.GONE);
			if(action == Actions.Edit){
				adapter.cancelEdition();
				return;
			}
			adapter.hideCheckBox();
			break;
		}	
	}
	

	public void onAddToProductDialogConfirmed(boolean isConfirmed, String newProductName, String newProductUnit , double newProductPrice){
		if(isConfirmed){
			adapter.addProduct(newProductName, newProductUnit, newProductPrice);
			}
	}
	

	
	private void processingMenuAction(Actions action){
		switch(action){
		case Add:
			//getting just Intent will optimize it?
			doneCancelLayout.setVisibility(View.GONE);
			adapter.hideCheckBox();
			action = Actions.None;
			Intent intent = new Intent(this,MainActivity.class);
			intent.putIntegerArrayListExtra("PurchaseProductIds", adapter.getCheckedProductsId());
	    	startActivity(intent);
			break;
		case Delete:
			doneCancelLayout.setVisibility(View.GONE);
			action = Actions.None;
			adapter.deleteChecked();
			adapter.hideCheckBox();
			break;
		case Edit:
			//refactor + action.Done
			adapter.saveEdition();
			doneCancelLayout.setVisibility(View.GONE);
			action = Actions.None;
		default:
			break;
		}
	}
	
	private void handleIntent(Intent intent){
		 if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            String query = intent.getStringExtra(SearchManager.QUERY);
		 }
	}
	
	private void onAddMenuButtonClick(){
		action = Actions.Add;
		adapter.getCheckBox();
		acceptProductActionButton.setText(getString(R.string.add_to_purchases_button));
		doneCancelLayout.setVisibility(View.VISIBLE);
	}	
	
	
	private void onDeleteMenuButtonClick(){
		action = Actions.Delete;
		adapter.getCheckBox();
		acceptProductActionButton.setText(getString(R.string.delete_from_products_button));
		doneCancelLayout.setVisibility(View.VISIBLE);
	}
}
