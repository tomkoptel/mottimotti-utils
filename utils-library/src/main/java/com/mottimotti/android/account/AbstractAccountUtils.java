package com.mottimotti.android.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.text.TextUtils;


public abstract class AbstractAccountUtils implements AccountUtility {
    @Override
    public Account getChosenAccount() {
        String account = getChosenAccountName();
        if (TextUtils.isEmpty(account)) {
            return null;
        } else {
            return new Account(account, getAccountType());
        }
    }

    @Override
    public void invalidateAuthToken() {
        AccountManager accountManager = AccountManager.get(getContext());
        assert accountManager != null;
        accountManager.invalidateAuthToken(getAccountType(),
                getAuthToken());
    }

    @Override
    public void removeAccount() {
        AccountManager accountManager = AccountManager.get(getContext());
        Account[] accounts = accountManager.getAccountsByType(getAccountType());
        if (accounts.length != 0) {
            accountManager.removeAccount(accounts[0], null, null);
        }
    }
}
