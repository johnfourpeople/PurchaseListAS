package ru.JB.develop.purchaselist;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ru.JB.develop.purchaselist.Adapters.ProductsAdapter;
import ru.JB.develop.purchaselist.Dialogs.AddProductDialog;
import ru.JB.develop.purchaselist.Model.ProductItems;

/**
 * Created by JB on 06.07.2015.
 */
public class ProductFragment extends Fragment implements View.OnClickListener{


    DialogFragment addProductDialog;

    enum Actions{
        Add, Delete, Edit, None
    }
    Actions action = Actions.None;

    ListView productList;
    EditText newProduct;

    RelativeLayout doneCancelLayout;
    Button addProductButton;
    Button acceptProductActionButton;
    Button cancelToPurchaseButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    ProductItems products;
    ProductsAdapter adapter;

    Activity mActivity;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.products, menu);

        SearchManager searchManager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_item_id).getActionView();

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(mActivity.getComponentName()));
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            };
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        products.close();
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

                addProductDialog.show(getFragmentManager(),"tag");
                return true;
            case R.id.action_add_to_purchase:
                onAddMenuButtonClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addProductDialog = new AddProductDialog();

        products = new ProductItems(mActivity);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productList = (ListView) view.findViewById(R.id.purchaseList);
        newProduct = (EditText) view.findViewById(R.id.newProduct);

        doneCancelLayout = (RelativeLayout) view.findViewById(R.id.done_cancel_id);

        addProductButton = (Button) view.findViewById(R.id.searchProduct);
        addProductButton.setOnClickListener(this);
        acceptProductActionButton = (Button) view.findViewById(R.id.accept_product_action);
        acceptProductActionButton.setOnClickListener(this);
        cancelToPurchaseButton = (Button) view.findViewById(R.id.cancel_product_action);
        cancelToPurchaseButton.setOnClickListener(this);

        adapter = new ProductsAdapter(products, mActivity);
        productList.setAdapter(adapter);

        productList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

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
            boolean isAdded = adapter.addProduct(newProductName, newProductUnit, newProductPrice);
            if(!isAdded){
                Toast notAddedMessage = Toast.makeText(getActivity(),R.string.add_error,Toast.LENGTH_LONG);
                notAddedMessage.show();
            }
        }
    }

    private void processingMenuAction(Actions action){
        switch(action){
            case Add:
                //getting just Intent will optimize it?
                doneCancelLayout.setVisibility(View.GONE);
                adapter.hideCheckBox();
                action = Actions.None;
                //TODO fragment part
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container,new PurchaseFragment()).commit();

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

    //TODO fragment part
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
