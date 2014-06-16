package com.mottimotti.android.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.os.Bundle;


public abstract class AbstractSyncUtils implements SyncUtility {
    public static final String PERSISTER_KEY = "PERSISTER_KEY";


    /**
     * Create an entry for this application in the system account list, if it isn't already there.
     */
    @Override
    public void createSyncAccount(Account account) {
        boolean setupComplete = getAccountUtility().isSetupComplete();
        String contentAuthority = getAccountUtility().getContentAuthority();

        // Inform the system that this account supports sync
        ContentResolver.setIsSyncable(account, contentAuthority, 1);
        // Inform the system that this account is eligible for auto sync when the network is up
        ContentResolver.setSyncAutomatically(account, contentAuthority, true);
        // Recommend a schedule for automatic synchronization. The system may modify this based
        // on other scheduled syncs and network utilization.
        ContentResolver.addPeriodicSync(
                account, contentAuthority, new Bundle(), getSyncFrequency());

        // Schedule an initial sync if we detect problems with either our account or our local
        // data has been deleted. (Note that it's possible to clear app data WITHOUT affecting
        // the account list, so wee need to check both.)
        if (!setupComplete) {
            getAccountUtility().setSetupComplete(true);
            triggerRefresh();
        }
    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     * <p/>
     * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
     * means the user has pressed the "refresh" button.
     * <p/>
     * Note that SYNC_EXTRAS_MANUAL will cause an immediate sync, without any optimization to
     * preserve battery life. If you know new data is available (perhaps via a GCM notification),
     * but the user is not actively waiting for that data, you should omit this flag; this will give
     * the OS additional freedom in scheduling your sync request.
     *
     * @param persisterKey Describes Table we need to manually sync.
     * @param bundle Extra keys for persister.
     */
    @Override
    public void triggerRefresh(String persisterKey, Bundle bundle) {
        Bundle data = new Bundle();
        if (bundle != null) {
            data.putAll(bundle);
        }
        if (persisterKey != null) {
            data.putString(PERSISTER_KEY, persisterKey);
        }
        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
        data.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        data.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(
                getAccountUtility().getChosenAccount(), // Sync account
                getAccountUtility().getContentAuthority(), // Content authority
                data); // Extras
    }

    public void triggerRefresh(String persisterKey) {
        triggerRefresh(persisterKey, null);
    }

    /**
     * {@code triggerRefresh} defaults to {@link AbstractSyncUtils#triggerRefresh}.
     */
    public void triggerRefresh() {
        triggerRefresh(null);
    }

    @Override
    public void triggerUpload() {
        Bundle data = new Bundle();
        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
        data.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        data.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(
                getAccountUtility().getChosenAccount(), // Sync account
                getAccountUtility().getContentAuthority(), // Content authority
                data); // Extras
    }

    @Override
    public void cancelSync() {
        ContentResolver.cancelSync(
                getAccountUtility().getChosenAccount(),
                getAccountUtility().getContentAuthority()
         );
    }

    public static boolean isEqual(String in, String out) {
        if (in == null && out != null) {
            return false;
        }
        return in != null && in.equals(out);
    }
}