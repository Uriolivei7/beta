package com.facebook.react.modules.network;

import M2.C0192c;
import M2.z;
import android.content.Context;
import java.io.File;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final g f7011a = new g();

    private g() {
    }

    public static final z a() {
        return c().b();
    }

    public static final z b(Context context) {
        D2.h.f(context, "context");
        return d(context).b();
    }

    public static final z.a c() {
        z.a aVar = new z.a();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        return aVar.e(0L, timeUnit).M(0L, timeUnit).N(0L, timeUnit).f(new m());
    }

    public static final z.a d(Context context) {
        D2.h.f(context, "context");
        return e(context, 10485760);
    }

    public static final z.a e(Context context, int i3) {
        D2.h.f(context, "context");
        z.a aVarC = c();
        return i3 == 0 ? aVarC : aVarC.c(new C0192c(new File(context.getCacheDir(), "http-cache"), i3));
    }
}
