package com.lundincast.presentation.view.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.OverheadModel;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter that manages a collection of {@link OverheadModel}.
 */
public class OverheadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onOverheadsItemClicked(OverheadModel overheadModel);
    }

    private List<OverheadModel> overheadsCollection;
    private final LayoutInflater layoutInflater;
    private String currencySymbol;

    private OnItemClickListener onItemClickListener;

    public OverheadsAdapter(Context context, List<OverheadModel> overheadsCollection) {
        this.validateOverheadsCollection(overheadsCollection);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.overheadsCollection = overheadsCollection;
        this.setCurrencySymbol(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = this.layoutInflater.inflate(R.layout.overheads_list_entry, parent, false);

        return new OverheadsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        OverheadsViewHolder viewHolder = (OverheadsViewHolder) holder;

        final OverheadModel overheadModel = this.overheadsCollection.get(position);
        // extract category information to set category icon color
        CategoryModel categoryModel = overheadModel.getCategory();
        int color = categoryModel.getColor();
        // set circle drawable color
        LayerDrawable bgDrawable = (LayerDrawable) viewHolder.iv_overheads_category.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(color);

        viewHolder.tv_category_name.setText(overheadModel.getCategory().getName());
        viewHolder.tv_overheads_comment.setText(overheadModel.getComment());
        short dayOfMonth = overheadModel.getDayOfMonth();
        String dayNbInString;
        if (dayOfMonth == 1 || dayOfMonth == 21) {
            dayNbInString = dayOfMonth + "st";
        } else if (dayOfMonth == 2 || dayOfMonth == 22) {
            dayNbInString = dayOfMonth + "nd";
        } else if(dayOfMonth == 3 || dayOfMonth == 23) {
            dayNbInString = dayOfMonth + "rd";
        } else {
            dayNbInString = dayOfMonth + "th";
        }
        viewHolder.tv_overheads_recurring_time.setText("On " + dayNbInString + " of month");
        viewHolder.tv_overheads_price.setText((String.format("%.2f", overheadModel.getPrice())) + this.currencySymbol);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OverheadsAdapter.this.onItemClickListener != null) {
                    OverheadsAdapter.this.onItemClickListener.onOverheadsItemClicked(overheadModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (this.overheadsCollection != null) ? this.overheadsCollection.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOverheadsCollection(Collection<OverheadModel> overheadModelCollection) {
        this.validateOverheadsCollection(overheadModelCollection);
        this.overheadsCollection = (List<OverheadModel>) overheadModelCollection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateOverheadsCollection(Collection<OverheadModel> overheadsCollection) {
        if (overheadsCollection == null) {
            throw new IllegalArgumentException("The overheads list cannot be null");
        }
    }

    public void setCurrencySymbol(Context context) {
        String currencyPref = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("pref_key_currency", "1");
        if (currencyPref.equals("2")) {
            this.currencySymbol = " $";
        } else if (currencyPref.equals("3")) {
            this.currencySymbol = " £";
        } else {
            this.currencySymbol = " €";
        }
    }


    static class OverheadsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_overheads_category) ImageView iv_overheads_category;
        @Bind(R.id.et_category_name) TextView tv_category_name;
        @Bind(R.id.tv_overheads_comment) TextView tv_overheads_comment;
        @Bind(R.id.tv_overheads_price) TextView tv_overheads_price;
        @Bind(R.id.tv_overheads_recurring_time) TextView tv_overheads_recurring_time;

        public OverheadsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
