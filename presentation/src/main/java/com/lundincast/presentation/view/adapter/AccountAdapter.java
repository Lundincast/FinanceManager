package com.lundincast.presentation.view.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.model.AccountModel;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter that manages a collection of {@link AccountModel}.
 */
public class AccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onAccountItemClicked(AccountModel accountModel);
    }

    private Context context;
    private List<AccountModel> accountCollection;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public AccountAdapter(Context context, List<AccountModel> accountCollection) {
        this.validateAccountCollection(accountCollection);
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.accountCollection = accountCollection;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = this.layoutInflater.inflate(R.layout.account_list_entry, parent, false);

        return new AccountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AccountViewHolder viewHolder = (AccountViewHolder) holder;

        final AccountModel accountModel = this.accountCollection.get(position);
        viewHolder.tv_account_name.setText(accountModel.getName());
        // set balance text color and value
        if (accountModel.getBalance() < 0) {
            viewHolder.tv_account_balance.setTextColor(context.getResources().getColor(R.color.Dark_red));
        } else {
            viewHolder.tv_account_balance.setTextColor(context.getResources().getColor(R.color.Dark_green));
        }
        viewHolder.tv_account_balance.setText((String.format("%.2f", accountModel.getBalance())));
        // get color code from color name
        int color = accountModel.getColor();
        // set circle drawable color
        LayerDrawable bgDrawable = (LayerDrawable) viewHolder.iv_account_icon.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(color);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountAdapter.this.onItemClickListener != null) {
                    AccountAdapter.this.onItemClickListener.onAccountItemClicked(accountModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (this.accountCollection != null) ? this.accountCollection.size() : 0;
    }

    public void setAccountCollection(Collection<AccountModel> accountModelCollection) {
        this.validateAccountCollection(accountModelCollection);
        this.accountCollection = (List<AccountModel>) accountModelCollection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateAccountCollection(Collection<AccountModel> accountCollection) {
        if (accountCollection == null) {
            throw new IllegalArgumentException("The account list cannot be null");
        }
    }


    static class AccountViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_account_icon) ImageView iv_account_icon;
        @Bind(R.id.tv_account_name) TextView tv_account_name;
        @Bind(R.id.tv_account_balance) TextView tv_account_balance;

        public AccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
