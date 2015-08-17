package ru.JB.develop.purchaselist.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import ru.JB.develop.purchaselist.R;

/**
 * Created by JB on 17.08.2015.
 */
public class AddPurchaseDialog extends DialogFragment implements View.OnClickListener {
    Button addPurch;
    Button cancelAddPurch;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_purchase, new FrameLayout(getActivity()));
        addPurch  = (Button) v.findViewById(R.id.add_new_purchase);
        addPurch.setOnClickListener(this);
        cancelAddPurch = (Button) v.findViewById(R.id.cancel_add_new_purchase);
        cancelAddPurch.setOnClickListener(this);
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
            dismiss();
            break;
        case R.id.cancel_add_new_purchase:
            dismiss();
            break;
        }

    }
}
