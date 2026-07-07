package androidx.activity;

import a.C0205a;
import a.InterfaceC0206b;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.window.OnBackInvokedDispatcher;
import androidx.core.view.A;
import androidx.core.view.C;
import androidx.core.view.InterfaceC0278z;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.B;
import androidx.lifecycle.E;
import androidx.lifecycle.G;
import androidx.lifecycle.H;
import androidx.lifecycle.I;
import androidx.lifecycle.InterfaceC0298f;
import androidx.lifecycle.InterfaceC0302j;
import androidx.lifecycle.J;
import androidx.lifecycle.u;
import androidx.lifecycle.y;
import androidx.savedstate.a;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import q.InterfaceC0643a;

/* JADX INFO: loaded from: classes.dex */
public class ComponentActivity extends androidx.core.app.f implements androidx.lifecycle.l, H, InterfaceC0298f, G.d, o, androidx.activity.result.f, androidx.core.content.c, androidx.core.content.d, androidx.core.app.j, androidx.core.app.k, InterfaceC0278z, l {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final C0205a f2966d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final A f2967e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final androidx.lifecycle.m f2968f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    final G.c f2969g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private G f2970h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private E.b f2971i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final OnBackPressedDispatcher f2972j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final f f2973k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    final k f2974l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f2975m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final AtomicInteger f2976n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final androidx.activity.result.e f2977o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final CopyOnWriteArrayList f2978p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final CopyOnWriteArrayList f2979q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final CopyOnWriteArrayList f2980r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final CopyOnWriteArrayList f2981s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final CopyOnWriteArrayList f2982t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private boolean f2983u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private boolean f2984v;

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ComponentActivity.super.onBackPressed();
            } catch (IllegalStateException e4) {
                if (!TextUtils.equals(e4.getMessage(), "Can not perform this action after onSaveInstanceState")) {
                    throw e4;
                }
            }
        }
    }

    class b extends androidx.activity.result.e {
        b() {
        }
    }

    static class c {
        static void a(View view) {
            view.cancelPendingInputEvents();
        }
    }

    static class d {
        static OnBackInvokedDispatcher a(Activity activity) {
            return activity.getOnBackInvokedDispatcher();
        }
    }

    static final class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        Object f2990a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        G f2991b;

        e() {
        }
    }

    private interface f extends Executor {
        void a(View view);
    }

    class g implements f, ViewTreeObserver.OnDrawListener, Runnable {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        Runnable f2993c;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final long f2992b = SystemClock.uptimeMillis() + 10000;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        boolean f2994d = false;

        g() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void c() {
            Runnable runnable = this.f2993c;
            if (runnable != null) {
                runnable.run();
                this.f2993c = null;
            }
        }

        @Override // androidx.activity.ComponentActivity.f
        public void a(View view) {
            if (this.f2994d) {
                return;
            }
            this.f2994d = true;
            view.getViewTreeObserver().addOnDrawListener(this);
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            this.f2993c = runnable;
            View decorView = ComponentActivity.this.getWindow().getDecorView();
            if (!this.f2994d) {
                decorView.postOnAnimation(new Runnable() { // from class: androidx.activity.f
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f3020b.c();
                    }
                });
            } else if (Looper.myLooper() == Looper.getMainLooper()) {
                decorView.invalidate();
            } else {
                decorView.postInvalidate();
            }
        }

        @Override // android.view.ViewTreeObserver.OnDrawListener
        public void onDraw() {
            Runnable runnable = this.f2993c;
            if (runnable == null) {
                if (SystemClock.uptimeMillis() > this.f2992b) {
                    this.f2994d = false;
                    ComponentActivity.this.getWindow().getDecorView().post(this);
                    return;
                }
                return;
            }
            runnable.run();
            this.f2993c = null;
            if (ComponentActivity.this.f2974l.c()) {
                this.f2994d = false;
                ComponentActivity.this.getWindow().getDecorView().post(this);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            ComponentActivity.this.getWindow().getDecorView().getViewTreeObserver().removeOnDrawListener(this);
        }
    }

    public ComponentActivity() {
        this.f2966d = new C0205a();
        this.f2967e = new A(new Runnable() { // from class: androidx.activity.b
            @Override // java.lang.Runnable
            public final void run() {
                this.f3016b.J();
            }
        });
        this.f2968f = new androidx.lifecycle.m(this);
        G.c cVarA = G.c.a(this);
        this.f2969g = cVarA;
        this.f2972j = new OnBackPressedDispatcher(new a());
        f fVarG = G();
        this.f2973k = fVarG;
        this.f2974l = new k(fVarG, new C2.a() { // from class: androidx.activity.c
            @Override // C2.a
            public final Object a() {
                return this.f3017b.K();
            }
        });
        this.f2976n = new AtomicInteger();
        this.f2977o = new b();
        this.f2978p = new CopyOnWriteArrayList();
        this.f2979q = new CopyOnWriteArrayList();
        this.f2980r = new CopyOnWriteArrayList();
        this.f2981s = new CopyOnWriteArrayList();
        this.f2982t = new CopyOnWriteArrayList();
        this.f2983u = false;
        this.f2984v = false;
        if (t() == null) {
            throw new IllegalStateException("getLifecycle() returned null in ComponentActivity's constructor. Please make sure you are lazily constructing your Lifecycle in the first call to getLifecycle() rather than relying on field initialization.");
        }
        t().a(new InterfaceC0302j() { // from class: androidx.activity.ComponentActivity.3
            @Override // androidx.lifecycle.InterfaceC0302j
            public void d(androidx.lifecycle.l lVar, AbstractC0299g.a aVar) {
                if (aVar == AbstractC0299g.a.ON_STOP) {
                    Window window = ComponentActivity.this.getWindow();
                    View viewPeekDecorView = window != null ? window.peekDecorView() : null;
                    if (viewPeekDecorView != null) {
                        c.a(viewPeekDecorView);
                    }
                }
            }
        });
        t().a(new InterfaceC0302j() { // from class: androidx.activity.ComponentActivity.4
            @Override // androidx.lifecycle.InterfaceC0302j
            public void d(androidx.lifecycle.l lVar, AbstractC0299g.a aVar) {
                if (aVar == AbstractC0299g.a.ON_DESTROY) {
                    ComponentActivity.this.f2966d.b();
                    if (ComponentActivity.this.isChangingConfigurations()) {
                        return;
                    }
                    ComponentActivity.this.s().a();
                }
            }
        });
        t().a(new InterfaceC0302j() { // from class: androidx.activity.ComponentActivity.5
            @Override // androidx.lifecycle.InterfaceC0302j
            public void d(androidx.lifecycle.l lVar, AbstractC0299g.a aVar) {
                ComponentActivity.this.H();
                ComponentActivity.this.t().c(this);
            }
        });
        cVarA.c();
        y.c(this);
        b().h("android:support:activity-result", new a.c() { // from class: androidx.activity.d
            @Override // androidx.savedstate.a.c
            public final Bundle a() {
                return this.f3018a.L();
            }
        });
        E(new InterfaceC0206b() { // from class: androidx.activity.e
            @Override // a.InterfaceC0206b
            public final void a(Context context) {
                this.f3019a.M(context);
            }
        });
    }

    private f G() {
        return new g();
    }

    private void I() {
        I.a(getWindow().getDecorView(), this);
        J.a(getWindow().getDecorView(), this);
        G.e.a(getWindow().getDecorView(), this);
        r.a(getWindow().getDecorView(), this);
        q.a(getWindow().getDecorView(), this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ r2.r K() {
        reportFullyDrawn();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Bundle L() {
        Bundle bundle = new Bundle();
        this.f2977o.f(bundle);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void M(Context context) {
        Bundle bundleB = b().b("android:support:activity-result");
        if (bundleB != null) {
            this.f2977o.e(bundleB);
        }
    }

    public final void E(InterfaceC0206b interfaceC0206b) {
        this.f2966d.a(interfaceC0206b);
    }

    public final void F(InterfaceC0643a interfaceC0643a) {
        this.f2980r.add(interfaceC0643a);
    }

    void H() {
        if (this.f2970h == null) {
            e eVar = (e) getLastNonConfigurationInstance();
            if (eVar != null) {
                this.f2970h = eVar.f2991b;
            }
            if (this.f2970h == null) {
                this.f2970h = new G();
            }
        }
    }

    public void J() {
        invalidateOptionsMenu();
    }

    public Object N() {
        return null;
    }

    @Override // androidx.activity.o
    public final OnBackPressedDispatcher a() {
        return this.f2972j;
    }

    @Override // android.app.Activity
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        I();
        this.f2973k.a(getWindow().getDecorView());
        super.addContentView(view, layoutParams);
    }

    @Override // G.d
    public final androidx.savedstate.a b() {
        return this.f2969g.b();
    }

    @Override // androidx.core.view.InterfaceC0278z
    public void d(C c4) {
        this.f2967e.f(c4);
    }

    @Override // androidx.core.app.j
    public final void g(InterfaceC0643a interfaceC0643a) {
        this.f2981s.remove(interfaceC0643a);
    }

    @Override // androidx.core.content.c
    public final void i(InterfaceC0643a interfaceC0643a) {
        this.f2978p.remove(interfaceC0643a);
    }

    @Override // androidx.lifecycle.InterfaceC0298f
    public E.b j() {
        if (this.f2971i == null) {
            this.f2971i = new B(getApplication(), this, getIntent() != null ? getIntent().getExtras() : null);
        }
        return this.f2971i;
    }

    @Override // androidx.lifecycle.InterfaceC0298f
    public F.a k() {
        F.d dVar = new F.d();
        if (getApplication() != null) {
            dVar.c(E.a.f5272h, getApplication());
        }
        dVar.c(y.f5368a, this);
        dVar.c(y.f5369b, this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            dVar.c(y.f5370c, getIntent().getExtras());
        }
        return dVar;
    }

    @Override // androidx.core.app.k
    public final void l(InterfaceC0643a interfaceC0643a) {
        this.f2982t.add(interfaceC0643a);
    }

    @Override // androidx.core.view.InterfaceC0278z
    public void n(C c4) {
        this.f2967e.a(c4);
    }

    @Override // androidx.activity.result.f
    public final androidx.activity.result.e o() {
        return this.f2977o;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i3, int i4, Intent intent) {
        if (this.f2977o.b(i3, i4, intent)) {
            return;
        }
        super.onActivityResult(i3, i4, intent);
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        this.f2972j.e();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Iterator it = this.f2978p.iterator();
        while (it.hasNext()) {
            ((InterfaceC0643a) it.next()).a(configuration);
        }
    }

    @Override // androidx.core.app.f, android.app.Activity
    protected void onCreate(Bundle bundle) {
        this.f2969g.d(bundle);
        this.f2966d.c(this);
        super.onCreate(bundle);
        u.e(this);
        if (androidx.core.os.a.b()) {
            this.f2972j.f(d.a(this));
        }
        int i3 = this.f2975m;
        if (i3 != 0) {
            setContentView(i3);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onCreatePanelMenu(int i3, Menu menu) {
        if (i3 != 0) {
            return true;
        }
        super.onCreatePanelMenu(i3, menu);
        this.f2967e.b(menu, getMenuInflater());
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onMenuItemSelected(int i3, MenuItem menuItem) {
        if (super.onMenuItemSelected(i3, menuItem)) {
            return true;
        }
        if (i3 == 0) {
            return this.f2967e.d(menuItem);
        }
        return false;
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z3) {
        if (this.f2983u) {
            return;
        }
        Iterator it = this.f2981s.iterator();
        while (it.hasNext()) {
            ((InterfaceC0643a) it.next()).a(new androidx.core.app.g(z3));
        }
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Iterator it = this.f2980r.iterator();
        while (it.hasNext()) {
            ((InterfaceC0643a) it.next()).a(intent);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onPanelClosed(int i3, Menu menu) {
        this.f2967e.c(menu);
        super.onPanelClosed(i3, menu);
    }

    @Override // android.app.Activity
    public void onPictureInPictureModeChanged(boolean z3) {
        if (this.f2984v) {
            return;
        }
        Iterator it = this.f2982t.iterator();
        while (it.hasNext()) {
            ((InterfaceC0643a) it.next()).a(new androidx.core.app.l(z3));
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onPreparePanel(int i3, View view, Menu menu) {
        if (i3 != 0) {
            return true;
        }
        super.onPreparePanel(i3, view, menu);
        this.f2967e.e(menu);
        return true;
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i3, String[] strArr, int[] iArr) {
        if (this.f2977o.b(i3, -1, new Intent().putExtra("androidx.activity.result.contract.extra.PERMISSIONS", strArr).putExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS", iArr))) {
            return;
        }
        super.onRequestPermissionsResult(i3, strArr, iArr);
    }

    @Override // android.app.Activity
    public final Object onRetainNonConfigurationInstance() {
        e eVar;
        Object objN = N();
        G g3 = this.f2970h;
        if (g3 == null && (eVar = (e) getLastNonConfigurationInstance()) != null) {
            g3 = eVar.f2991b;
        }
        if (g3 == null && objN == null) {
            return null;
        }
        e eVar2 = new e();
        eVar2.f2990a = objN;
        eVar2.f2991b = g3;
        return eVar2;
    }

    @Override // androidx.core.app.f, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        AbstractC0299g abstractC0299gT = t();
        if (abstractC0299gT instanceof androidx.lifecycle.m) {
            ((androidx.lifecycle.m) abstractC0299gT).m(AbstractC0299g.b.CREATED);
        }
        super.onSaveInstanceState(bundle);
        this.f2969g.e(bundle);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks2
    public void onTrimMemory(int i3) {
        super.onTrimMemory(i3);
        Iterator it = this.f2979q.iterator();
        while (it.hasNext()) {
            ((InterfaceC0643a) it.next()).a(Integer.valueOf(i3));
        }
    }

    @Override // androidx.core.app.j
    public final void q(InterfaceC0643a interfaceC0643a) {
        this.f2981s.add(interfaceC0643a);
    }

    @Override // androidx.core.content.c
    public final void r(InterfaceC0643a interfaceC0643a) {
        this.f2978p.add(interfaceC0643a);
    }

    @Override // android.app.Activity
    public void reportFullyDrawn() {
        try {
            if (J.a.h()) {
                J.a.c("reportFullyDrawn() for ComponentActivity");
            }
            super.reportFullyDrawn();
            this.f2974l.b();
            J.a.f();
        } catch (Throwable th) {
            J.a.f();
            throw th;
        }
    }

    @Override // androidx.lifecycle.H
    public G s() {
        if (getApplication() == null) {
            throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
        }
        H();
        return this.f2970h;
    }

    @Override // android.app.Activity
    public void setContentView(int i3) {
        I();
        this.f2973k.a(getWindow().getDecorView());
        super.setContentView(i3);
    }

    @Override // android.app.Activity
    public void startActivityForResult(Intent intent, int i3) {
        super.startActivityForResult(intent, i3);
    }

    @Override // android.app.Activity
    public void startIntentSenderForResult(IntentSender intentSender, int i3, Intent intent, int i4, int i5, int i6) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intentSender, i3, intent, i4, i5, i6);
    }

    @Override // androidx.core.app.f, androidx.lifecycle.l
    public AbstractC0299g t() {
        return this.f2968f;
    }

    @Override // androidx.core.app.k
    public final void u(InterfaceC0643a interfaceC0643a) {
        this.f2982t.remove(interfaceC0643a);
    }

    @Override // androidx.core.content.d
    public final void v(InterfaceC0643a interfaceC0643a) {
        this.f2979q.remove(interfaceC0643a);
    }

    @Override // androidx.core.content.d
    public final void x(InterfaceC0643a interfaceC0643a) {
        this.f2979q.add(interfaceC0643a);
    }

    @Override // android.app.Activity
    public void startActivityForResult(Intent intent, int i3, Bundle bundle) {
        super.startActivityForResult(intent, i3, bundle);
    }

    @Override // android.app.Activity
    public void startIntentSenderForResult(IntentSender intentSender, int i3, Intent intent, int i4, int i5, int i6, Bundle bundle) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intentSender, i3, intent, i4, i5, i6, bundle);
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z3, Configuration configuration) {
        this.f2983u = true;
        try {
            super.onMultiWindowModeChanged(z3, configuration);
            this.f2983u = false;
            Iterator it = this.f2981s.iterator();
            while (it.hasNext()) {
                ((InterfaceC0643a) it.next()).a(new androidx.core.app.g(z3, configuration));
            }
        } catch (Throwable th) {
            this.f2983u = false;
            throw th;
        }
    }

    @Override // android.app.Activity
    public void onPictureInPictureModeChanged(boolean z3, Configuration configuration) {
        this.f2984v = true;
        try {
            super.onPictureInPictureModeChanged(z3, configuration);
            this.f2984v = false;
            Iterator it = this.f2982t.iterator();
            while (it.hasNext()) {
                ((InterfaceC0643a) it.next()).a(new androidx.core.app.l(z3, configuration));
            }
        } catch (Throwable th) {
            this.f2984v = false;
            throw th;
        }
    }

    @Override // android.app.Activity
    public void setContentView(View view) {
        I();
        this.f2973k.a(getWindow().getDecorView());
        super.setContentView(view);
    }

    @Override // android.app.Activity
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        I();
        this.f2973k.a(getWindow().getDecorView());
        super.setContentView(view, layoutParams);
    }

    public ComponentActivity(int i3) {
        this();
        this.f2975m = i3;
    }
}
