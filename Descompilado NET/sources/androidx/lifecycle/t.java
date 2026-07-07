package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.u;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class t implements l {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final b f5346j = new b(null);

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final t f5347k = new t();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f5348b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f5349c;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Handler f5352f;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f5350d = true;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f5351e = true;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final m f5353g = new m(this);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Runnable f5354h = new Runnable() { // from class: androidx.lifecycle.s
        @Override // java.lang.Runnable
        public final void run() {
            t.k(this.f5345b);
        }
    };

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final u.a f5355i = new d();

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final a f5356a = new a();

        private a() {
        }

        public static final void a(Activity activity, Application.ActivityLifecycleCallbacks activityLifecycleCallbacks) {
            D2.h.f(activity, "activity");
            D2.h.f(activityLifecycleCallbacks, "callback");
            activity.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
    }

    public static final class b {
        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final l a() {
            return t.f5347k;
        }

        public final void b(Context context) {
            D2.h.f(context, "context");
            t.f5347k.j(context);
        }

        private b() {
        }
    }

    public static final class c extends C0296d {

        public static final class a extends C0296d {
            final /* synthetic */ t this$0;

            a(t tVar) {
                this.this$0 = tVar;
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPostResumed(Activity activity) {
                D2.h.f(activity, "activity");
                this.this$0.g();
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPostStarted(Activity activity) {
                D2.h.f(activity, "activity");
                this.this$0.h();
            }
        }

        c() {
        }

        @Override // androidx.lifecycle.C0296d, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
            D2.h.f(activity, "activity");
            if (Build.VERSION.SDK_INT < 29) {
                u.f5358c.b(activity).f(t.this.f5355i);
            }
        }

        @Override // androidx.lifecycle.C0296d, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
            D2.h.f(activity, "activity");
            t.this.f();
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPreCreated(Activity activity, Bundle bundle) {
            D2.h.f(activity, "activity");
            a.a(activity, new a(t.this));
        }

        @Override // androidx.lifecycle.C0296d, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
            D2.h.f(activity, "activity");
            t.this.i();
        }
    }

    public static final class d implements u.a {
        d() {
        }

        @Override // androidx.lifecycle.u.a
        public void a() {
            t.this.g();
        }

        @Override // androidx.lifecycle.u.a
        public void b() {
        }

        @Override // androidx.lifecycle.u.a
        public void c() {
            t.this.h();
        }
    }

    private t() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void k(t tVar) {
        D2.h.f(tVar, "this$0");
        tVar.l();
        tVar.m();
    }

    public final void f() {
        int i3 = this.f5349c - 1;
        this.f5349c = i3;
        if (i3 == 0) {
            Handler handler = this.f5352f;
            D2.h.c(handler);
            handler.postDelayed(this.f5354h, 700L);
        }
    }

    public final void g() {
        int i3 = this.f5349c + 1;
        this.f5349c = i3;
        if (i3 == 1) {
            if (this.f5350d) {
                this.f5353g.h(AbstractC0299g.a.ON_RESUME);
                this.f5350d = false;
            } else {
                Handler handler = this.f5352f;
                D2.h.c(handler);
                handler.removeCallbacks(this.f5354h);
            }
        }
    }

    public final void h() {
        int i3 = this.f5348b + 1;
        this.f5348b = i3;
        if (i3 == 1 && this.f5351e) {
            this.f5353g.h(AbstractC0299g.a.ON_START);
            this.f5351e = false;
        }
    }

    public final void i() {
        this.f5348b--;
        m();
    }

    public final void j(Context context) {
        D2.h.f(context, "context");
        this.f5352f = new Handler();
        this.f5353g.h(AbstractC0299g.a.ON_CREATE);
        Context applicationContext = context.getApplicationContext();
        D2.h.d(applicationContext, "null cannot be cast to non-null type android.app.Application");
        ((Application) applicationContext).registerActivityLifecycleCallbacks(new c());
    }

    public final void l() {
        if (this.f5349c == 0) {
            this.f5350d = true;
            this.f5353g.h(AbstractC0299g.a.ON_PAUSE);
        }
    }

    public final void m() {
        if (this.f5348b == 0 && this.f5350d) {
            this.f5353g.h(AbstractC0299g.a.ON_STOP);
            this.f5351e = true;
        }
    }

    @Override // androidx.lifecycle.l
    public AbstractC0299g t() {
        return this.f5353g;
    }
}
