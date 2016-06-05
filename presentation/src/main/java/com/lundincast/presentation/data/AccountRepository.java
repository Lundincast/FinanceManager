package com.lundincast.presentation.data;

import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.model.TransactionModel;

import java.util.List;

/**
 * Public interface that represents a Repository for getting {@link AccountModel} related data.
 */
public interface AccountRepository {

    /**
     * Get a {@link List} of {@link AccountModel}.
     */
    List<AccountModel> accounts();

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
