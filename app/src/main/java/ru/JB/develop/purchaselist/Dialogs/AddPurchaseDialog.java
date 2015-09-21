package ru.JB.develop.purchaselist.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import ru.JB.develop.purchaselist.Model.ProductItem;
import ru.JB.develop.purchaselist.Model.ProductItems;
import ru.JB.develop.purchaselist.ProductFragment;
import ru.JB.develop.purchaselist.PurchaseFragment;
import ru.JB.develop.purchaselist.R;

/**
 * Created by JB on 17.08.2015.
 */
public class AddPurchaseDialog extends DialogFragment implements View.OnClickListener {
    Button addPurch;
    Button cancelAddPurch;
    View browseProduct;
    AutoCompleteTextView newPurchase;
    ProductItems products;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.d_add_purchase, new FrameLayout(getActivity()));
        addPurch  = (Button) v.findViewById(R.id.add_new_purchase);
        addPurch.setOnClickListener(this);
        cancelAddPurch = (Button) v.findViewById(R.id.cancel_add_new_purchase);
        cancelAddPurch.setOnClickListener(this);
        browseProduct = v.findViewById(R.id.browse_product);
        browseProduct.setOnClickListener(this);
        products = new ProductItems(getActivity());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, products.getProductNames());
            newPurchase = (AutoCompleteTextView) v.findViewById(R.id.new_purchase_name);
            newPurchase.setAdapter(adapter);

        Dialog builder = new Dialog(getActivity());
        builder.setContentView(v);
        builder.setTitle(R.string.add_product);
        return builder;
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.add_new_purchase:
            if (!newPurchase.getText().toString().trim().isEmpty()) {
                int newId = products.add(new ProductItem(newPurchase.getText().toString()));
                List<Integer> id = new ArrayList<>();
                id.add(Math.abs(newId));
                PurchaseFragment fragment = PurchaseFragment.newInstance(id);
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.container, fragment)
                        .commit();
                dismiss();
            }
            break;
        case R.id.cancel_add_new_purchase:
            dismiss();
            break;
        case R.id.browse_product:
            android.support.v4.app.FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.container, new ProductFragment())
                    .addToBackStack(null).commit();
            dismiss();
            break;
        }
    }
}
