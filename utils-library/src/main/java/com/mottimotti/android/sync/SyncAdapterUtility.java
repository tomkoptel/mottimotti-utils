package com.mottimotti.android.sync;

import android.content.SyncResult;

import com.mottimotti.android.account.AccountUtility;

import java.util.List;

public interface SyncAdapterUtility {
    public AccountUtility getAccountUtility();
    public SyncUtility getSyncUtility();
    public List<SyncHelper> getSyncHelpers(SyncResult syncResult);
}
