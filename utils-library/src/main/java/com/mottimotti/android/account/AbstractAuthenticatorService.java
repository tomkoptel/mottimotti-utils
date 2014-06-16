package com.mottimotti.android.account;

import android.accounts.AbstractAccountAuthenticator;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class AbstractAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return getAuthenticator().getIBinder();
    }

    public abstract AbstractAccountAuthenticator getAuthenticator();
}
