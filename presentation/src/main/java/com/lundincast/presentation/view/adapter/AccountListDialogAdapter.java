package com.lundincast.presentation.view.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.model.AccountModel;

import java.util.List;

/**
 * Adapter that manages a collection of {@link AccountModel} for a dialog
 */
public class AccountListDialogAdapter extends BaseAdapter {

    private Context context;
    private List<AccountModel> accountList;

    public AccountListDialogAdapter(Context context, List<AccountModel> accountList) {
        this.context = context;
        this.accountList = accountList;
    }

    @Override
    public int getCount() {
        return (this.accountList != null) ? this.accountList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.accountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.category_list_entry, null);
        }
        // get color code from color name
        int color = accountList.get(position).getColor();
        // set circle drawable color
        ImageView iv_category_icon = (ImageView) convertView.findViewById(R.id.iv_category_icon);
        LayerDrawable bgDrawable = (LayerDrawable) iv_category_icon.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(color);

        TextView categoryNameTv = (TextView) convertView.findViewById(R.id.et_category_name);
        categoryNameTv.setText(accountList.get(position).getName());

        return convertView;
    }
}
