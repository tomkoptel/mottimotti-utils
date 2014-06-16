package com.mottimotti.android.account;


import android.accounts.Account;
import android.content.Context;

public interface AccountUtility {
    public abstract Account getChosenAccount();

    public abstract void invalidateAuthToken();

    public abstract void removeAccount();

    public abstract boolean isAuthenticated();

    public abstract boolean isSetupComplete();

    public abstract void setSetupComplete(boolean complete);

    public abstract String getAuthToken();

    public abstract void setAuthToken(String token);

    public abstract String getAccountType();

    public abstract String getChosenAccountName();

    public abstract void setChosenAccountName(String accountName);

    public abstract Context getContext();

    public abstract String getContentAuthority();
}
