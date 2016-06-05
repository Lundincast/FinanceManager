package com.lundincast.presentation.data.datasource;

import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.model.TransactionModel;

import java.io.IOException;
import java.util.List;

/**
 * Interface that represents a data store from where account data is retrieved.
 */
public interface AccountDataStore {

    /**
     * Get a {@link List} of {@link AccountModel}.
     */
    List<AccountModel> accountList() throws IOException;

    /**
     * Save an {@link AccountModel} in database
     */
    void saveAccount(final AccountModel accountModel);

    /**
     * Delete an {@link AccountModel} by id in database
     */
    void deleteAccount(final long accountId);

    /**
     * Update an {@link AccountModel}'s balance with transaction value
     */
    void updateAccountBalance(final long accountId, final double delta);
}
