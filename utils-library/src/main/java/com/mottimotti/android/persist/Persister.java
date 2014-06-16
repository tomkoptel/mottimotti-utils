package com.mottimotti.android.persist;

import android.content.ContentProviderOperation;
import android.os.Bundle;

import java.util.ArrayList;

public interface Persister<T> {
    T fetch(Bundle bundle);
    ArrayList<ContentProviderOperation> persist(T response);
}
