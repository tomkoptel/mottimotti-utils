package com.mottimotti.android.sync;

import android.accounts.Account;
import android.os.Bundle;

import com.mottimotti.android.account.AccountUtility;

public interface SyncUtility {
    public AccountUtility getAccountUtility();

    public void createSyncAccount(Account account);

    public void triggerRefresh(String persisterKey, Bundle bundle);

    public void triggerUpload();

    public void cancelSync();

    public long getSyncFrequency();
}
