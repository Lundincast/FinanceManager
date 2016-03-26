package com.lundincast.presentation.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.model.CategoryModel;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter that manages a collection of {@link CategoryModel}.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onCategoryItemClicked(CategoryModel categoryModel);
    }

    private List<CategoryModel> categoryCollection;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public CategoriesAdapter(Context context, List<CategoryModel> categoryCollection) {
        this.validateCategoryCollection(categoryCollection);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categoryCollection = categoryCollection;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = this.layoutInflater.inflate(R.layout.category_list_entry, parent, false);

        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CategoryViewHolder viewHolder = (CategoryViewHolder) holder;

        final CategoryModel categoryModel = this.categoryCollection.get(position);
        viewHolder.tv_category_name.setText(categoryModel.getName());
        // get color code from color name
        int color = categoryModel.getColor();
        // set circle drawable color
        LayerDrawable bgDrawable = (LayerDrawable) viewHolder.iv_category_icon.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(color);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CategoriesAdapter.this.onItemClickListener != null) {
                    CategoriesAdapter.this.onItemClickListener.onCategoryItemClicked(categoryModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (this.categoryCollection != null) ? this.categoryCollection.size() : 0;
    }

    @Override public long getItemId(int position) {
        return position;
    }

    public void setCategoryCollection(Collection<CategoryModel> categoryModelCollection) {
        this.validateCategoryCollection(categoryModelCollection);
        this.categoryCollection = (List<CategoryModel>) categoryModelCollection;
        this.notifyDataSetChanged();
    }


    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateCategoryCollection(Collection<CategoryModel> categoryCollection) {
        if (categoryCollection == null) {
            throw new IllegalArgumentException("The category list cannot be null");
        }
    }


    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_category_icon) ImageView iv_category_icon;
        @Bind(R.id.tv_category_name) TextView tv_category_name;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
