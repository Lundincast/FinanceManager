package com.lundincast.presentation.presenter;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lundincast.presentation.R;
import com.lundincast.presentation.data.AccountRepository;
import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.view.activity.CreateOrUpdateAccountActivity;
import com.lundincast.presentation.view.fragment.CreateOrUpdateAccountFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class CreateAccountPresenter implements Presenter {

    private Realm realm;

    private SharedPreferences sharedPreferences;
    private final AccountRepository accountRepository;
    private CreateOrUpdateAccountFragment viewListView;

    private AccountModel accountModel;
    private String currencySymbol;

    @Inject
    public CreateAccountPresenter(SharedPreferences sharedPreferences, AccountRepository accountRepository) {
        this.sharedPreferences = sharedPreferences;
        this.accountRepository = accountRepository;
        this.realm = Realm.getDefaultInstance();
    }

    /**
     * Setter method to reference corresponding view (CreateOrUpdateAccountFragment in this case)
     *
     * @param view
     */
    public void setView(CreateOrUpdateAccountFragment view) {
        this.viewListView = view;
    }

    public void initialize(long accountId) {
        // get currency setting from SharedPreferences
        String currencyPref = sharedPreferences.getString("pref_key_currency", "1");
        if (currencyPref.equals("2")) {
            this.currencySymbol = " $";
        } else if (currencyPref.equals("3")) {
            this.currencySymbol = " £";
        } else {
            this.currencySymbol = " €";
        }
        // set default values
        if (accountId != -1) {
            // Toolbar title by default is "New category". Set to "Edit account" in this case.
            viewListView.setToolbarTitle(R.string.edit_account);
            // set account name
            accountModel = realm.where(AccountModel.class).equalTo("id", accountId).findFirst();
            viewListView.setName(accountModel.getName());
            // set color in parent activity (due to library limitation) and in fragment
            ((CreateOrUpdateAccountActivity) viewListView.getActivity()).color = accountModel.getColor();
            viewListView.setColor(accountModel.getColor());
            viewListView.setBalanceLabel(R.string.current_balance);
            viewListView.setBalance(accountModel.getBalance());
            // Display delete icon since it's an existing account
            viewListView.showDeleteIcon();
        } else {
            // set color in parent activity (due to library limitation) and in fragment
            ((CreateOrUpdateAccountActivity) viewListView.getActivity()).color = 0xfff44336;
            viewListView.setColor(((CreateOrUpdateAccountActivity) viewListView.getActivity()).color);
            viewListView.setBalanceLabel(R.string.initial_balance);
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.realm = null;
    }

    public void setDeleteDialogBuilder() {
        // get list of transactions made under this account
//        RealmResults<TransactionModel> transactionList = realm.where(TransactionModel.class)
//                                                              .equalTo("account.name", this.accountModel.getName())
//                                                              .findAll();

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ArrayList<TransactionModel> transactionList = new ArrayList<>();
        String contentText;
        if (transactionList.size() != 0) {
            contentText = transactionList.size() + " transactions pertain to this account. Note that " +
                    "deleting an account will also delete all transactions associated with it.";
        } else {
            contentText = ((CreateOrUpdateAccountFragment) viewListView).getResources().getString(R.string.delete_account_complete_question);
        }

        // Display dialog to ask user confirmation to delete account
        MaterialDialog.Builder builder =
                new MaterialDialog.Builder((CreateOrUpdateAccountActivity) viewListView.getActivity())
                    .title(R.string.delete_account_title_question)
                    .content(contentText)
                    .positiveText(R.string.delete)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            CreateAccountPresenter.this.deleteAccount(CreateAccountPresenter.this.accountModel.getId());
                            dialog.dismiss();
                            CreateAccountPresenter.this.viewListView.closeActivity();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    });

        // pass builder to fragment for display
        this.viewListView.showDialog(builder);
    }

    public void saveAccount(Long accountId, String accountName, int accountColor, double balance) {
        if (!accountName.equals("")) {
            AccountModel accountModel = new AccountModel();
            accountModel.setId(accountId);
            accountModel.setName(accountName);
            accountModel.setColor(accountColor);
            accountModel.setBalance(balance);
            this.accountRepository.saveAccount(accountModel);
        }
    }

    public void deleteAccount(Long accountId) {
        this.accountRepository.deleteAccount(accountId);
    }
}
