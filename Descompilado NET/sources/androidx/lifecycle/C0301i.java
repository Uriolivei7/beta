package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: renamed from: androidx.lifecycle.i, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0301i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0301i f5328a = new C0301i();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final AtomicBoolean f5329b = new AtomicBoolean(false);

    /* JADX INFO: renamed from: androidx.lifecycle.i$a */
    public static final class a extends C0296d {
        @Override // androidx.lifecycle.C0296d, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
            D2.h.f(activity, "activity");
            u.f5358c.c(activity);
        }
    }

    private C0301i() {
    }

    public static final void a(Context context) {
        D2.h.f(context, "context");
        if (f5329b.getAndSet(true)) {
            return;
        }
        Context applicationContext = context.getApplicationContext();
        D2.h.d(applicationContext, "null cannot be cast to non-null type android.app.Application");
        ((Application) applicationContext).registerActivityLifecycleCallbacks(new a());
    }
}
