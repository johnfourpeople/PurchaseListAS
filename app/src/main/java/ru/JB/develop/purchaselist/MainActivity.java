package ru.JB.develop.purchaselist;

import java.util.ArrayList;

import ru.JB.develop.purchaselist.Adapters.PurchaseAdapter;
import ru.JB.develop.purchaselist.Model.PurchaseItems;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity{

	Boolean deleting = false;
	
	ListView purchaseList;
	
	ArrayList<Integer> PurchasesId;
	
	PurchaseItems purchases;
	PurchaseAdapter adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        purchases = new PurchaseItems(this);
        
        Intent intent = getIntent();
        PurchasesId = intent.getIntegerArrayListExtra("PurchaseProductIds");
        
        if(PurchasesId != null)
        	purchases.add(PurchasesId);
        init();
    }

    
    private void getToProducts(){
    	Intent intent = new Intent(this,Products.class);
    	startActivity(intent);
    }
    
    private void init(){
    	purchaseList = (ListView) findViewById(R.id.purchaseList);
    	
        adapter = new PurchaseAdapter(purchases ,this);
        purchaseList.setAdapter(adapter);
    }

    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if(deleting){
        	menu.findItem(R.id.action_add).setVisible(false);
        }
        	
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
        case R.id.action_settings:
        	return true;
        case R.id.action_add:
        	getToProducts();
        	return true;
        case R.id.action_delete:
        	deleting = !deleting;
        	if(deleting){
        		invalidateOptionsMenu();
        		adapter.getDeleteCheckBox();
        	} else {
        		adapter.deleteCheckedItems();
        		invalidateOptionsMenu();
        	}
        	return true;
        	default: 
        		Log.e("Options MainActivity","haven't such id of button");
        		return super.onOptionsItemSelected(item);
        }
    }
}
