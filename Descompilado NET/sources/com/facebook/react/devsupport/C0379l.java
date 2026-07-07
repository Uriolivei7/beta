package com.facebook.react.devsupport;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import d1.AbstractC0508p;

/* JADX INFO: renamed from: com.facebook.react.devsupport.l, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0379l extends PreferenceActivity {
    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle(getApplication().getResources().getString(AbstractC0508p.f9292s));
        addPreferencesFromResource(d1.r.f9307a);
    }
}
