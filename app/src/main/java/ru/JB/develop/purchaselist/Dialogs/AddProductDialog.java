package ru.JB.develop.purchaselist.Dialogs;



import ru.JB.develop.purchaselist.ProductFragment;
import ru.JB.develop.purchaselist.R;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AddProductDialog extends DialogFragment implements OnClickListener {

    //TODO fix covering dialog by soft keyboard

    TextView productName;
     public AddProductDialog(){}

    //TODO check perfomance oncreateView vs oncreateDialog

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        View v = getActivity().getLayoutInflater().inflate(
                R.layout.d_add_product,new FrameLayout(getActivity()));
        v.findViewById(R.id.add_new_product).setOnClickListener(this);
        v.findViewById(R.id.cancel_add_new_product).setOnClickListener(this);
        productName = (TextView) v.findViewById(R.id.new_product_name);

        Dialog builder = new Dialog(getActivity());
        builder.setContentView(v);
        builder.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        builder.setTitle(getString(R.string.add_product));
        return builder;
    }


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.add_new_product:
            //TODO  make call form interface
            ProductFragment fragment = (ProductFragment) getTargetFragment();
            if (!(productName.getText().toString().trim().isEmpty())) {
                if (fragment != null) {
                    fragment.onAddToProductDialogConfirmed(true,
                            productName.getText().toString());
                    clearTextViews();
                    dismiss();
                }
            }
            break;
        case R.id.cancel_add_new_product:
            clearTextViews();
            dismiss();
            break;
        }
    }

    public void clearTextViews() {
        productName.setText("");

    }
}