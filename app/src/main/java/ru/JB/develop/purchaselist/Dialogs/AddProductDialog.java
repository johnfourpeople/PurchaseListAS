package ru.JB.develop.purchaselist.Dialogs;

import java.util.ArrayList;

import ru.JB.develop.purchaselist.Products;
import ru.JB.develop.purchaselist.R;
import ru.JB.develop.purchaselist.Database.DBUnits;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AddProductDialog extends DialogFragment implements OnClickListener{

    //TODO fix covering dialog by soft keyboard

	TextView productName;
	TextView productPrice;
	AutoCompleteTextView productUnit;
	DBUnits unitDatabase;
	public AddProductDialog(){
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        getDialog().setTitle(R.string.add_product);

        unitDatabase = new DBUnits(getActivity());

        ArrayList<String> units = unitDatabase.readUnits();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,units);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_product,new FrameLayout(getActivity()));
        v.findViewById(R.id.add_new_product).setOnClickListener(this);
        v.findViewById(R.id.cancel_add_new_product).setOnClickListener(this);
        productName = (TextView) v.findViewById(R.id.new_product_name);
        productPrice = (TextView) v.findViewById(R.id.new_product_price);
        productUnit = (AutoCompleteTextView) v.findViewById(R.id.new_product_unit);
        productUnit.setAdapter(adapter);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        Dialog builder = new Dialog(getActivity());
        builder.setContentView(v);

        return builder;
	}

	/*public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		getDialog().setTitle(R.string.add_product);
		
		unitDatabase = new DBUnits(getActivity());
		
		ArrayList<String> units = unitDatabase.readUnits();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,units);
		View v = inflater.inflate(R.layout.dialog_add_product, container);
		v.findViewById(R.id.add_new_product).setOnClickListener(this);
		v.findViewById(R.id.cancel_add_new_product).setOnClickListener(this);
		productName = (TextView) v.findViewById(R.id.new_product_name);
		productPrice = (TextView) v.findViewById(R.id.new_product_price);
		productUnit = (AutoCompleteTextView) v.findViewById(R.id.new_product_unit);
		productUnit.setAdapter(adapter);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );


        return v;
	}*/
	
	public void onDismiss(DialogInterface dialog){
		unitDatabase.close();
		super.onDismiss(dialog);
	}
	
	public void onCancel(DialogInterface dialog){
		unitDatabase.close();
		super.onCancel(dialog);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.add_new_product:
			Products activity = (Products) getActivity();
			if(!(productName.getText().toString().trim().isEmpty() || productPrice.getText().toString().trim().isEmpty())){
				activity.onAddToProductDialogConfirmed(true,productName.getText().toString(), productUnit.getText().toString() , Double.parseDouble(productPrice.getText().toString()));
		
				clearTextViews();		
				dismiss();
			}
			break;
		case R.id.cancel_add_new_product:
			clearTextViews();
			dismiss();
			break;
		}
	}
	
	public void clearTextViews(){
		productName.setText("");
		productUnit.setText("");
		productPrice.setText("");
	}
}

