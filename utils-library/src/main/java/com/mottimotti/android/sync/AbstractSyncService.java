package com.mottimotti.android.sync;

import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service that handles sync. It simply instantiates a SyncAdapter and returns its IBinder.
 */
public abstract class AbstractSyncService extends Service {
    private static final Object S_SYNC_ADAPTER_LOCK = new Object();
    private static AbstractThreadedSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (S_SYNC_ADAPTER_LOCK) {
            if (sSyncAdapter == null) {
                sSyncAdapter = getSyncAdapter();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }

    public abstract AbstractThreadedSyncAdapter getSyncAdapter();
}
