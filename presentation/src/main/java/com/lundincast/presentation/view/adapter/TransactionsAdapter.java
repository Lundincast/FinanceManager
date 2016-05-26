package com.lundincast.presentation.view.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.utils.CustomDateFormatter;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;

import java.util.Calendar;
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
    private String currencyPref;

    private OnItemClickListener onItemClickListener;

    public TransactionsAdapter(Context context, List<TransactionModel> transactionCollection) {
        this.validateTransactionsCollection(transactionCollection);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.transactionsCollection = transactionCollection;
        this.setCurrencyPref(context);
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
        // extract category or account information to set icon color
        int color = 0;
        if (transactionModel.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)) {
            CategoryModel categoryModel = transactionModel.getCategory();
            color = categoryModel.getColor();
        } else if (transactionModel.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_INCOME)) {
            AccountModel accountModel = transactionModel.getFromAccount();
            color = accountModel.getColor();
        }
        // set circle drawable color
        LayerDrawable bgDrawable = (LayerDrawable) viewHolder.ivTransactionCategory.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(color);

        Calendar cal = Calendar.getInstance();
        cal.setTime(transactionModel.getDate());
        viewHolder.tvDayOfWeek.setText(CustomDateFormatter.getShortDayOfWeekName(cal));
        viewHolder.tvTransactionDate.setText(CustomDateFormatter.getShortFormattedDate(cal));
        viewHolder.tvTransactionComment.setText(transactionModel.getComment());
        // determine currency from preferences
        String currency;
        if (currencyPref.equals("2")) {
            currency = " $";
        } else if (currencyPref.equals("3")) {
            currency = " £";
        } else {
            currency = " €";
        }
        viewHolder.tvTransactionPrice.setText((String.format("%.2f", transactionModel.getPrice())) + currency);
        // set price color depending if expense or income
        if (transactionModel.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)) {
            viewHolder.tvTransactionPrice.setTextColor(Color.RED);
        } else if (transactionModel.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_INCOME)) {
            viewHolder.tvTransactionPrice.setTextColor(Color.GREEN);
        }

        if (transactionModel.isPending()) {
            viewHolder.iv_pending_icon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_pending_icon.setVisibility(View.GONE);
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

    public void setCurrencyPref(Context context) {
        this.currencyPref = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("pref_key_currency", "1");
    }


    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_transaction_category) ImageView ivTransactionCategory;
        @Bind(R.id.tv_day_of_week) TextView tvDayOfWeek;
        @Bind(R.id.tv_transaction_date) TextView tvTransactionDate;
        @Bind(R.id.tv_transaction_comment) TextView tvTransactionComment;
        @Bind(R.id.tv_transaction_price) TextView tvTransactionPrice;
        @Bind(R.id.iv_pending_icon) ImageView iv_pending_icon;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}