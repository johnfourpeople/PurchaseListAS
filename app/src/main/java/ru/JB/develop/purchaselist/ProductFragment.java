package ru.JB.develop.purchaselist;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ru.JB.develop.purchaselist.Adapters.ProductsAdapter;
import ru.JB.develop.purchaselist.Dialogs.AddProductDialog;
import ru.JB.develop.purchaselist.Model.ProductItems;

/**
 * Created by JB on 06.07.2015.
 */
public class ProductFragment extends Fragment implements View.OnClickListener {

    static final String TAG = "ProductFragment";

    DialogFragment addProductDialog;

    enum Actions{
        Add, Delete, Edit, None
    }
    Actions action = Actions.None;

    ListView productList;

    ViewGroup doneCancelLayout;
    Button acceptProductActionButton;
    Button cancelToPurchaseButton;
    ProductItems products;
    ProductsAdapter adapter;
    Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.products, menu);
        Log.d(TAG, String.valueOf(action));
        if(!action.equals(Actions.None)){
            menu.findItem(R.id.action_add_to_purchase).setVisible(false);
            menu.findItem(R.id.action_add).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_settings).setVisible(false);
        }

        SearchManager searchManager = (SearchManager)
                mActivity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)
                menu.findItem(R.id.search_item_id).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(
                    mActivity.getComponentName()));
            searchView.setOnQueryTextListener(
                    new SearchView.OnQueryTextListener() {

                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        products.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_delete:
                onDeleteMenuButtonClick();
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_add:
                //TODO interaction of fragments must be maden by Activity
                Bundle args = new Bundle();
                addProductDialog.setArguments(args);
                addProductDialog.setTargetFragment(this, 0);
                addProductDialog.show(getFragmentManager(), "Add Product Dialog");
                return true;
            case R.id.action_add_to_purchase:
                onAddMenuButtonClick();
                getActivity().invalidateOptionsMenu();
                return true;
        }
        getActivity().invalidateOptionsMenu();
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productList = (ListView) view.findViewById(R.id.purchaseList);
        doneCancelLayout = (ViewGroup)
                view.findViewById(R.id.done_cancel_id);

        acceptProductActionButton = (Button)
                view.findViewById(R.id.accept_product_action);
        acceptProductActionButton.setOnClickListener(this);

        cancelToPurchaseButton = (Button)
                view.findViewById(R.id.cancel_product_action);
        cancelToPurchaseButton.setOnClickListener(this);

        adapter = new ProductsAdapter(products, mActivity);
        productList.setAdapter(adapter);

        productList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> listView, View arg1,
                                           int position, long id) {
                adapter.onItemEdition(position);
                action = Actions.Edit;
                acceptProductActionButton.setText(getString(R.string.save_product_edition));
                doneCancelLayout.setVisibility(View.VISIBLE);
                getActivity().invalidateOptionsMenu();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.accept_product_action:
            processingMenuAction();
            getActivity().invalidateOptionsMenu();
            break;
        case R.id.cancel_product_action:
            doneCancelLayout.setVisibility(View.GONE);
            if (action == Actions.Edit) {
                adapter.cancelEdition();
                action = Actions.None;
                getActivity().invalidateOptionsMenu();
                return;
            }
            adapter.hideCheckBox();
            action = Actions.None;
            getActivity().invalidateOptionsMenu();
            break;
        }
    }

    public void onAddToProductDialogConfirmed(boolean isConfirmed,
            String newProductName, String newProductUnit,
            double newProductPrice) {
        if (isConfirmed) {
            int pos = adapter.addProduct(newProductName,
                    newProductUnit, newProductPrice);
            Log.d(TAG, String.valueOf(pos));
            //TODO check is working
            if (pos < 0) {
                Toast notAddedMessage = Toast.makeText(getActivity(),
                        R.string.add_error,Toast.LENGTH_LONG);
                notAddedMessage.show();
            }
            productList.setSelection(pos);
        }
    }

    private void processingMenuAction() {
        switch (action) {
        case Add:
            //TODO getting just other fragment will optimize it?

            doneCancelLayout.setVisibility(View.GONE);
            adapter.hideCheckBox();
            action = Actions.None;
            PurchaseFragment purchaseFragment = PurchaseFragment.newInstance(
                    (ArrayList<Integer>) adapter.getCheckedProductsId());
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container,
                    purchaseFragment).addToBackStack(null).commit();
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

    private void onAddMenuButtonClick() {
        action = Actions.Add;
        adapter.getCheckBox();
        acceptProductActionButton.setText(getString(R.string.add_to_purchases_button));
        doneCancelLayout.setVisibility(View.VISIBLE);
    }

    private void onDeleteMenuButtonClick() {
        action = Actions.Delete;
        adapter.getCheckBox();
        acceptProductActionButton.setText(
                getString(R.string.delete_from_products_button));
        doneCancelLayout.setVisibility(View.VISIBLE);
    }
}