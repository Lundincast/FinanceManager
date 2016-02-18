package com.lundincast.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lundincast.presentation.model.TransactionModel;

import java.util.Collection;
import java.util.List;

/**
 * Adapter that manages a collection of {@link TransactionModel}.
 */
public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onTransactionItemClicked(TransactionModel transactionModel);
    }

    private List<TransactionModel> transactionsCollection;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public TransactionsAdapter(Context context, Collection<TransactionModel> transactionCollection) {
        this.validateTransactionsCollection(transactionCollection);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.transactionsCollection = (List<TransactionModel>) transactionCollection;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO
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



}
