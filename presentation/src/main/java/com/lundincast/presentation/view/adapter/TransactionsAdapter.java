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
import com.lundincast.presentation.model.TransactionModel;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter that manages a collection of {@link TransactionModel}.
 */
public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onTransactionItemClicked(TransactionModel transactionModel);
    }

    private List<TransactionModel> transactionsCollection;
    private final LayoutInflater layoutInflater;

    private String[] colorsArray;
    private String[] colorValue;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private OnItemClickListener onItemClickListener;

    public TransactionsAdapter(Context context, List<TransactionModel> transactionCollection) {
        this.validateTransactionsCollection(transactionCollection);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.transactionsCollection = transactionCollection;
        this.colorsArray = context.getResources().getStringArray(R.array.colors_name);
        this.colorValue = context.getResources().getStringArray(R.array.colors_value);
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = this.layoutInflater.inflate(R.layout.transaction_list_entry, parent, false);

        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TransactionViewHolder viewHolder = (TransactionViewHolder) holder;

        final TransactionModel transactionModel = this.transactionsCollection.get(position);
        // extract category information to set category icon color
//        String colorName = transactionModel.getCategory().getColor();
        CategoryModel categoryModel = transactionModel.getCategory();
        String colorName = categoryModel.getColor();
        String colorCode = null;
        int it = 0;
        for (String s: colorsArray) {
            if (s.equals(colorName)) {
                colorCode = colorValue[it];
                break;
            }
            it++;
        }
        // set circle drawable color
        LayerDrawable bgDrawable = (LayerDrawable) viewHolder.ivTransactionCategory.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(Color.parseColor(colorCode));

        viewHolder.tvTransactionDate.setText(this.sdf.format(transactionModel.getDate()));
        viewHolder.tvTransactionComment.setText(transactionModel.getComment());
        viewHolder.tvTransactionPrice.setText((String.format("%.2f", transactionModel.getPrice())) + " €");
        if (transactionModel.isPending()) {
            viewHolder.iv_warning_icon.setVisibility(View.VISIBLE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TransactionsAdapter.this.onItemClickListener != null) {
                    TransactionsAdapter.this.onItemClickListener.onTransactionItemClicked(transactionModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (this.transactionsCollection != null) ? this.transactionsCollection.size(): 0;
    }

    @Override public long getItemId(int position) {
        return position;
    }

    public void setTransactionsCollection(Collection<TransactionModel> transactionModelCollection) {
        this.validateTransactionsCollection(transactionModelCollection);
        this.transactionsCollection = (List<TransactionModel>) transactionModelCollection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateTransactionsCollection(Collection<TransactionModel> transactionsCollection) {
        if (transactionsCollection == null) {
            throw new IllegalArgumentException("The transaction list cannot be null");
        }
    }


    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_transaction_category) ImageView ivTransactionCategory;
        @Bind(R.id.tv_transaction_date) TextView tvTransactionDate;
        @Bind(R.id.tv_transaction_comment) TextView tvTransactionComment;
        @Bind(R.id.tv_transaction_price) TextView tvTransactionPrice;
        @Bind(R.id.iv_warning_icon) ImageView iv_warning_icon;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
