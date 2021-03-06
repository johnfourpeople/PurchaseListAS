package ru.JB.develop.purchaselist;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import ru.JB.develop.purchaselist.Dialogs.AddPurchaseDialog;
import ru.JB.develop.purchaselist.Model.PurchaseItems;

/**
 * Created by JB on 06.07.2015.
 */
public class PurchaseFragment extends Fragment {

    boolean deleting = false;

    DialogFragment addPurchaseDialog;

    ListView purchaseList;
    Activity mActivity;
    List<Integer> PurchasesId;

    PurchaseItems purchases;
    PurchaseAdapter adapter;

    public static PurchaseFragment newInstance(List<Integer> newPurchases) {
        PurchaseFragment purchaseFragment = new PurchaseFragment();

        Bundle args = new Bundle();
        args.putIntegerArrayList("PurchaseProductIds", (ArrayList<Integer>)newPurchases);
        purchaseFragment.setArguments(args);
        return purchaseFragment;
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f_purchase, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPurchaseDialog = new AddPurchaseDialog();

        setHasOptionsMenu(true);

        purchases = new PurchaseItems(getActivity());

        if (getArguments() != null) {
            PurchasesId = getArguments().getIntegerArrayList("PurchaseProductIds");
            if (PurchasesId != null) {
                purchases.add(PurchasesId);
            }
            getArguments().clear();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.action_add_purchase:
            addPurchaseDialog.setTargetFragment(this, 0);
            addPurchaseDialog.show(getFragmentManager(), getString(R.string.action_add_to_purchase));
                return true;
        case R.id.action_delete:
            deleting = !deleting;
            if (deleting) {
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
