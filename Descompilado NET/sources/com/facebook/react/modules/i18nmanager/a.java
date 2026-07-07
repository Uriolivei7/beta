package com.facebook.react.modules.i18nmanager;

import D2.h;
import K2.o;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.text.m;
import java.util.Locale;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0108a f6977a = new C0108a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final a f6978b = new a();

    /* JADX INFO: renamed from: com.facebook.react.modules.i18nmanager.a$a, reason: collision with other inner class name */
    public static final class C0108a {
        public /* synthetic */ C0108a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final a a() {
            return a.f6978b;
        }

        private C0108a() {
        }
    }

    private a() {
    }

    private final boolean c(Context context) {
        return (context.getApplicationInfo().flags & 4194304) != 0;
    }

    public static final a f() {
        return f6977a.a();
    }

    private final boolean g() {
        return m.a(Locale.getDefault()) == 1;
    }

    private final boolean h(Context context, String str, boolean z3) {
        return context.getSharedPreferences("com.facebook.react.modules.i18nmanager.I18nUtil", 0).getBoolean(str, z3);
    }

    private final boolean j(Context context) {
        return h(context, "RCTI18nUtil_allowRTL", true);
    }

    private final boolean k(Context context) {
        return h(context, "RCTI18nUtil_forceRTL", false) || o.n(System.getProperty("FORCE_RTL_FOR_TESTING", "false"), "true", true);
    }

    private final void l(Context context, String str, boolean z3) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("com.facebook.react.modules.i18nmanager.I18nUtil", 0).edit();
        editorEdit.putBoolean(str, z3);
        editorEdit.apply();
    }

    public final void b(Context context, boolean z3) {
        h.f(context, "context");
        l(context, "RCTI18nUtil_allowRTL", z3);
    }

    public final boolean d(Context context) {
        h.f(context, "context");
        return h(context, "RCTI18nUtil_makeRTLFlipLeftAndRightStyles", true);
    }

    public final void e(Context context, boolean z3) {
        h.f(context, "context");
        l(context, "RCTI18nUtil_forceRTL", z3);
    }

    public final boolean i(Context context) {
        h.f(context, "context");
        return c(context) && (k(context) || (j(context) && g()));
    }

    public final void m(Context context, boolean z3) {
        h.f(context, "context");
        l(context, "RCTI18nUtil_makeRTLFlipLeftAndRightStyles", z3);
    }
}
