package com.mottimotti.android.sync;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;

import com.mottimotti.android.persist.AbstractPersister;

import org.springframework.web.client.RestClientException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mottimotti.android.utils.ConnectionDetector.isOnline;


public final class SyncHelper {
    public static final String SYNC_EXTRAS_CACHED = "SYNC_CACHED";

    public static final int FLAG_SYNC_MANUAL = 0x1;
    public static final int FLAG_SYNC_REMOTE = 0x4;
    public static final int FLAG_SYNC_CACHED = 0x8;

    public final Map<String, AbstractPersister<?>> persistersMap;

    private final Context mContext;
    private final String contentAuthority;
    private final OnSyncFinishedListener syncFinishedListener;

    private SyncHelper(Context context, String contentAuthority,
                       Map<String, AbstractPersister<?>> persistersMap,
                       OnSyncFinishedListener syncFinishedListener) {
        if (context == null) {
            throw new IllegalStateException("Context should not be null");
        }
        if (contentAuthority == null) {
            throw new IllegalStateException("Content Authority should not be null");
        }
        this.mContext = context;
        this.contentAuthority = contentAuthority;
        this.persistersMap = persistersMap;
        this.syncFinishedListener = syncFinishedListener;
    }

    public void performSync(Bundle extras, int flags) throws RestClientException {
        ContentResolver contentResolver = mContext.getContentResolver();
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();


        String persisterKey = extras.getString(AbstractSyncUtils.PERSISTER_KEY);
        if (((flags & FLAG_SYNC_REMOTE) == FLAG_SYNC_REMOTE) && isOnline(mContext)) {
            persist(extras, batch, persisterKey);
        }
        if (((flags & FLAG_SYNC_CACHED) == FLAG_SYNC_CACHED)) {
            Bundle extra = new Bundle();
            extra.putAll(extras);
            extra.putBoolean(SYNC_EXTRAS_CACHED, true);

            persist(extra, batch, persisterKey);
        }

        try {
            // Apply all queued up batch operations for local data.
            if (!batch.isEmpty())
                contentResolver.applyBatch(contentAuthority, batch);
        } catch (RemoteException | OperationApplicationException e) {
            throw new RuntimeException("Problem applying batch operation", e);
        }

        if (syncFinishedListener != null && !batch.isEmpty()) {
            syncFinishedListener.onFinish(mContext);
        }
    }

    private void persist(Bundle extras, ArrayList<ContentProviderOperation> batch, String persisterKey) {
        if (persisterKey != null) {
            AbstractPersister<?> persister = persistersMap.get(persisterKey);
            if (persister != null) {
                batch.addAll(persister.fetchAndPersist(extras));
            }
        } else {
            for (Map.Entry<String, AbstractPersister<?>> entry : persistersMap.entrySet()) {
                batch.addAll(entry.getValue().fetchAndPersist(extras));
            }
        }
    }

    public static Builder newSyncBuilder(Context context, String authority, SyncResult syncResult) {
        return new Builder().setContext(context)
                .setContentAuthority(authority)
                .setSyncResult(syncResult);
    }

    public static class Builder {
        public final Map<String, AbstractPersister<?>> persistersMap = new HashMap<>();
        private Context mContext;
        private String contentAuthority;
        private SyncResult mSyncResult;
        private OnSyncFinishedListener syncFinishedListener;

        private Builder() {
        }

        public Builder registerPersister(String table, Class<?> clazz) {
            try {
                Constructor<?> constructor = clazz.getConstructor(Context.class, SyncResult.class);
                persistersMap.put(table, (AbstractPersister<?>) constructor.newInstance(mContext, mSyncResult));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        private Builder setContext(Context context) {
            mContext = context;
            return this;
        }

        private Builder setSyncResult(SyncResult syncResult) {
            mSyncResult = syncResult;
            return this;
        }

        private Builder setContentAuthority(String contentAuthority) {
            this.contentAuthority = contentAuthority;
            return this;
        }

        public Builder setSyncFinishListener(OnSyncFinishedListener syncFinishedListener) {
            this.syncFinishedListener = syncFinishedListener;
            return this;
        }

        public SyncHelper build() {
            return new SyncHelper(mContext, contentAuthority,
                    persistersMap, syncFinishedListener);
        }
    }

    public interface OnSyncFinishedListener {
        void onFinish(Context context);
    }
}