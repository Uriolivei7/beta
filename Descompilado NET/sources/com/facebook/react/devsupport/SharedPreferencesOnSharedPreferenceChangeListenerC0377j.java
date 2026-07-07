package com.facebook.react.devsupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import g1.C0542a;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: com.facebook.react.devsupport.j, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class SharedPreferencesOnSharedPreferenceChangeListenerC0377j implements C1.a, SharedPreferences.OnSharedPreferenceChangeListener {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f6732e = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final b f6733a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final SharedPreferences f6734b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final H1.d f6735c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f6736d;

    /* JADX INFO: renamed from: com.facebook.react.devsupport.j$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.devsupport.j$b */
    public interface b {
        void a();
    }

    public SharedPreferencesOnSharedPreferenceChangeListenerC0377j(Context context, b bVar) {
        D2.h.f(context, "applicationContext");
        this.f6733a = bVar;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        D2.h.e(defaultSharedPreferences, "getDefaultSharedPreferences(...)");
        this.f6734b = defaultSharedPreferences;
        this.f6735c = new H1.d(context);
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        this.f6736d = C0542a.f9423b;
    }

    @Override // C1.a
    public void c(boolean z3) {
        this.f6734b.edit().putBoolean("fps_debug", z3).apply();
    }

    @Override // C1.a
    public void f(boolean z3) {
        this.f6734b.edit().putBoolean("hot_module_replacement", z3).apply();
    }

    @Override // C1.a
    public boolean g() {
        return this.f6734b.getBoolean("inspector_debug", false);
    }

    @Override // C1.a
    public H1.d h() {
        return this.f6735c;
    }

    @Override // C1.a
    public void i(boolean z3) {
        this.f6734b.edit().putBoolean("inspector_debug", z3).apply();
    }

    @Override // C1.a
    public boolean j() {
        return this.f6736d;
    }

    @Override // C1.a
    public void k(boolean z3) {
        this.f6734b.edit().putBoolean("js_dev_mode_debug", z3).apply();
    }

    @Override // C1.a
    public boolean l() {
        return this.f6734b.getBoolean("js_minify_debug", false);
    }

    @Override // C1.a
    public boolean m() {
        return this.f6734b.getBoolean("fps_debug", false);
    }

    @Override // C1.a
    public boolean n() {
        return this.f6734b.getBoolean("js_dev_mode_debug", true);
    }

    @Override // C1.a
    public boolean o() {
        return this.f6734b.getBoolean("hot_module_replacement", true);
    }

    @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        D2.h.f(sharedPreferences, "sharedPreferences");
        if (this.f6733a != null) {
            if (D2.h.b("fps_debug", str) || D2.h.b("js_dev_mode_debug", str) || D2.h.b("js_minify_debug", str)) {
                this.f6733a.a();
            }
        }
    }
}
