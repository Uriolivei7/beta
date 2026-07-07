package com.facebook.react.bridge;

import android.app.Activity;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public interface ActivityEventListener {
    void onActivityResult(Activity activity, int i3, int i4, Intent intent);

    void onNewIntent(Intent intent);

    default void onUserLeaveHint(Activity activity) {
    }
}
