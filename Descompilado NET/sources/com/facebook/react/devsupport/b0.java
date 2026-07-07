package com.facebook.react.devsupport;

import android.content.Context;
import com.facebook.react.devsupport.SharedPreferencesOnSharedPreferenceChangeListenerC0377j;

/* JADX INFO: loaded from: classes.dex */
public final class b0 extends k0 {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C1.a f6687b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0378k f6688c;

    public static final class a implements SharedPreferencesOnSharedPreferenceChangeListenerC0377j.b {
        a() {
        }

        @Override // com.facebook.react.devsupport.SharedPreferencesOnSharedPreferenceChangeListenerC0377j.b
        public void a() {
        }
    }

    public b0(Context context) {
        D2.h.f(context, "applicationContext");
        this.f6687b = new SharedPreferencesOnSharedPreferenceChangeListenerC0377j(context, new a());
        this.f6688c = new C0378k(o(), context, o().h());
    }

    @Override // com.facebook.react.devsupport.k0, k1.e
    public void m() {
        this.f6688c.i();
    }

    @Override // com.facebook.react.devsupport.k0, k1.e
    public C1.a o() {
        return this.f6687b;
    }

    @Override // com.facebook.react.devsupport.k0, k1.e
    public void u() {
        this.f6688c.y();
    }
}
