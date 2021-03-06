package ru.JB.develop.purchaselist.Adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.JB.develop.purchaselist.Model.PurchaseItem;
import ru.JB.develop.purchaselist.R;
import ru.JB.develop.purchaselist.Model.PurchaseItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class PurchaseAdapter extends BaseAdapter implements OnClickListener {

    static final String TAG = "PurchaseAdapter";

    PurchaseItems purchases;
    List<PurchaseItem> viewItems;
    Context context;
    boolean deleting = false;
    Comparator<PurchaseItem> purchaseComparator = new PurchaseComparator();
    List<Integer> idsForDelete = new ArrayList<Integer>();

    public PurchaseAdapter(PurchaseItems items, Context cntxt) {
        purchases = items;
        context = cntxt;
        viewItems = purchases.getAll();
        Collections.sort(viewItems, purchaseComparator);
    }

         static class ViewHolder {
                TextView purchaseName;
                CheckBox purchaseIsBought;
         }

    public void getDeleteCheckBox() {
        deleting = true;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return viewItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return viewItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void deleteCheckedItems() {
        if (idsForDelete != null) {
            purchases.delete(idsForDelete);
            viewItems.clear();
            viewItems.addAll(purchases.getAll());
            idsForDelete.clear();
            deleting = false;
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int index, View rowView, ViewGroup arg2) {
        ViewHolder holder;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.purchase_item, arg2, false);
            holder = new ViewHolder();
            holder.purchaseName = (TextView)
                    rowView.findViewById(R.id.purchaseName);
            holder.purchaseIsBought = (CheckBox)
                    rowView.findViewById(R.id.purchaseIsBought);
            rowView.setTag(holder);
        } else {
        holder = (ViewHolder) rowView.getTag();
        }

        holder.purchaseName.setText(viewItems.get(index).getPurchaseName());
        holder.purchaseIsBought.setTag(viewItems.get(index).getProductId());
        holder.purchaseIsBought.setOnClickListener(this);

        if (deleting) {
            holder.purchaseIsBought.setBackgroundResource(R.color.Red);
            holder.purchaseIsBought.setChecked(idsForDelete.contains(
                    viewItems.get(index).getProductId()));
        } else {
            holder.purchaseIsBought.setChecked(
                    viewItems.get(index).getPurchaseIsBought());
            rowView.setBackgroundResource((viewItems.get(index).getPurchaseIsBought()?R.color.disabled:R.color.Transparent));
        }
        return rowView;
    }

    @Override
    public void onClick(View arg0) {
        Boolean isChecked = ((CheckBox) arg0).isChecked();
        if (deleting) {
            if (isChecked) {
                idsForDelete.add((Integer) arg0.getTag());
            } else {
                idsForDelete.remove(arg0.getTag());
            }
        } else {
            purchases.getById((Integer)arg0.getTag()).setPurchaseIsBought(isChecked);
            Collections.sort(viewItems, purchaseComparator);
        }
        notifyDataSetChanged();
    }

    private class PurchaseComparator implements Comparator<PurchaseItem> {
        @Override
        public int compare(PurchaseItem lhs, PurchaseItem rhs) {
            int result;
            result = ((Boolean)lhs.getPurchaseIsBought()).compareTo(rhs.getPurchaseIsBought());
            if (result == 0 ){
                result = lhs.getPurchaseName().compareTo(rhs.getPurchaseName());
            }
            return result;
        }
    }
}
