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
    private String currencySymbol;

    // Three constants for defining transaction's viewType
    private final int EXPENSE = 0, INCOME = 1, TRANSFER = 2;

    private OnItemClickListener onItemClickListener;

    public TransactionsAdapter(Context context, List<TransactionModel> transactionCollection) {
        this.validateTransactionsCollection(transactionCollection);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.transactionsCollection = transactionCollection;
        this.setCurrencySymbol(context);
    }

    @Override
    public int getItemViewType(int position) {
        if ((transactionsCollection.get(position).getTransactionType()).equals(CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)) {
            return EXPENSE;
        } else if ((transactionsCollection.get(position).getTransactionType()).equals(CreateTransactionActivity.TRANSACTION_TYPE_INCOME)) {
            return INCOME;
        } else {
            return TRANSFER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case EXPENSE:
                View v1 = this.layoutInflater.inflate(R.layout.transaction_list_entry, parent, false);
                viewHolder = new TransactionViewHolder(v1);
                break;
            case INCOME:
                View v2 = this.layoutInflater.inflate(R.layout.income_list_entry, parent, false);
                viewHolder = new IncomeViewHolder(v2);
                break;
            case TRANSFER:
                View v3 = this.layoutInflater.inflate(R.layout.transfer_list_entry, parent, false);
                viewHolder = new TransferViewHolder(v3);
                break;
            default:
                viewHolder = null;
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final TransactionModel transactionModel = this.transactionsCollection.get(position);

        Calendar cal = Calendar.getInstance();
        cal.setTime(transactionModel.getDate());

        switch (holder.getItemViewType()) {
            case 0:
                TransactionViewHolder expenseVH = (TransactionViewHolder) holder;

                // extract category or account information to set icon color
                int color = 0;
                if (transactionModel.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)) {
                    CategoryModel categoryModel = transactionModel.getCategory();
                    color = categoryModel.getColor();
                } else if (transactionModel.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_INCOME)) {
                    AccountModel accountModel = transactionModel.getFromAccount();
                    if (accountModel != null) {
                        color = accountModel.getColor();
                    }
                }
                // set circle drawable color
                LayerDrawable bgDrawable = (LayerDrawable) expenseVH.ivTransactionCategory.getBackground();
                final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
                shape.setColor(color);
                expenseVH.tvDayOfWeek.setText(CustomDateFormatter.getShortDayOfWeekName(cal));
                expenseVH.tvTransactionDate.setText(CustomDateFormatter.getShortFormattedDate(cal));
                expenseVH.tvTransactionComment.setText(transactionModel.getComment());
                expenseVH.tvTransactionPrice.setText((String.format("%.2f", transactionModel.getPrice())) + this.currencySymbol);
                expenseVH.tvTransactionPrice.setTextColor(Color.RED);
                if (transactionModel.isPending()) {
                    expenseVH.iv_pending_icon.setVisibility(View.VISIBLE);
                } else {
                    expenseVH.iv_pending_icon.setVisibility(View.GONE);
                }
                expenseVH.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TransactionsAdapter.this.onItemClickListener != null) {
                            TransactionsAdapter.this.onItemClickListener.onTransactionItemClicked(transactionModel);
                        }
                    }
                });
                break;
            case 1:
                IncomeViewHolder incomeVH = (IncomeViewHolder) holder;
                incomeVH.tvDayOfWeek.setText(CustomDateFormatter.getShortDayOfWeekName(cal));
                incomeVH.tvTransactionDate.setText(CustomDateFormatter.getShortFormattedDate(cal));
                incomeVH.tvTransactionComment.setText(transactionModel.getComment());
                incomeVH.tvTransactionPrice.setText((String.format("%.2f", transactionModel.getPrice())) + this.currencySymbol);
                incomeVH.tvTransactionPrice.setTextColor(Color.parseColor("#4caf50"));
                incomeVH.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TransactionsAdapter.this.onItemClickListener != null) {
                            TransactionsAdapter.this.onItemClickListener.onTransactionItemClicked(transactionModel);
                        }
                    }
                });
                break;
            case 2:
                TransferViewHolder transferVH = (TransferViewHolder) holder;
                transferVH.tvDayOfWeek.setText(CustomDateFormatter.getShortDayOfWeekName(cal));
                transferVH.tvTransferDate.setText(CustomDateFormatter.getShortFormattedDate(cal));
                transferVH.tvTransferComment.setText(
                        transactionModel.getFromAccount().getName() + " -> " + transactionModel.getToAccount().getName());
                transferVH.tvTransferPrice.setText((String.format("%.2f", transactionModel.getPrice())) + this.currencySymbol);
                transferVH.tvTransferPrice.setTextColor(Color.parseColor("#9C27B0"));
                transferVH.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TransactionsAdapter.this.onItemClickListener != null) {
                            TransactionsAdapter.this.onItemClickListener.onTransactionItemClicked(transactionModel);
                        }
                    }
                });
                break;
            default:

                break;
        }
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


    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_transaction_category) ImageView ivTransactionCategory;
        @Bind(R.id.tv_day_of_week) TextView tvDayOfWeek;
        @Bind(R.id.et_transaction_date) TextView tvTransactionDate;
        @Bind(R.id.tv_transaction_comment) TextView tvTransactionComment;
        @Bind(R.id.tv_transaction_price) TextView tvTransactionPrice;
        @Bind(R.id.iv_pending_icon) ImageView iv_pending_icon;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class IncomeViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_day_of_week) TextView tvDayOfWeek;
        @Bind(R.id.et_transaction_date) TextView tvTransactionDate;
        @Bind(R.id.tv_transaction_comment) TextView tvTransactionComment;
        @Bind(R.id.tv_transaction_price) TextView tvTransactionPrice;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class TransferViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_day_of_week) TextView tvDayOfWeek;
        @Bind(R.id.et_transfer_date) TextView tvTransferDate;
        @Bind(R.id.tv_transfer_comment) TextView tvTransferComment;
        @Bind(R.id.tv_transfer_price) TextView tvTransferPrice;

        public TransferViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}