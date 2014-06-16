package com.mottimotti.android.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import org.springframework.web.client.RestClientException;

import java.util.List;

public abstract  class AbstractSyncAdapter extends AbstractThreadedSyncAdapter
        implements SyncAdapterUtility {

    private final ContentResolver mContentResolver;
    private List<SyncHelper> mSyncHelpers;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public AbstractSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AbstractSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras,
                              String authority, ContentProviderClient provider,
                              SyncResult syncResult) {
        final boolean uploadOnly = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);
        final boolean manualSync = extras.getBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, false);
        final boolean initialize = extras.getBoolean(ContentResolver.SYNC_EXTRAS_INITIALIZE, false);

        String chosenAccountName = getAccountUtility().getChosenAccountName();
        boolean isAccountSet = !TextUtils.isEmpty(chosenAccountName);
        boolean isChosenAccount = isAccountSet && chosenAccountName.equals(account.name);
        if (isAccountSet) {
            ContentResolver.setIsSyncable(account, authority, isChosenAccount ? 1 : 0);
        }
        if (!isChosenAccount) {
            ++syncResult.stats.numAuthExceptions;
            return;
        }

        startSync(syncResult, extras);
    }

    private void startSync(SyncResult syncResult, Bundle extras) {
        final boolean manualSync = extras.getBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, false);
        // Perform a sync using SyncHelper
        if (mSyncHelpers == null) {
            mSyncHelpers = getSyncHelpers();

        }
        int flag = SyncHelper.FLAG_SYNC_REMOTE;
        if (manualSync) flag |= SyncHelper.FLAG_SYNC_MANUAL;

        for (SyncHelper syncHelper : mSyncHelpers) {
            try {
                syncHelper.performSync(extras, flag);
            } catch (Exception ex) {
                if (ex instanceof RestClientException) {
                    ++syncResult.stats.numIoExceptions;
                }
            }
        }

        getSyncUtility().cancelSync();
    }
}
