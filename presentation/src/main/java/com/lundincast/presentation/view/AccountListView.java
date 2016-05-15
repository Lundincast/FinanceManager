package com.lundincast.presentation.view;

import com.lundincast.presentation.model.AccountModel;

import java.util.Collection;

/**
 * Interface representing a View in a model view Presenter (MVP) pattern.
 * In this case it is used as a view representing a list of {@link AccountModel}.
 */
public interface AccountListView extends LoadDataView {

    /**
     * Render a list of accounts in the UI.
     *
     * @param accountModelCollection The collection of {@link AccountModel} that will be shown.
     */
    void renderAccountList(Collection<AccountModel> accountModelCollection);

    /**
     * View a {@link AccountModel} details.
     *
     * @param accountModel The account that will be shown.
     */
    void viewAccount(AccountModel accountModel);


}
