package com.lundincast.presentation.view.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.model.CategoryModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter that manages a collection of {@link CategoryModel} for a dialog
 */
public class CategoryListDialogAdapter extends BaseAdapter {

    private List<CategoryModel> categoryList;

    private final LayoutInflater inflater;

    public CategoryListDialogAdapter(Context context, List<CategoryModel> categoryList) {
        this.inflater = LayoutInflater.from(context);
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return (this.categoryList != null) ? this.categoryList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.category_list_entry, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        // if "All" entry, set list icon in ImageView instead of colored circle
        if (categoryList.get(position).getName().equals("All")) {
            holder.iv_category_icon.setVisibility(View.GONE);
            holder.iv_list_icon.setVisibility(View.VISIBLE);
        } else {
            // get color code from color name
            int color = categoryList.get(position).getColor();
            // set circle drawable color
            LayerDrawable bgDrawable = (LayerDrawable) holder.iv_category_icon.getBackground();
            final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
            shape.setColor(color);
        }

        holder.et_category_name.setText(categoryList.get(position).getName());

        return convertView;
    }


    static final class ViewHolder {

        @Bind(R.id.iv_category_icon) ImageView iv_category_icon;
        @Bind(R.id.iv_list_icon) ImageView iv_list_icon;
        @Bind(R.id.et_category_name) TextView et_category_name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
