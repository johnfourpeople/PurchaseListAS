package ru.JB.develop.purchaselist;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.JB.develop.purchaselist.Adapters.PurchaseAdapter;
import ru.JB.develop.purchaselist.Model.PurchaseItems;

/**
 * Created by JB on 06.07.2015.
 */
public class PurchaseFragment extends Fragment{


    boolean deleting = false;

    ListView purchaseList;
    Activity mActivity;
    List<Integer> PurchasesId;

    PurchaseItems purchases;
    PurchaseAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        purchaseList = (ListView) view.findViewById(R.id.purchaseList);

        adapter = new PurchaseAdapter(purchases, getActivity());
        purchaseList.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purchase, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        purchases = new PurchaseItems(getActivity());

        if(getArguments() != null)
            PurchasesId = getArguments().getIntegerArrayList("PurchaseProductIds");
        if(PurchasesId != null)
            purchases.add(PurchasesId);
    }

    private void getToProducts(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container,new ProductFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        if(deleting){
            menu.findItem(R.id.action_add).setVisible(false);
        }

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
                    getActivity().invalidateOptionsMenu();
                    adapter.getDeleteCheckBox();
                } else {
                    adapter.deleteCheckedItems();
                    getActivity().invalidateOptionsMenu();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
