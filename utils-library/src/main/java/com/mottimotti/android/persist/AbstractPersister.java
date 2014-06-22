package com.mottimotti.android.persist;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import java.util.ArrayList;

public abstract class AbstractPersister<T> implements Persister<T> {
    private final Context mContext;
    private final SyncResult mSyncResult;

    public AbstractPersister(Context context, SyncResult syncResult) {
        mContext = context;
        mSyncResult = syncResult;
    }

    protected final void incrementInsert() {
        mSyncResult.stats.numInserts++;
    }

    protected final void incrementUpdate() {
        mSyncResult.stats.numUpdates++;
    }

    protected final void incrementDelete() {
        mSyncResult.stats.numDeletes++;
    }

    public final Context getContext() {
        return mContext;
    }

    public final ContentResolver getContentResolver() {
        return mContext.getContentResolver();
    }

    public final SyncResult getSyncResult() {
        return mSyncResult;
    }

    public final ArrayList<ContentProviderOperation> fetchAndPersist(Bundle bundle) { return persist(fetch(bundle));}

    public final ArrayList<ContentProviderOperation> fetchAndPersist() { return persist(fetch(null));}
}