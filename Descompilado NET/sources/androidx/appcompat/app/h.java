package androidx.appcompat.app;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.appcompat.view.b;
import androidx.appcompat.view.f;
import androidx.appcompat.view.menu.e;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.C0222k;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.I;
import androidx.appcompat.widget.ViewStubCompat;
import androidx.appcompat.widget.h0;
import androidx.appcompat.widget.r0;
import androidx.appcompat.widget.s0;
import androidx.core.content.res.f;
import androidx.core.view.AbstractC0276x;
import androidx.core.view.AbstractC0277y;
import androidx.core.view.C0254i0;
import androidx.core.view.C0258k0;
import androidx.core.view.C0264n0;
import androidx.core.view.Z;
import androidx.lifecycle.AbstractC0299g;
import d.AbstractC0487a;
import e.AbstractC0521a;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: loaded from: classes.dex */
class h extends androidx.appcompat.app.f implements e.a, LayoutInflater.Factory2 {

    /* JADX INFO: renamed from: k0, reason: collision with root package name */
    private static final l.g f3185k0 = new l.g();

    /* JADX INFO: renamed from: l0, reason: collision with root package name */
    private static final boolean f3186l0 = false;

    /* JADX INFO: renamed from: m0, reason: collision with root package name */
    private static final int[] f3187m0 = {R.attr.windowBackground};

    /* JADX INFO: renamed from: n0, reason: collision with root package name */
    private static final boolean f3188n0 = !"robolectric".equals(Build.FINGERPRINT);

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private boolean f3189A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private boolean f3190B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    ViewGroup f3191C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private TextView f3192D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private View f3193E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private boolean f3194F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private boolean f3195G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    boolean f3196H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    boolean f3197I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    boolean f3198J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    boolean f3199K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    boolean f3200L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private boolean f3201M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private q[] f3202N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private q f3203O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private boolean f3204P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private boolean f3205Q;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private boolean f3206R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    boolean f3207S;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    private Configuration f3208T;

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    private int f3209U;

    /* JADX INFO: renamed from: V, reason: collision with root package name */
    private int f3210V;

    /* JADX INFO: renamed from: W, reason: collision with root package name */
    private int f3211W;

    /* JADX INFO: renamed from: X, reason: collision with root package name */
    private boolean f3212X;

    /* JADX INFO: renamed from: Y, reason: collision with root package name */
    private n f3213Y;

    /* JADX INFO: renamed from: Z, reason: collision with root package name */
    private n f3214Z;

    /* JADX INFO: renamed from: a0, reason: collision with root package name */
    boolean f3215a0;

    /* JADX INFO: renamed from: b0, reason: collision with root package name */
    int f3216b0;

    /* JADX INFO: renamed from: c0, reason: collision with root package name */
    private final Runnable f3217c0;

    /* JADX INFO: renamed from: d0, reason: collision with root package name */
    private boolean f3218d0;

    /* JADX INFO: renamed from: e0, reason: collision with root package name */
    private Rect f3219e0;

    /* JADX INFO: renamed from: f0, reason: collision with root package name */
    private Rect f3220f0;

    /* JADX INFO: renamed from: g0, reason: collision with root package name */
    private s f3221g0;

    /* JADX INFO: renamed from: h0, reason: collision with root package name */
    private u f3222h0;

    /* JADX INFO: renamed from: i0, reason: collision with root package name */
    private OnBackInvokedDispatcher f3223i0;

    /* JADX INFO: renamed from: j0, reason: collision with root package name */
    private OnBackInvokedCallback f3224j0;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    final Object f3225k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    final Context f3226l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    Window f3227m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private l f3228n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    final androidx.appcompat.app.d f3229o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    androidx.appcompat.app.a f3230p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    MenuInflater f3231q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private CharSequence f3232r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private I f3233s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private f f3234t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private r f3235u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    androidx.appcompat.view.b f3236v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    ActionBarContextView f3237w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    PopupWindow f3238x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    Runnable f3239y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    C0254i0 f3240z;

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            h hVar = h.this;
            if ((hVar.f3216b0 & 1) != 0) {
                hVar.i0(0);
            }
            h hVar2 = h.this;
            if ((hVar2.f3216b0 & 4096) != 0) {
                hVar2.i0(108);
            }
            h hVar3 = h.this;
            hVar3.f3215a0 = false;
            hVar3.f3216b0 = 0;
        }
    }

    class b implements androidx.core.view.I {
        b() {
        }

        @Override // androidx.core.view.I
        public C0264n0 a(View view, C0264n0 c0264n0) {
            int iK = c0264n0.k();
            int iF1 = h.this.f1(c0264n0, null);
            if (iK != iF1) {
                c0264n0 = c0264n0.p(c0264n0.i(), iF1, c0264n0.j(), c0264n0.h());
            }
            return Z.M(view, c0264n0);
        }
    }

    class c implements ContentFrameLayout.a {
        c() {
        }

        @Override // androidx.appcompat.widget.ContentFrameLayout.a
        public void a() {
        }

        @Override // androidx.appcompat.widget.ContentFrameLayout.a
        public void onDetachedFromWindow() {
            h.this.g0();
        }
    }

    class d implements Runnable {

        class a extends C0258k0 {
            a() {
            }

            @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
            public void b(View view) {
                h.this.f3237w.setAlpha(1.0f);
                h.this.f3240z.h(null);
                h.this.f3240z = null;
            }

            @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
            public void c(View view) {
                h.this.f3237w.setVisibility(0);
            }
        }

        d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            h hVar = h.this;
            hVar.f3238x.showAtLocation(hVar.f3237w, 55, 0, 0);
            h.this.j0();
            if (!h.this.U0()) {
                h.this.f3237w.setAlpha(1.0f);
                h.this.f3237w.setVisibility(0);
            } else {
                h.this.f3237w.setAlpha(0.0f);
                h hVar2 = h.this;
                hVar2.f3240z = Z.c(hVar2.f3237w).b(1.0f);
                h.this.f3240z.h(new a());
            }
        }
    }

    class e extends C0258k0 {
        e() {
        }

        @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
        public void b(View view) {
            h.this.f3237w.setAlpha(1.0f);
            h.this.f3240z.h(null);
            h.this.f3240z = null;
        }

        @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
        public void c(View view) {
            h.this.f3237w.setVisibility(0);
            if (h.this.f3237w.getParent() instanceof View) {
                Z.U((View) h.this.f3237w.getParent());
            }
        }
    }

    private final class f implements j.a {
        f() {
        }

        @Override // androidx.appcompat.view.menu.j.a
        public void c(androidx.appcompat.view.menu.e eVar, boolean z3) {
            h.this.Z(eVar);
        }

        @Override // androidx.appcompat.view.menu.j.a
        public boolean d(androidx.appcompat.view.menu.e eVar) {
            Window.Callback callbackV0 = h.this.v0();
            if (callbackV0 == null) {
                return true;
            }
            callbackV0.onMenuOpened(108, eVar);
            return true;
        }
    }

    class g implements b.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private b.a f3248a;

        class a extends C0258k0 {
            a() {
            }

            @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
            public void b(View view) {
                h.this.f3237w.setVisibility(8);
                h hVar = h.this;
                PopupWindow popupWindow = hVar.f3238x;
                if (popupWindow != null) {
                    popupWindow.dismiss();
                } else if (hVar.f3237w.getParent() instanceof View) {
                    Z.U((View) h.this.f3237w.getParent());
                }
                h.this.f3237w.k();
                h.this.f3240z.h(null);
                h hVar2 = h.this;
                hVar2.f3240z = null;
                Z.U(hVar2.f3191C);
            }
        }

        public g(b.a aVar) {
            this.f3248a = aVar;
        }

        @Override // androidx.appcompat.view.b.a
        public boolean a(androidx.appcompat.view.b bVar, Menu menu) {
            Z.U(h.this.f3191C);
            return this.f3248a.a(bVar, menu);
        }

        @Override // androidx.appcompat.view.b.a
        public void b(androidx.appcompat.view.b bVar) {
            this.f3248a.b(bVar);
            h hVar = h.this;
            if (hVar.f3238x != null) {
                hVar.f3227m.getDecorView().removeCallbacks(h.this.f3239y);
            }
            h hVar2 = h.this;
            if (hVar2.f3237w != null) {
                hVar2.j0();
                h hVar3 = h.this;
                hVar3.f3240z = Z.c(hVar3.f3237w).b(0.0f);
                h.this.f3240z.h(new a());
            }
            h hVar4 = h.this;
            androidx.appcompat.app.d dVar = hVar4.f3229o;
            if (dVar != null) {
                dVar.h(hVar4.f3236v);
            }
            h hVar5 = h.this;
            hVar5.f3236v = null;
            Z.U(hVar5.f3191C);
            h.this.d1();
        }

        @Override // androidx.appcompat.view.b.a
        public boolean c(androidx.appcompat.view.b bVar, MenuItem menuItem) {
            return this.f3248a.c(bVar, menuItem);
        }

        @Override // androidx.appcompat.view.b.a
        public boolean d(androidx.appcompat.view.b bVar, Menu menu) {
            return this.f3248a.d(bVar, menu);
        }
    }

    /* JADX INFO: renamed from: androidx.appcompat.app.h$h, reason: collision with other inner class name */
    static class C0052h {
        static boolean a(PowerManager powerManager) {
            return powerManager.isPowerSaveMode();
        }

        static String b(Locale locale) {
            return locale.toLanguageTag();
        }
    }

    static class i {
        static void a(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            LocaleList locales = configuration.getLocales();
            LocaleList locales2 = configuration2.getLocales();
            if (locales.equals(locales2)) {
                return;
            }
            configuration3.setLocales(locales2);
            configuration3.locale = configuration2.locale;
        }

        static androidx.core.os.e b(Configuration configuration) {
            return androidx.core.os.e.b(configuration.getLocales().toLanguageTags());
        }

        public static void c(androidx.core.os.e eVar) {
            LocaleList.setDefault(LocaleList.forLanguageTags(eVar.g()));
        }

        static void d(Configuration configuration, androidx.core.os.e eVar) {
            configuration.setLocales(LocaleList.forLanguageTags(eVar.g()));
        }
    }

    static class j {
        static void a(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            if ((configuration.colorMode & 3) != (configuration2.colorMode & 3)) {
                configuration3.colorMode |= configuration2.colorMode & 3;
            }
            if ((configuration.colorMode & 12) != (configuration2.colorMode & 12)) {
                configuration3.colorMode |= configuration2.colorMode & 12;
            }
        }
    }

    static class k {
        static OnBackInvokedDispatcher a(Activity activity) {
            return activity.getOnBackInvokedDispatcher();
        }

        static OnBackInvokedCallback b(Object obj, final h hVar) {
            Objects.requireNonNull(hVar);
            OnBackInvokedCallback onBackInvokedCallback = new OnBackInvokedCallback() { // from class: androidx.appcompat.app.p
                @Override // android.window.OnBackInvokedCallback
                public final void onBackInvoked() {
                    hVar.D0();
                }
            };
            androidx.appcompat.app.l.a(obj).registerOnBackInvokedCallback(1000000, onBackInvokedCallback);
            return onBackInvokedCallback;
        }

        static void c(Object obj, Object obj2) {
            androidx.appcompat.app.l.a(obj).unregisterOnBackInvokedCallback(androidx.appcompat.app.k.a(obj2));
        }
    }

    class l extends androidx.appcompat.view.i {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f3251c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f3252d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f3253e;

        l(Window.Callback callback) {
            super(callback);
        }

        public boolean b(Window.Callback callback, KeyEvent keyEvent) {
            try {
                this.f3252d = true;
                return callback.dispatchKeyEvent(keyEvent);
            } finally {
                this.f3252d = false;
            }
        }

        public void c(Window.Callback callback) {
            try {
                this.f3251c = true;
                callback.onContentChanged();
            } finally {
                this.f3251c = false;
            }
        }

        public void d(Window.Callback callback, int i3, Menu menu) {
            try {
                this.f3253e = true;
                callback.onPanelClosed(i3, menu);
            } finally {
                this.f3253e = false;
            }
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return this.f3252d ? a().dispatchKeyEvent(keyEvent) : h.this.h0(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return super.dispatchKeyShortcutEvent(keyEvent) || h.this.G0(keyEvent.getKeyCode(), keyEvent);
        }

        final ActionMode e(ActionMode.Callback callback) {
            f.a aVar = new f.a(h.this.f3226l, callback);
            androidx.appcompat.view.b bVarX0 = h.this.X0(aVar);
            if (bVarX0 != null) {
                return aVar.e(bVarX0);
            }
            return null;
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public void onContentChanged() {
            if (this.f3251c) {
                a().onContentChanged();
            }
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean onCreatePanelMenu(int i3, Menu menu) {
            if (i3 != 0 || (menu instanceof androidx.appcompat.view.menu.e)) {
                return super.onCreatePanelMenu(i3, menu);
            }
            return false;
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public View onCreatePanelView(int i3) {
            return super.onCreatePanelView(i3);
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean onMenuOpened(int i3, Menu menu) {
            super.onMenuOpened(i3, menu);
            h.this.J0(i3);
            return true;
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public void onPanelClosed(int i3, Menu menu) {
            if (this.f3253e) {
                a().onPanelClosed(i3, menu);
            } else {
                super.onPanelClosed(i3, menu);
                h.this.K0(i3);
            }
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean onPreparePanel(int i3, View view, Menu menu) {
            androidx.appcompat.view.menu.e eVar = menu instanceof androidx.appcompat.view.menu.e ? (androidx.appcompat.view.menu.e) menu : null;
            if (i3 == 0 && eVar == null) {
                return false;
            }
            if (eVar != null) {
                eVar.b0(true);
            }
            boolean zOnPreparePanel = super.onPreparePanel(i3, view, menu);
            if (eVar != null) {
                eVar.b0(false);
            }
            return zOnPreparePanel;
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public void onProvideKeyboardShortcuts(List list, Menu menu, int i3) {
            androidx.appcompat.view.menu.e eVar;
            q qVarT0 = h.this.t0(0, true);
            if (qVarT0 == null || (eVar = qVarT0.f3272j) == null) {
                super.onProvideKeyboardShortcuts(list, menu, i3);
            } else {
                super.onProvideKeyboardShortcuts(list, eVar, i3);
            }
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return null;
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i3) {
            return (h.this.B0() && i3 == 0) ? e(callback) : super.onWindowStartingActionMode(callback, i3);
        }
    }

    private class m extends n {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final PowerManager f3255c;

        m(Context context) {
            super();
            this.f3255c = (PowerManager) context.getApplicationContext().getSystemService("power");
        }

        @Override // androidx.appcompat.app.h.n
        IntentFilter b() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
            return intentFilter;
        }

        @Override // androidx.appcompat.app.h.n
        public int c() {
            return C0052h.a(this.f3255c) ? 2 : 1;
        }

        @Override // androidx.appcompat.app.h.n
        public void d() {
            h.this.f();
        }
    }

    abstract class n {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private BroadcastReceiver f3257a;

        class a extends BroadcastReceiver {
            a() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                n.this.d();
            }
        }

        n() {
        }

        void a() {
            BroadcastReceiver broadcastReceiver = this.f3257a;
            if (broadcastReceiver != null) {
                try {
                    h.this.f3226l.unregisterReceiver(broadcastReceiver);
                } catch (IllegalArgumentException unused) {
                }
                this.f3257a = null;
            }
        }

        abstract IntentFilter b();

        abstract int c();

        abstract void d();

        void e() {
            a();
            IntentFilter intentFilterB = b();
            if (intentFilterB == null || intentFilterB.countActions() == 0) {
                return;
            }
            if (this.f3257a == null) {
                this.f3257a = new a();
            }
            h.this.f3226l.registerReceiver(this.f3257a, intentFilterB);
        }
    }

    private class o extends n {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final y f3260c;

        o(y yVar) {
            super();
            this.f3260c = yVar;
        }

        @Override // androidx.appcompat.app.h.n
        IntentFilter b() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.TIME_TICK");
            return intentFilter;
        }

        @Override // androidx.appcompat.app.h.n
        public int c() {
            return this.f3260c.d() ? 2 : 1;
        }

        @Override // androidx.appcompat.app.h.n
        public void d() {
            h.this.f();
        }
    }

    private class p extends ContentFrameLayout {
        public p(Context context) {
            super(context);
        }

        private boolean b(int i3, int i4) {
            return i3 < -5 || i4 < -5 || i3 > getWidth() + 5 || i4 > getHeight() + 5;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return h.this.h0(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0 || !b((int) motionEvent.getX(), (int) motionEvent.getY())) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            h.this.b0(0);
            return true;
        }

        @Override // android.view.View
        public void setBackgroundResource(int i3) {
            setBackgroundDrawable(AbstractC0521a.b(getContext(), i3));
        }
    }

    protected static final class q {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        int f3263a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        int f3264b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        int f3265c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        int f3266d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        int f3267e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f3268f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        ViewGroup f3269g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        View f3270h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        View f3271i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        androidx.appcompat.view.menu.e f3272j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        androidx.appcompat.view.menu.c f3273k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        Context f3274l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        boolean f3275m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        boolean f3276n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        boolean f3277o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        public boolean f3278p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        boolean f3279q = false;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        boolean f3280r;

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        Bundle f3281s;

        q(int i3) {
            this.f3263a = i3;
        }

        androidx.appcompat.view.menu.k a(j.a aVar) {
            if (this.f3272j == null) {
                return null;
            }
            if (this.f3273k == null) {
                androidx.appcompat.view.menu.c cVar = new androidx.appcompat.view.menu.c(this.f3274l, d.g.f8819j);
                this.f3273k = cVar;
                cVar.k(aVar);
                this.f3272j.b(this.f3273k);
            }
            return this.f3273k.b(this.f3269g);
        }

        public boolean b() {
            if (this.f3270h == null) {
                return false;
            }
            return this.f3271i != null || this.f3273k.a().getCount() > 0;
        }

        void c(androidx.appcompat.view.menu.e eVar) {
            androidx.appcompat.view.menu.c cVar;
            androidx.appcompat.view.menu.e eVar2 = this.f3272j;
            if (eVar == eVar2) {
                return;
            }
            if (eVar2 != null) {
                eVar2.P(this.f3273k);
            }
            this.f3272j = eVar;
            if (eVar == null || (cVar = this.f3273k) == null) {
                return;
            }
            eVar.b(cVar);
        }

        void d(Context context) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme themeNewTheme = context.getResources().newTheme();
            themeNewTheme.setTo(context.getTheme());
            themeNewTheme.resolveAttribute(AbstractC0487a.f8673a, typedValue, true);
            int i3 = typedValue.resourceId;
            if (i3 != 0) {
                themeNewTheme.applyStyle(i3, true);
            }
            themeNewTheme.resolveAttribute(AbstractC0487a.f8662G, typedValue, true);
            int i4 = typedValue.resourceId;
            if (i4 != 0) {
                themeNewTheme.applyStyle(i4, true);
            } else {
                themeNewTheme.applyStyle(d.i.f8845b, true);
            }
            androidx.appcompat.view.d dVar = new androidx.appcompat.view.d(context, 0);
            dVar.getTheme().setTo(themeNewTheme);
            this.f3274l = dVar;
            TypedArray typedArrayObtainStyledAttributes = dVar.obtainStyledAttributes(d.j.f9054y0);
            this.f3264b = typedArrayObtainStyledAttributes.getResourceId(d.j.f8853B0, 0);
            this.f3268f = typedArrayObtainStyledAttributes.getResourceId(d.j.f8849A0, 0);
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    private final class r implements j.a {
        r() {
        }

        @Override // androidx.appcompat.view.menu.j.a
        public void c(androidx.appcompat.view.menu.e eVar, boolean z3) {
            androidx.appcompat.view.menu.e eVarD = eVar.D();
            boolean z4 = eVarD != eVar;
            h hVar = h.this;
            if (z4) {
                eVar = eVarD;
            }
            q qVarM0 = hVar.m0(eVar);
            if (qVarM0 != null) {
                if (!z4) {
                    h.this.c0(qVarM0, z3);
                } else {
                    h.this.Y(qVarM0.f3263a, qVarM0, eVarD);
                    h.this.c0(qVarM0, true);
                }
            }
        }

        @Override // androidx.appcompat.view.menu.j.a
        public boolean d(androidx.appcompat.view.menu.e eVar) {
            Window.Callback callbackV0;
            if (eVar != eVar.D()) {
                return true;
            }
            h hVar = h.this;
            if (!hVar.f3196H || (callbackV0 = hVar.v0()) == null || h.this.f3207S) {
                return true;
            }
            callbackV0.onMenuOpened(108, eVar);
            return true;
        }
    }

    h(Activity activity, androidx.appcompat.app.d dVar) {
        this(activity, null, dVar, activity);
    }

    private void A0(int i3) {
        this.f3216b0 = (1 << i3) | this.f3216b0;
        if (this.f3215a0) {
            return;
        }
        Z.S(this.f3227m.getDecorView(), this.f3217c0);
        this.f3215a0 = true;
    }

    private boolean F0(int i3, KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() != 0) {
            return false;
        }
        q qVarT0 = t0(i3, true);
        if (qVarT0.f3277o) {
            return false;
        }
        return P0(qVarT0, keyEvent);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean I0(int r5, android.view.KeyEvent r6) {
        /*
            r4 = this;
            androidx.appcompat.view.b r0 = r4.f3236v
            r1 = 0
            if (r0 == 0) goto L6
            return r1
        L6:
            r0 = 1
            androidx.appcompat.app.h$q r2 = r4.t0(r5, r0)
            if (r5 != 0) goto L43
            androidx.appcompat.widget.I r5 = r4.f3233s
            if (r5 == 0) goto L43
            boolean r5 = r5.h()
            if (r5 == 0) goto L43
            android.content.Context r5 = r4.f3226l
            android.view.ViewConfiguration r5 = android.view.ViewConfiguration.get(r5)
            boolean r5 = r5.hasPermanentMenuKey()
            if (r5 != 0) goto L43
            androidx.appcompat.widget.I r5 = r4.f3233s
            boolean r5 = r5.b()
            if (r5 != 0) goto L3c
            boolean r5 = r4.f3207S
            if (r5 != 0) goto L62
            boolean r5 = r4.P0(r2, r6)
            if (r5 == 0) goto L62
            androidx.appcompat.widget.I r5 = r4.f3233s
            boolean r0 = r5.g()
            goto L68
        L3c:
            androidx.appcompat.widget.I r5 = r4.f3233s
            boolean r0 = r5.f()
            goto L68
        L43:
            boolean r5 = r2.f3277o
            if (r5 != 0) goto L64
            boolean r3 = r2.f3276n
            if (r3 == 0) goto L4c
            goto L64
        L4c:
            boolean r5 = r2.f3275m
            if (r5 == 0) goto L62
            boolean r5 = r2.f3280r
            if (r5 == 0) goto L5b
            r2.f3275m = r1
            boolean r5 = r4.P0(r2, r6)
            goto L5c
        L5b:
            r5 = r0
        L5c:
            if (r5 == 0) goto L62
            r4.M0(r2, r6)
            goto L68
        L62:
            r0 = r1
            goto L68
        L64:
            r4.c0(r2, r0)
            r0 = r5
        L68:
            if (r0 == 0) goto L85
            android.content.Context r5 = r4.f3226l
            android.content.Context r5 = r5.getApplicationContext()
            java.lang.String r6 = "audio"
            java.lang.Object r5 = r5.getSystemService(r6)
            android.media.AudioManager r5 = (android.media.AudioManager) r5
            if (r5 == 0) goto L7e
            r5.playSoundEffect(r1)
            goto L85
        L7e:
            java.lang.String r5 = "AppCompatDelegate"
            java.lang.String r6 = "Couldn't get audio manager"
            android.util.Log.w(r5, r6)
        L85:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.h.I0(int, android.view.KeyEvent):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void M0(androidx.appcompat.app.h.q r12, android.view.KeyEvent r13) {
        /*
            Method dump skipped, instruction units count: 244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.h.M0(androidx.appcompat.app.h$q, android.view.KeyEvent):void");
    }

    private boolean O0(q qVar, int i3, KeyEvent keyEvent, int i4) {
        androidx.appcompat.view.menu.e eVar;
        boolean zPerformShortcut = false;
        if (keyEvent.isSystem()) {
            return false;
        }
        if ((qVar.f3275m || P0(qVar, keyEvent)) && (eVar = qVar.f3272j) != null) {
            zPerformShortcut = eVar.performShortcut(i3, keyEvent, i4);
        }
        if (zPerformShortcut && (i4 & 1) == 0 && this.f3233s == null) {
            c0(qVar, true);
        }
        return zPerformShortcut;
    }

    private boolean P0(q qVar, KeyEvent keyEvent) {
        I i3;
        I i4;
        I i5;
        if (this.f3207S) {
            return false;
        }
        if (qVar.f3275m) {
            return true;
        }
        q qVar2 = this.f3203O;
        if (qVar2 != null && qVar2 != qVar) {
            c0(qVar2, false);
        }
        Window.Callback callbackV0 = v0();
        if (callbackV0 != null) {
            qVar.f3271i = callbackV0.onCreatePanelView(qVar.f3263a);
        }
        int i6 = qVar.f3263a;
        boolean z3 = i6 == 0 || i6 == 108;
        if (z3 && (i5 = this.f3233s) != null) {
            i5.d();
        }
        if (qVar.f3271i == null) {
            if (z3) {
                N0();
            }
            androidx.appcompat.view.menu.e eVar = qVar.f3272j;
            if (eVar == null || qVar.f3280r) {
                if (eVar == null && (!z0(qVar) || qVar.f3272j == null)) {
                    return false;
                }
                if (z3 && this.f3233s != null) {
                    if (this.f3234t == null) {
                        this.f3234t = new f();
                    }
                    this.f3233s.a(qVar.f3272j, this.f3234t);
                }
                qVar.f3272j.e0();
                if (!callbackV0.onCreatePanelMenu(qVar.f3263a, qVar.f3272j)) {
                    qVar.c(null);
                    if (z3 && (i3 = this.f3233s) != null) {
                        i3.a(null, this.f3234t);
                    }
                    return false;
                }
                qVar.f3280r = false;
            }
            qVar.f3272j.e0();
            Bundle bundle = qVar.f3281s;
            if (bundle != null) {
                qVar.f3272j.Q(bundle);
                qVar.f3281s = null;
            }
            if (!callbackV0.onPreparePanel(0, qVar.f3271i, qVar.f3272j)) {
                if (z3 && (i4 = this.f3233s) != null) {
                    i4.a(null, this.f3234t);
                }
                qVar.f3272j.d0();
                return false;
            }
            boolean z4 = KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1;
            qVar.f3278p = z4;
            qVar.f3272j.setQwertyMode(z4);
            qVar.f3272j.d0();
        }
        qVar.f3275m = true;
        qVar.f3276n = false;
        this.f3203O = qVar;
        return true;
    }

    private void Q0(boolean z3) {
        I i3 = this.f3233s;
        if (i3 == null || !i3.h() || (ViewConfiguration.get(this.f3226l).hasPermanentMenuKey() && !this.f3233s.e())) {
            q qVarT0 = t0(0, true);
            qVarT0.f3279q = true;
            c0(qVarT0, false);
            M0(qVarT0, null);
            return;
        }
        Window.Callback callbackV0 = v0();
        if (this.f3233s.b() && z3) {
            this.f3233s.f();
            if (this.f3207S) {
                return;
            }
            callbackV0.onPanelClosed(108, t0(0, true).f3272j);
            return;
        }
        if (callbackV0 == null || this.f3207S) {
            return;
        }
        if (this.f3215a0 && (this.f3216b0 & 1) != 0) {
            this.f3227m.getDecorView().removeCallbacks(this.f3217c0);
            this.f3217c0.run();
        }
        q qVarT02 = t0(0, true);
        androidx.appcompat.view.menu.e eVar = qVarT02.f3272j;
        if (eVar == null || qVarT02.f3280r || !callbackV0.onPreparePanel(0, qVarT02.f3271i, eVar)) {
            return;
        }
        callbackV0.onMenuOpened(108, qVarT02.f3272j);
        this.f3233s.g();
    }

    private int R0(int i3) {
        if (i3 == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        }
        if (i3 != 9) {
            return i3;
        }
        Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
        return 109;
    }

    private boolean S(boolean z3) {
        return T(z3, true);
    }

    private boolean T(boolean z3, boolean z4) {
        if (this.f3207S) {
            return false;
        }
        int iX = X();
        int iC0 = C0(this.f3226l, iX);
        androidx.core.os.e eVarW = Build.VERSION.SDK_INT < 33 ? W(this.f3226l) : null;
        if (!z4 && eVarW != null) {
            eVarW = s0(this.f3226l.getResources().getConfiguration());
        }
        boolean zC1 = c1(iC0, eVarW, z3);
        if (iX == 0) {
            r0(this.f3226l).e();
        } else {
            n nVar = this.f3213Y;
            if (nVar != null) {
                nVar.a();
            }
        }
        if (iX == 3) {
            q0(this.f3226l).e();
        } else {
            n nVar2 = this.f3214Z;
            if (nVar2 != null) {
                nVar2.a();
            }
        }
        return zC1;
    }

    private void U() {
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) this.f3191C.findViewById(R.id.content);
        View decorView = this.f3227m.getDecorView();
        contentFrameLayout.a(decorView.getPaddingLeft(), decorView.getPaddingTop(), decorView.getPaddingRight(), decorView.getPaddingBottom());
        TypedArray typedArrayObtainStyledAttributes = this.f3226l.obtainStyledAttributes(d.j.f9054y0);
        typedArrayObtainStyledAttributes.getValue(d.j.f8889K0, contentFrameLayout.getMinWidthMajor());
        typedArrayObtainStyledAttributes.getValue(d.j.f8893L0, contentFrameLayout.getMinWidthMinor());
        if (typedArrayObtainStyledAttributes.hasValue(d.j.f8881I0)) {
            typedArrayObtainStyledAttributes.getValue(d.j.f8881I0, contentFrameLayout.getFixedWidthMajor());
        }
        if (typedArrayObtainStyledAttributes.hasValue(d.j.f8885J0)) {
            typedArrayObtainStyledAttributes.getValue(d.j.f8885J0, contentFrameLayout.getFixedWidthMinor());
        }
        if (typedArrayObtainStyledAttributes.hasValue(d.j.f8873G0)) {
            typedArrayObtainStyledAttributes.getValue(d.j.f8873G0, contentFrameLayout.getFixedHeightMajor());
        }
        if (typedArrayObtainStyledAttributes.hasValue(d.j.f8877H0)) {
            typedArrayObtainStyledAttributes.getValue(d.j.f8877H0, contentFrameLayout.getFixedHeightMinor());
        }
        typedArrayObtainStyledAttributes.recycle();
        contentFrameLayout.requestLayout();
    }

    private void V(Window window) {
        if (this.f3227m != null) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        Window.Callback callback = window.getCallback();
        if (callback instanceof l) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        l lVar = new l(callback);
        this.f3228n = lVar;
        window.setCallback(lVar);
        h0 h0VarT = h0.t(this.f3226l, null, f3187m0);
        Drawable drawableG = h0VarT.g(0);
        if (drawableG != null) {
            window.setBackgroundDrawable(drawableG);
        }
        h0VarT.w();
        this.f3227m = window;
        if (Build.VERSION.SDK_INT < 33 || this.f3223i0 != null) {
            return;
        }
        N(null);
    }

    private boolean V0(ViewParent viewParent) {
        if (viewParent == null) {
            return false;
        }
        View decorView = this.f3227m.getDecorView();
        while (viewParent != null) {
            if (viewParent == decorView || !(viewParent instanceof View) || ((View) viewParent).isAttachedToWindow()) {
                return false;
            }
            viewParent = viewParent.getParent();
        }
        return true;
    }

    private int X() {
        int i3 = this.f3209U;
        return i3 != -100 ? i3 : androidx.appcompat.app.f.o();
    }

    private void Z0() {
        if (this.f3190B) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }

    private void a0() {
        n nVar = this.f3213Y;
        if (nVar != null) {
            nVar.a();
        }
        n nVar2 = this.f3214Z;
        if (nVar2 != null) {
            nVar2.a();
        }
    }

    private androidx.appcompat.app.c a1() {
        for (Context baseContext = this.f3226l; baseContext != null; baseContext = ((ContextWrapper) baseContext).getBaseContext()) {
            if (baseContext instanceof androidx.appcompat.app.c) {
                return (androidx.appcompat.app.c) baseContext;
            }
            if (!(baseContext instanceof ContextWrapper)) {
                break;
            }
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void b1(Configuration configuration) {
        Activity activity = (Activity) this.f3225k;
        if (activity instanceof androidx.lifecycle.l) {
            if (((androidx.lifecycle.l) activity).t().b().b(AbstractC0299g.b.CREATED)) {
                activity.onConfigurationChanged(configuration);
            }
        } else {
            if (!this.f3206R || this.f3207S) {
                return;
            }
            activity.onConfigurationChanged(configuration);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x008c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean c1(int r10, androidx.core.os.e r11, boolean r12) {
        /*
            Method dump skipped, instruction units count: 203
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.h.c1(int, androidx.core.os.e, boolean):boolean");
    }

    private Configuration d0(Context context, int i3, androidx.core.os.e eVar, Configuration configuration, boolean z3) {
        int i4 = i3 != 1 ? i3 != 2 ? z3 ? 0 : context.getApplicationContext().getResources().getConfiguration().uiMode & 48 : 32 : 16;
        Configuration configuration2 = new Configuration();
        configuration2.fontScale = 0.0f;
        if (configuration != null) {
            configuration2.setTo(configuration);
        }
        configuration2.uiMode = i4 | (configuration2.uiMode & (-49));
        if (eVar != null) {
            S0(configuration2, eVar);
        }
        return configuration2;
    }

    private ViewGroup e0() {
        ViewGroup viewGroup;
        TypedArray typedArrayObtainStyledAttributes = this.f3226l.obtainStyledAttributes(d.j.f9054y0);
        if (!typedArrayObtainStyledAttributes.hasValue(d.j.f8861D0)) {
            typedArrayObtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
        if (typedArrayObtainStyledAttributes.getBoolean(d.j.f8897M0, false)) {
            I(1);
        } else if (typedArrayObtainStyledAttributes.getBoolean(d.j.f8861D0, false)) {
            I(108);
        }
        if (typedArrayObtainStyledAttributes.getBoolean(d.j.f8865E0, false)) {
            I(109);
        }
        if (typedArrayObtainStyledAttributes.getBoolean(d.j.f8869F0, false)) {
            I(10);
        }
        this.f3199K = typedArrayObtainStyledAttributes.getBoolean(d.j.f9058z0, false);
        typedArrayObtainStyledAttributes.recycle();
        l0();
        this.f3227m.getDecorView();
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.f3226l);
        if (this.f3200L) {
            viewGroup = this.f3198J ? (ViewGroup) layoutInflaterFrom.inflate(d.g.f8824o, (ViewGroup) null) : (ViewGroup) layoutInflaterFrom.inflate(d.g.f8823n, (ViewGroup) null);
        } else if (this.f3199K) {
            viewGroup = (ViewGroup) layoutInflaterFrom.inflate(d.g.f8815f, (ViewGroup) null);
            this.f3197I = false;
            this.f3196H = false;
        } else if (this.f3196H) {
            TypedValue typedValue = new TypedValue();
            this.f3226l.getTheme().resolveAttribute(AbstractC0487a.f8679g, typedValue, true);
            viewGroup = (ViewGroup) LayoutInflater.from(typedValue.resourceId != 0 ? new androidx.appcompat.view.d(this.f3226l, typedValue.resourceId) : this.f3226l).inflate(d.g.f8825p, (ViewGroup) null);
            I i3 = (I) viewGroup.findViewById(d.f.f8799p);
            this.f3233s = i3;
            i3.setWindowCallback(v0());
            if (this.f3197I) {
                this.f3233s.k(109);
            }
            if (this.f3194F) {
                this.f3233s.k(2);
            }
            if (this.f3195G) {
                this.f3233s.k(5);
            }
        } else {
            viewGroup = null;
        }
        if (viewGroup == null) {
            throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.f3196H + ", windowActionBarOverlay: " + this.f3197I + ", android:windowIsFloating: " + this.f3199K + ", windowActionModeOverlay: " + this.f3198J + ", windowNoTitle: " + this.f3200L + " }");
        }
        Z.i0(viewGroup, new b());
        if (this.f3233s == null) {
            this.f3192D = (TextView) viewGroup.findViewById(d.f.f8780M);
        }
        s0.c(viewGroup);
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) viewGroup.findViewById(d.f.f8785b);
        ViewGroup viewGroup2 = (ViewGroup) this.f3227m.findViewById(R.id.content);
        if (viewGroup2 != null) {
            while (viewGroup2.getChildCount() > 0) {
                View childAt = viewGroup2.getChildAt(0);
                viewGroup2.removeViewAt(0);
                contentFrameLayout.addView(childAt);
            }
            viewGroup2.setId(-1);
            contentFrameLayout.setId(R.id.content);
            if (viewGroup2 instanceof FrameLayout) {
                ((FrameLayout) viewGroup2).setForeground(null);
            }
        }
        this.f3227m.setContentView(viewGroup);
        contentFrameLayout.setAttachListener(new c());
        return viewGroup;
    }

    private void e1(int i3, androidx.core.os.e eVar, boolean z3, Configuration configuration) {
        Resources resources = this.f3226l.getResources();
        Configuration configuration2 = new Configuration(resources.getConfiguration());
        if (configuration != null) {
            configuration2.updateFrom(configuration);
        }
        configuration2.uiMode = i3 | (resources.getConfiguration().uiMode & (-49));
        if (eVar != null) {
            S0(configuration2, eVar);
        }
        resources.updateConfiguration(configuration2, null);
        if (Build.VERSION.SDK_INT < 26) {
            w.a(resources);
        }
        int i4 = this.f3210V;
        if (i4 != 0) {
            this.f3226l.setTheme(i4);
            this.f3226l.getTheme().applyStyle(this.f3210V, true);
        }
        if (z3 && (this.f3225k instanceof Activity)) {
            b1(configuration2);
        }
    }

    private void g1(View view) {
        view.setBackgroundColor((Z.B(view) & 8192) != 0 ? androidx.core.content.a.b(this.f3226l, d.c.f8701b) : androidx.core.content.a.b(this.f3226l, d.c.f8700a));
    }

    private void k0() {
        if (this.f3190B) {
            return;
        }
        this.f3191C = e0();
        CharSequence charSequenceU0 = u0();
        if (!TextUtils.isEmpty(charSequenceU0)) {
            I i3 = this.f3233s;
            if (i3 != null) {
                i3.setWindowTitle(charSequenceU0);
            } else if (N0() != null) {
                N0().t(charSequenceU0);
            } else {
                TextView textView = this.f3192D;
                if (textView != null) {
                    textView.setText(charSequenceU0);
                }
            }
        }
        U();
        L0(this.f3191C);
        this.f3190B = true;
        q qVarT0 = t0(0, false);
        if (this.f3207S) {
            return;
        }
        if (qVarT0 == null || qVarT0.f3272j == null) {
            A0(108);
        }
    }

    private void l0() {
        if (this.f3227m == null) {
            Object obj = this.f3225k;
            if (obj instanceof Activity) {
                V(((Activity) obj).getWindow());
            }
        }
        if (this.f3227m == null) {
            throw new IllegalStateException("We have not been given a Window");
        }
    }

    private static Configuration n0(Configuration configuration, Configuration configuration2) {
        Configuration configuration3 = new Configuration();
        configuration3.fontScale = 0.0f;
        if (configuration2 != null && configuration.diff(configuration2) != 0) {
            float f3 = configuration.fontScale;
            float f4 = configuration2.fontScale;
            if (f3 != f4) {
                configuration3.fontScale = f4;
            }
            int i3 = configuration.mcc;
            int i4 = configuration2.mcc;
            if (i3 != i4) {
                configuration3.mcc = i4;
            }
            int i5 = configuration.mnc;
            int i6 = configuration2.mnc;
            if (i5 != i6) {
                configuration3.mnc = i6;
            }
            int i7 = Build.VERSION.SDK_INT;
            i.a(configuration, configuration2, configuration3);
            int i8 = configuration.touchscreen;
            int i9 = configuration2.touchscreen;
            if (i8 != i9) {
                configuration3.touchscreen = i9;
            }
            int i10 = configuration.keyboard;
            int i11 = configuration2.keyboard;
            if (i10 != i11) {
                configuration3.keyboard = i11;
            }
            int i12 = configuration.keyboardHidden;
            int i13 = configuration2.keyboardHidden;
            if (i12 != i13) {
                configuration3.keyboardHidden = i13;
            }
            int i14 = configuration.navigation;
            int i15 = configuration2.navigation;
            if (i14 != i15) {
                configuration3.navigation = i15;
            }
            int i16 = configuration.navigationHidden;
            int i17 = configuration2.navigationHidden;
            if (i16 != i17) {
                configuration3.navigationHidden = i17;
            }
            int i18 = configuration.orientation;
            int i19 = configuration2.orientation;
            if (i18 != i19) {
                configuration3.orientation = i19;
            }
            int i20 = configuration.screenLayout & 15;
            int i21 = configuration2.screenLayout;
            if (i20 != (i21 & 15)) {
                configuration3.screenLayout |= i21 & 15;
            }
            int i22 = configuration.screenLayout & 192;
            int i23 = configuration2.screenLayout;
            if (i22 != (i23 & 192)) {
                configuration3.screenLayout |= i23 & 192;
            }
            int i24 = configuration.screenLayout & 48;
            int i25 = configuration2.screenLayout;
            if (i24 != (i25 & 48)) {
                configuration3.screenLayout |= i25 & 48;
            }
            int i26 = configuration.screenLayout & 768;
            int i27 = configuration2.screenLayout;
            if (i26 != (i27 & 768)) {
                configuration3.screenLayout |= i27 & 768;
            }
            if (i7 >= 26) {
                j.a(configuration, configuration2, configuration3);
            }
            int i28 = configuration.uiMode & 15;
            int i29 = configuration2.uiMode;
            if (i28 != (i29 & 15)) {
                configuration3.uiMode |= i29 & 15;
            }
            int i30 = configuration.uiMode & 48;
            int i31 = configuration2.uiMode;
            if (i30 != (i31 & 48)) {
                configuration3.uiMode |= i31 & 48;
            }
            int i32 = configuration.screenWidthDp;
            int i33 = configuration2.screenWidthDp;
            if (i32 != i33) {
                configuration3.screenWidthDp = i33;
            }
            int i34 = configuration.screenHeightDp;
            int i35 = configuration2.screenHeightDp;
            if (i34 != i35) {
                configuration3.screenHeightDp = i35;
            }
            int i36 = configuration.smallestScreenWidthDp;
            int i37 = configuration2.smallestScreenWidthDp;
            if (i36 != i37) {
                configuration3.smallestScreenWidthDp = i37;
            }
            int i38 = configuration.densityDpi;
            int i39 = configuration2.densityDpi;
            if (i38 != i39) {
                configuration3.densityDpi = i39;
            }
        }
        return configuration3;
    }

    private int p0(Context context) {
        if (!this.f3212X && (this.f3225k instanceof Activity)) {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return 0;
            }
            try {
                ActivityInfo activityInfo = packageManager.getActivityInfo(new ComponentName(context, this.f3225k.getClass()), Build.VERSION.SDK_INT >= 29 ? 269221888 : 786432);
                if (activityInfo != null) {
                    this.f3211W = activityInfo.configChanges;
                }
            } catch (PackageManager.NameNotFoundException e4) {
                Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", e4);
                this.f3211W = 0;
            }
        }
        this.f3212X = true;
        return this.f3211W;
    }

    private n q0(Context context) {
        if (this.f3214Z == null) {
            this.f3214Z = new m(context);
        }
        return this.f3214Z;
    }

    private n r0(Context context) {
        if (this.f3213Y == null) {
            this.f3213Y = new o(y.a(context));
        }
        return this.f3213Y;
    }

    private void w0() {
        k0();
        if (this.f3196H && this.f3230p == null) {
            Object obj = this.f3225k;
            if (obj instanceof Activity) {
                this.f3230p = new z((Activity) this.f3225k, this.f3197I);
            } else if (obj instanceof Dialog) {
                this.f3230p = new z((Dialog) this.f3225k);
            }
            androidx.appcompat.app.a aVar = this.f3230p;
            if (aVar != null) {
                aVar.r(this.f3218d0);
            }
        }
    }

    private boolean x0(q qVar) {
        View view = qVar.f3271i;
        if (view != null) {
            qVar.f3270h = view;
            return true;
        }
        if (qVar.f3272j == null) {
            return false;
        }
        if (this.f3235u == null) {
            this.f3235u = new r();
        }
        View view2 = (View) qVar.a(this.f3235u);
        qVar.f3270h = view2;
        return view2 != null;
    }

    private boolean y0(q qVar) {
        qVar.d(o0());
        qVar.f3269g = new p(qVar.f3274l);
        qVar.f3265c = 81;
        return true;
    }

    private boolean z0(q qVar) {
        Resources.Theme themeNewTheme;
        Context context = this.f3226l;
        int i3 = qVar.f3263a;
        if ((i3 == 0 || i3 == 108) && this.f3233s != null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(AbstractC0487a.f8679g, typedValue, true);
            if (typedValue.resourceId != 0) {
                themeNewTheme = context.getResources().newTheme();
                themeNewTheme.setTo(theme);
                themeNewTheme.applyStyle(typedValue.resourceId, true);
                themeNewTheme.resolveAttribute(AbstractC0487a.f8680h, typedValue, true);
            } else {
                theme.resolveAttribute(AbstractC0487a.f8680h, typedValue, true);
                themeNewTheme = null;
            }
            if (typedValue.resourceId != 0) {
                if (themeNewTheme == null) {
                    themeNewTheme = context.getResources().newTheme();
                    themeNewTheme.setTo(theme);
                }
                themeNewTheme.applyStyle(typedValue.resourceId, true);
            }
            if (themeNewTheme != null) {
                androidx.appcompat.view.d dVar = new androidx.appcompat.view.d(context, 0);
                dVar.getTheme().setTo(themeNewTheme);
                context = dVar;
            }
        }
        androidx.appcompat.view.menu.e eVar = new androidx.appcompat.view.menu.e(context);
        eVar.S(this);
        qVar.c(eVar);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0045  */
    @Override // androidx.appcompat.app.f
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void A() {
        /*
            r3 = this;
            java.lang.Object r0 = r3.f3225k
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 == 0) goto L9
            androidx.appcompat.app.f.G(r3)
        L9:
            boolean r0 = r3.f3215a0
            if (r0 == 0) goto L18
            android.view.Window r0 = r3.f3227m
            android.view.View r0 = r0.getDecorView()
            java.lang.Runnable r1 = r3.f3217c0
            r0.removeCallbacks(r1)
        L18:
            r0 = 1
            r3.f3207S = r0
            int r0 = r3.f3209U
            r1 = -100
            if (r0 == r1) goto L45
            java.lang.Object r0 = r3.f3225k
            boolean r1 = r0 instanceof android.app.Activity
            if (r1 == 0) goto L45
            android.app.Activity r0 = (android.app.Activity) r0
            boolean r0 = r0.isChangingConfigurations()
            if (r0 == 0) goto L45
            l.g r0 = androidx.appcompat.app.h.f3185k0
            java.lang.Object r1 = r3.f3225k
            java.lang.Class r1 = r1.getClass()
            java.lang.String r1 = r1.getName()
            int r2 = r3.f3209U
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r0.put(r1, r2)
            goto L54
        L45:
            l.g r0 = androidx.appcompat.app.h.f3185k0
            java.lang.Object r1 = r3.f3225k
            java.lang.Class r1 = r1.getClass()
            java.lang.String r1 = r1.getName()
            r0.remove(r1)
        L54:
            androidx.appcompat.app.a r0 = r3.f3230p
            if (r0 == 0) goto L5b
            r0.n()
        L5b:
            r3.a0()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.h.A():void");
    }

    @Override // androidx.appcompat.app.f
    public void B(Bundle bundle) {
        k0();
    }

    public boolean B0() {
        return this.f3189A;
    }

    @Override // androidx.appcompat.app.f
    public void C() {
        androidx.appcompat.app.a aVarT = t();
        if (aVarT != null) {
            aVarT.s(true);
        }
    }

    int C0(Context context, int i3) {
        if (i3 == -100) {
            return -1;
        }
        if (i3 != -1) {
            if (i3 == 0) {
                if (((UiModeManager) context.getApplicationContext().getSystemService("uimode")).getNightMode() == 0) {
                    return -1;
                }
                return r0(context).c();
            }
            if (i3 != 1 && i3 != 2) {
                if (i3 == 3) {
                    return q0(context).c();
                }
                throw new IllegalStateException("Unknown value set for night mode. Please use one of the MODE_NIGHT values from AppCompatDelegate.");
            }
        }
        return i3;
    }

    @Override // androidx.appcompat.app.f
    public void D(Bundle bundle) {
    }

    boolean D0() {
        boolean z3 = this.f3204P;
        this.f3204P = false;
        q qVarT0 = t0(0, false);
        if (qVarT0 != null && qVarT0.f3277o) {
            if (!z3) {
                c0(qVarT0, true);
            }
            return true;
        }
        androidx.appcompat.view.b bVar = this.f3236v;
        if (bVar != null) {
            bVar.c();
            return true;
        }
        androidx.appcompat.app.a aVarT = t();
        return aVarT != null && aVarT.h();
    }

    @Override // androidx.appcompat.app.f
    public void E() {
        T(true, false);
    }

    boolean E0(int i3, KeyEvent keyEvent) {
        if (i3 == 4) {
            this.f3204P = (keyEvent.getFlags() & 128) != 0;
        } else if (i3 == 82) {
            F0(0, keyEvent);
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.app.f
    public void F() {
        androidx.appcompat.app.a aVarT = t();
        if (aVarT != null) {
            aVarT.s(false);
        }
    }

    boolean G0(int i3, KeyEvent keyEvent) {
        androidx.appcompat.app.a aVarT = t();
        if (aVarT != null && aVarT.o(i3, keyEvent)) {
            return true;
        }
        q qVar = this.f3203O;
        if (qVar != null && O0(qVar, keyEvent.getKeyCode(), keyEvent, 1)) {
            q qVar2 = this.f3203O;
            if (qVar2 != null) {
                qVar2.f3276n = true;
            }
            return true;
        }
        if (this.f3203O == null) {
            q qVarT0 = t0(0, true);
            P0(qVarT0, keyEvent);
            boolean zO0 = O0(qVarT0, keyEvent.getKeyCode(), keyEvent, 1);
            qVarT0.f3275m = false;
            if (zO0) {
                return true;
            }
        }
        return false;
    }

    boolean H0(int i3, KeyEvent keyEvent) {
        if (i3 != 4) {
            if (i3 == 82) {
                I0(0, keyEvent);
                return true;
            }
        } else if (D0()) {
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.app.f
    public boolean I(int i3) {
        int iR0 = R0(i3);
        if (this.f3200L && iR0 == 108) {
            return false;
        }
        if (this.f3196H && iR0 == 1) {
            this.f3196H = false;
        }
        if (iR0 == 1) {
            Z0();
            this.f3200L = true;
            return true;
        }
        if (iR0 == 2) {
            Z0();
            this.f3194F = true;
            return true;
        }
        if (iR0 == 5) {
            Z0();
            this.f3195G = true;
            return true;
        }
        if (iR0 == 10) {
            Z0();
            this.f3198J = true;
            return true;
        }
        if (iR0 == 108) {
            Z0();
            this.f3196H = true;
            return true;
        }
        if (iR0 != 109) {
            return this.f3227m.requestFeature(iR0);
        }
        Z0();
        this.f3197I = true;
        return true;
    }

    @Override // androidx.appcompat.app.f
    public void J(int i3) {
        k0();
        ViewGroup viewGroup = (ViewGroup) this.f3191C.findViewById(R.id.content);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.f3226l).inflate(i3, viewGroup);
        this.f3228n.c(this.f3227m.getCallback());
    }

    void J0(int i3) {
        androidx.appcompat.app.a aVarT;
        if (i3 != 108 || (aVarT = t()) == null) {
            return;
        }
        aVarT.i(true);
    }

    @Override // androidx.appcompat.app.f
    public void K(View view) {
        k0();
        ViewGroup viewGroup = (ViewGroup) this.f3191C.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.f3228n.c(this.f3227m.getCallback());
    }

    void K0(int i3) {
        if (i3 == 108) {
            androidx.appcompat.app.a aVarT = t();
            if (aVarT != null) {
                aVarT.i(false);
                return;
            }
            return;
        }
        if (i3 == 0) {
            q qVarT0 = t0(i3, true);
            if (qVarT0.f3277o) {
                c0(qVarT0, false);
            }
        }
    }

    @Override // androidx.appcompat.app.f
    public void L(View view, ViewGroup.LayoutParams layoutParams) {
        k0();
        ViewGroup viewGroup = (ViewGroup) this.f3191C.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        this.f3228n.c(this.f3227m.getCallback());
    }

    void L0(ViewGroup viewGroup) {
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002c  */
    @Override // androidx.appcompat.app.f
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void N(android.window.OnBackInvokedDispatcher r3) {
        /*
            r2 = this;
            super.N(r3)
            android.window.OnBackInvokedDispatcher r0 = r2.f3223i0
            if (r0 == 0) goto L11
            android.window.OnBackInvokedCallback r1 = r2.f3224j0
            if (r1 == 0) goto L11
            androidx.appcompat.app.h.k.c(r0, r1)
            r0 = 0
            r2.f3224j0 = r0
        L11:
            if (r3 != 0) goto L2c
            java.lang.Object r0 = r2.f3225k
            boolean r1 = r0 instanceof android.app.Activity
            if (r1 == 0) goto L2c
            android.app.Activity r0 = (android.app.Activity) r0
            android.view.Window r0 = r0.getWindow()
            if (r0 == 0) goto L2c
            java.lang.Object r3 = r2.f3225k
            android.app.Activity r3 = (android.app.Activity) r3
            android.window.OnBackInvokedDispatcher r3 = androidx.appcompat.app.h.k.a(r3)
            r2.f3223i0 = r3
            goto L2e
        L2c:
            r2.f3223i0 = r3
        L2e:
            r2.d1()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.h.N(android.window.OnBackInvokedDispatcher):void");
    }

    final androidx.appcompat.app.a N0() {
        return this.f3230p;
    }

    @Override // androidx.appcompat.app.f
    public void O(int i3) {
        this.f3210V = i3;
    }

    @Override // androidx.appcompat.app.f
    public final void P(CharSequence charSequence) {
        this.f3232r = charSequence;
        I i3 = this.f3233s;
        if (i3 != null) {
            i3.setWindowTitle(charSequence);
            return;
        }
        if (N0() != null) {
            N0().t(charSequence);
            return;
        }
        TextView textView = this.f3192D;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    void S0(Configuration configuration, androidx.core.os.e eVar) {
        i.d(configuration, eVar);
    }

    void T0(androidx.core.os.e eVar) {
        i.c(eVar);
    }

    final boolean U0() {
        ViewGroup viewGroup;
        return this.f3190B && (viewGroup = this.f3191C) != null && viewGroup.isLaidOut();
    }

    androidx.core.os.e W(Context context) {
        androidx.core.os.e eVarS;
        if (Build.VERSION.SDK_INT >= 33 || (eVarS = androidx.appcompat.app.f.s()) == null) {
            return null;
        }
        androidx.core.os.e eVarS0 = s0(context.getApplicationContext().getResources().getConfiguration());
        androidx.core.os.e eVarB = v.b(eVarS, eVarS0);
        return eVarB.e() ? eVarS0 : eVarB;
    }

    boolean W0() {
        if (this.f3223i0 == null) {
            return false;
        }
        q qVarT0 = t0(0, false);
        return (qVarT0 != null && qVarT0.f3277o) || this.f3236v != null;
    }

    public androidx.appcompat.view.b X0(b.a aVar) {
        androidx.appcompat.app.d dVar;
        if (aVar == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }
        androidx.appcompat.view.b bVar = this.f3236v;
        if (bVar != null) {
            bVar.c();
        }
        g gVar = new g(aVar);
        androidx.appcompat.app.a aVarT = t();
        if (aVarT != null) {
            androidx.appcompat.view.b bVarU = aVarT.u(gVar);
            this.f3236v = bVarU;
            if (bVarU != null && (dVar = this.f3229o) != null) {
                dVar.f(bVarU);
            }
        }
        if (this.f3236v == null) {
            this.f3236v = Y0(gVar);
        }
        d1();
        return this.f3236v;
    }

    void Y(int i3, q qVar, Menu menu) {
        if (menu == null) {
            if (qVar == null && i3 >= 0) {
                q[] qVarArr = this.f3202N;
                if (i3 < qVarArr.length) {
                    qVar = qVarArr[i3];
                }
            }
            if (qVar != null) {
                menu = qVar.f3272j;
            }
        }
        if ((qVar == null || qVar.f3277o) && !this.f3207S) {
            this.f3228n.d(this.f3227m.getCallback(), i3, menu);
        }
    }

    androidx.appcompat.view.b Y0(b.a aVar) {
        androidx.appcompat.view.b bVarW;
        Context dVar;
        androidx.appcompat.app.d dVar2;
        j0();
        androidx.appcompat.view.b bVar = this.f3236v;
        if (bVar != null) {
            bVar.c();
        }
        if (!(aVar instanceof g)) {
            aVar = new g(aVar);
        }
        androidx.appcompat.app.d dVar3 = this.f3229o;
        if (dVar3 == null || this.f3207S) {
            bVarW = null;
        } else {
            try {
                bVarW = dVar3.w(aVar);
            } catch (AbstractMethodError unused) {
                bVarW = null;
            }
        }
        if (bVarW != null) {
            this.f3236v = bVarW;
        } else {
            if (this.f3237w == null) {
                if (this.f3199K) {
                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = this.f3226l.getTheme();
                    theme.resolveAttribute(AbstractC0487a.f8679g, typedValue, true);
                    if (typedValue.resourceId != 0) {
                        Resources.Theme themeNewTheme = this.f3226l.getResources().newTheme();
                        themeNewTheme.setTo(theme);
                        themeNewTheme.applyStyle(typedValue.resourceId, true);
                        dVar = new androidx.appcompat.view.d(this.f3226l, 0);
                        dVar.getTheme().setTo(themeNewTheme);
                    } else {
                        dVar = this.f3226l;
                    }
                    this.f3237w = new ActionBarContextView(dVar);
                    PopupWindow popupWindow = new PopupWindow(dVar, (AttributeSet) null, AbstractC0487a.f8682j);
                    this.f3238x = popupWindow;
                    androidx.core.widget.h.b(popupWindow, 2);
                    this.f3238x.setContentView(this.f3237w);
                    this.f3238x.setWidth(-1);
                    dVar.getTheme().resolveAttribute(AbstractC0487a.f8674b, typedValue, true);
                    this.f3237w.setContentHeight(TypedValue.complexToDimensionPixelSize(typedValue.data, dVar.getResources().getDisplayMetrics()));
                    this.f3238x.setHeight(-2);
                    this.f3239y = new d();
                } else {
                    ViewStubCompat viewStubCompat = (ViewStubCompat) this.f3191C.findViewById(d.f.f8791h);
                    if (viewStubCompat != null) {
                        viewStubCompat.setLayoutInflater(LayoutInflater.from(o0()));
                        this.f3237w = (ActionBarContextView) viewStubCompat.a();
                    }
                }
            }
            if (this.f3237w != null) {
                j0();
                this.f3237w.k();
                androidx.appcompat.view.e eVar = new androidx.appcompat.view.e(this.f3237w.getContext(), this.f3237w, aVar, this.f3238x == null);
                if (aVar.d(eVar, eVar.e())) {
                    eVar.k();
                    this.f3237w.h(eVar);
                    this.f3236v = eVar;
                    if (U0()) {
                        this.f3237w.setAlpha(0.0f);
                        C0254i0 c0254i0B = Z.c(this.f3237w).b(1.0f);
                        this.f3240z = c0254i0B;
                        c0254i0B.h(new e());
                    } else {
                        this.f3237w.setAlpha(1.0f);
                        this.f3237w.setVisibility(0);
                        if (this.f3237w.getParent() instanceof View) {
                            Z.U((View) this.f3237w.getParent());
                        }
                    }
                    if (this.f3238x != null) {
                        this.f3227m.getDecorView().post(this.f3239y);
                    }
                } else {
                    this.f3236v = null;
                }
            }
        }
        androidx.appcompat.view.b bVar2 = this.f3236v;
        if (bVar2 != null && (dVar2 = this.f3229o) != null) {
            dVar2.f(bVar2);
        }
        d1();
        return this.f3236v;
    }

    void Z(androidx.appcompat.view.menu.e eVar) {
        if (this.f3201M) {
            return;
        }
        this.f3201M = true;
        this.f3233s.l();
        Window.Callback callbackV0 = v0();
        if (callbackV0 != null && !this.f3207S) {
            callbackV0.onPanelClosed(108, eVar);
        }
        this.f3201M = false;
    }

    @Override // androidx.appcompat.view.menu.e.a
    public boolean a(androidx.appcompat.view.menu.e eVar, MenuItem menuItem) {
        q qVarM0;
        Window.Callback callbackV0 = v0();
        if (callbackV0 == null || this.f3207S || (qVarM0 = m0(eVar.D())) == null) {
            return false;
        }
        return callbackV0.onMenuItemSelected(qVarM0.f3263a, menuItem);
    }

    @Override // androidx.appcompat.view.menu.e.a
    public void b(androidx.appcompat.view.menu.e eVar) {
        Q0(true);
    }

    void b0(int i3) {
        c0(t0(i3, true), true);
    }

    void c0(q qVar, boolean z3) {
        ViewGroup viewGroup;
        I i3;
        if (z3 && qVar.f3263a == 0 && (i3 = this.f3233s) != null && i3.b()) {
            Z(qVar.f3272j);
            return;
        }
        WindowManager windowManager = (WindowManager) this.f3226l.getSystemService("window");
        if (windowManager != null && qVar.f3277o && (viewGroup = qVar.f3269g) != null) {
            windowManager.removeView(viewGroup);
            if (z3) {
                Y(qVar.f3263a, qVar, null);
            }
        }
        qVar.f3275m = false;
        qVar.f3276n = false;
        qVar.f3277o = false;
        qVar.f3270h = null;
        qVar.f3279q = true;
        if (this.f3203O == qVar) {
            this.f3203O = null;
        }
        if (qVar.f3263a == 0) {
            d1();
        }
    }

    void d1() {
        OnBackInvokedCallback onBackInvokedCallback;
        if (Build.VERSION.SDK_INT >= 33) {
            boolean zW0 = W0();
            if (zW0 && this.f3224j0 == null) {
                this.f3224j0 = k.b(this.f3223i0, this);
            } else {
                if (zW0 || (onBackInvokedCallback = this.f3224j0) == null) {
                    return;
                }
                k.c(this.f3223i0, onBackInvokedCallback);
                this.f3224j0 = null;
            }
        }
    }

    @Override // androidx.appcompat.app.f
    public void e(View view, ViewGroup.LayoutParams layoutParams) {
        k0();
        ((ViewGroup) this.f3191C.findViewById(R.id.content)).addView(view, layoutParams);
        this.f3228n.c(this.f3227m.getCallback());
    }

    @Override // androidx.appcompat.app.f
    public boolean f() {
        return S(true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public View f0(View view, String str, Context context, AttributeSet attributeSet) {
        boolean z3;
        boolean zV0 = false;
        if (this.f3221g0 == null) {
            TypedArray typedArrayObtainStyledAttributes = this.f3226l.obtainStyledAttributes(d.j.f9054y0);
            String string = typedArrayObtainStyledAttributes.getString(d.j.f8857C0);
            typedArrayObtainStyledAttributes.recycle();
            if (string == null) {
                this.f3221g0 = new s();
            } else {
                try {
                    this.f3221g0 = (s) this.f3226l.getClassLoader().loadClass(string).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (Throwable th) {
                    Log.i("AppCompatDelegate", "Failed to instantiate custom view inflater " + string + ". Falling back to default.", th);
                    this.f3221g0 = new s();
                }
            }
        }
        boolean z4 = f3186l0;
        if (z4) {
            if (this.f3222h0 == null) {
                this.f3222h0 = new u();
            }
            if (this.f3222h0.a(attributeSet)) {
                z3 = true;
            } else {
                if (!(attributeSet instanceof XmlPullParser)) {
                    zV0 = V0((ViewParent) view);
                } else if (((XmlPullParser) attributeSet).getDepth() > 1) {
                    zV0 = true;
                }
                z3 = zV0;
            }
        } else {
            z3 = zV0;
        }
        return this.f3221g0.r(view, str, context, attributeSet, z3, z4, true, r0.c());
    }

    final int f1(C0264n0 c0264n0, Rect rect) {
        boolean z3;
        boolean z4;
        int iK = c0264n0 != null ? c0264n0.k() : rect != null ? rect.top : 0;
        ActionBarContextView actionBarContextView = this.f3237w;
        if (actionBarContextView == null || !(actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            z3 = false;
        } else {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f3237w.getLayoutParams();
            if (this.f3237w.isShown()) {
                if (this.f3219e0 == null) {
                    this.f3219e0 = new Rect();
                    this.f3220f0 = new Rect();
                }
                Rect rect2 = this.f3219e0;
                Rect rect3 = this.f3220f0;
                if (c0264n0 == null) {
                    rect2.set(rect);
                } else {
                    rect2.set(c0264n0.i(), c0264n0.k(), c0264n0.j(), c0264n0.h());
                }
                s0.a(this.f3191C, rect2, rect3);
                int i3 = rect2.top;
                int i4 = rect2.left;
                int i5 = rect2.right;
                C0264n0 c0264n0Y = Z.y(this.f3191C);
                int i6 = c0264n0Y == null ? 0 : c0264n0Y.i();
                int iJ = c0264n0Y == null ? 0 : c0264n0Y.j();
                if (marginLayoutParams.topMargin == i3 && marginLayoutParams.leftMargin == i4 && marginLayoutParams.rightMargin == i5) {
                    z4 = false;
                } else {
                    marginLayoutParams.topMargin = i3;
                    marginLayoutParams.leftMargin = i4;
                    marginLayoutParams.rightMargin = i5;
                    z4 = true;
                }
                if (i3 <= 0 || this.f3193E != null) {
                    View view = this.f3193E;
                    if (view != null) {
                        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                        int i7 = marginLayoutParams2.height;
                        int i8 = marginLayoutParams.topMargin;
                        if (i7 != i8 || marginLayoutParams2.leftMargin != i6 || marginLayoutParams2.rightMargin != iJ) {
                            marginLayoutParams2.height = i8;
                            marginLayoutParams2.leftMargin = i6;
                            marginLayoutParams2.rightMargin = iJ;
                            this.f3193E.setLayoutParams(marginLayoutParams2);
                        }
                    }
                } else {
                    View view2 = new View(this.f3226l);
                    this.f3193E = view2;
                    view2.setVisibility(8);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, marginLayoutParams.topMargin, 51);
                    layoutParams.leftMargin = i6;
                    layoutParams.rightMargin = iJ;
                    this.f3191C.addView(this.f3193E, -1, layoutParams);
                }
                View view3 = this.f3193E;
                z = view3 != null;
                if (z && view3.getVisibility() != 0) {
                    g1(this.f3193E);
                }
                if (!this.f3198J && z) {
                    iK = 0;
                }
                z3 = z;
                z = z4;
            } else if (marginLayoutParams.topMargin != 0) {
                marginLayoutParams.topMargin = 0;
                z3 = false;
            } else {
                z3 = false;
                z = false;
            }
            if (z) {
                this.f3237w.setLayoutParams(marginLayoutParams);
            }
        }
        View view4 = this.f3193E;
        if (view4 != null) {
            view4.setVisibility(z3 ? 0 : 8);
        }
        return iK;
    }

    void g0() {
        androidx.appcompat.view.menu.e eVar;
        I i3 = this.f3233s;
        if (i3 != null) {
            i3.l();
        }
        if (this.f3238x != null) {
            this.f3227m.getDecorView().removeCallbacks(this.f3239y);
            if (this.f3238x.isShowing()) {
                try {
                    this.f3238x.dismiss();
                } catch (IllegalArgumentException unused) {
                }
            }
            this.f3238x = null;
        }
        j0();
        q qVarT0 = t0(0, false);
        if (qVarT0 == null || (eVar = qVarT0.f3272j) == null) {
            return;
        }
        eVar.close();
    }

    boolean h0(KeyEvent keyEvent) {
        View decorView;
        Object obj = this.f3225k;
        if (((obj instanceof AbstractC0276x.a) || (obj instanceof androidx.appcompat.app.r)) && (decorView = this.f3227m.getDecorView()) != null && AbstractC0276x.d(decorView, keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 82 && this.f3228n.b(this.f3227m.getCallback(), keyEvent)) {
            return true;
        }
        int keyCode = keyEvent.getKeyCode();
        return keyEvent.getAction() == 0 ? E0(keyCode, keyEvent) : H0(keyCode, keyEvent);
    }

    @Override // androidx.appcompat.app.f
    public Context i(Context context) {
        this.f3205Q = true;
        int iC0 = C0(context, X());
        if (androidx.appcompat.app.f.w(context)) {
            androidx.appcompat.app.f.R(context);
        }
        androidx.core.os.e eVarW = W(context);
        if (context instanceof ContextThemeWrapper) {
            try {
                ((ContextThemeWrapper) context).applyOverrideConfiguration(d0(context, iC0, eVarW, null, false));
                return context;
            } catch (IllegalStateException unused) {
            }
        }
        if (context instanceof androidx.appcompat.view.d) {
            try {
                ((androidx.appcompat.view.d) context).a(d0(context, iC0, eVarW, null, false));
                return context;
            } catch (IllegalStateException unused2) {
            }
        }
        if (!f3188n0) {
            return super.i(context);
        }
        Configuration configuration = new Configuration();
        configuration.uiMode = -1;
        configuration.fontScale = 0.0f;
        Configuration configuration2 = context.createConfigurationContext(configuration).getResources().getConfiguration();
        Configuration configuration3 = context.getResources().getConfiguration();
        configuration2.uiMode = configuration3.uiMode;
        Configuration configurationD0 = d0(context, iC0, eVarW, !configuration2.equals(configuration3) ? n0(configuration2, configuration3) : null, true);
        androidx.appcompat.view.d dVar = new androidx.appcompat.view.d(context, d.i.f8846c);
        dVar.a(configurationD0);
        try {
            if (context.getTheme() != null) {
                f.C0061f.a(dVar.getTheme());
            }
        } catch (NullPointerException unused3) {
        }
        return super.i(dVar);
    }

    void i0(int i3) {
        q qVarT0;
        q qVarT02 = t0(i3, true);
        if (qVarT02.f3272j != null) {
            Bundle bundle = new Bundle();
            qVarT02.f3272j.R(bundle);
            if (bundle.size() > 0) {
                qVarT02.f3281s = bundle;
            }
            qVarT02.f3272j.e0();
            qVarT02.f3272j.clear();
        }
        qVarT02.f3280r = true;
        qVarT02.f3279q = true;
        if ((i3 != 108 && i3 != 0) || this.f3233s == null || (qVarT0 = t0(0, false)) == null) {
            return;
        }
        qVarT0.f3275m = false;
        P0(qVarT0, null);
    }

    void j0() {
        C0254i0 c0254i0 = this.f3240z;
        if (c0254i0 != null) {
            c0254i0.c();
        }
    }

    @Override // androidx.appcompat.app.f
    public View l(int i3) {
        k0();
        return this.f3227m.findViewById(i3);
    }

    q m0(Menu menu) {
        q[] qVarArr = this.f3202N;
        int length = qVarArr != null ? qVarArr.length : 0;
        for (int i3 = 0; i3 < length; i3++) {
            q qVar = qVarArr[i3];
            if (qVar != null && qVar.f3272j == menu) {
                return qVar;
            }
        }
        return null;
    }

    @Override // androidx.appcompat.app.f
    public Context n() {
        return this.f3226l;
    }

    final Context o0() {
        androidx.appcompat.app.a aVarT = t();
        Context contextK = aVarT != null ? aVarT.k() : null;
        return contextK == null ? this.f3226l : contextK;
    }

    @Override // android.view.LayoutInflater.Factory2
    public final View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return f0(view, str, context, attributeSet);
    }

    @Override // androidx.appcompat.app.f
    public int p() {
        return this.f3209U;
    }

    @Override // androidx.appcompat.app.f
    public MenuInflater r() {
        if (this.f3231q == null) {
            w0();
            androidx.appcompat.app.a aVar = this.f3230p;
            this.f3231q = new androidx.appcompat.view.g(aVar != null ? aVar.k() : this.f3226l);
        }
        return this.f3231q;
    }

    androidx.core.os.e s0(Configuration configuration) {
        return i.b(configuration);
    }

    @Override // androidx.appcompat.app.f
    public androidx.appcompat.app.a t() {
        w0();
        return this.f3230p;
    }

    protected q t0(int i3, boolean z3) {
        q[] qVarArr = this.f3202N;
        if (qVarArr == null || qVarArr.length <= i3) {
            q[] qVarArr2 = new q[i3 + 1];
            if (qVarArr != null) {
                System.arraycopy(qVarArr, 0, qVarArr2, 0, qVarArr.length);
            }
            this.f3202N = qVarArr2;
            qVarArr = qVarArr2;
        }
        q qVar = qVarArr[i3];
        if (qVar != null) {
            return qVar;
        }
        q qVar2 = new q(i3);
        qVarArr[i3] = qVar2;
        return qVar2;
    }

    @Override // androidx.appcompat.app.f
    public void u() {
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.f3226l);
        if (layoutInflaterFrom.getFactory() == null) {
            AbstractC0277y.a(layoutInflaterFrom, this);
        } else {
            if (layoutInflaterFrom.getFactory2() instanceof h) {
                return;
            }
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    final CharSequence u0() {
        Object obj = this.f3225k;
        return obj instanceof Activity ? ((Activity) obj).getTitle() : this.f3232r;
    }

    @Override // androidx.appcompat.app.f
    public void v() {
        if (N0() == null || t().l()) {
            return;
        }
        A0(0);
    }

    final Window.Callback v0() {
        return this.f3227m.getCallback();
    }

    @Override // androidx.appcompat.app.f
    public void y(Configuration configuration) {
        androidx.appcompat.app.a aVarT;
        if (this.f3196H && this.f3190B && (aVarT = t()) != null) {
            aVarT.m(configuration);
        }
        C0222k.b().g(this.f3226l);
        this.f3208T = new Configuration(this.f3226l.getResources().getConfiguration());
        T(false, false);
    }

    @Override // androidx.appcompat.app.f
    public void z(Bundle bundle) {
        String strC;
        this.f3205Q = true;
        S(false);
        l0();
        Object obj = this.f3225k;
        if (obj instanceof Activity) {
            try {
                strC = androidx.core.app.h.c((Activity) obj);
            } catch (IllegalArgumentException unused) {
                strC = null;
            }
            if (strC != null) {
                androidx.appcompat.app.a aVarN0 = N0();
                if (aVarN0 == null) {
                    this.f3218d0 = true;
                } else {
                    aVarN0.r(true);
                }
            }
            androidx.appcompat.app.f.d(this);
        }
        this.f3208T = new Configuration(this.f3226l.getResources().getConfiguration());
        this.f3206R = true;
    }

    h(Dialog dialog, androidx.appcompat.app.d dVar) {
        this(dialog.getContext(), dialog.getWindow(), dVar, dialog);
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    private h(Context context, Window window, androidx.appcompat.app.d dVar, Object obj) {
        androidx.appcompat.app.c cVarA1;
        this.f3240z = null;
        this.f3189A = true;
        this.f3209U = -100;
        this.f3217c0 = new a();
        this.f3226l = context;
        this.f3229o = dVar;
        this.f3225k = obj;
        if (this.f3209U == -100 && (obj instanceof Dialog) && (cVarA1 = a1()) != null) {
            this.f3209U = cVarA1.d0().p();
        }
        if (this.f3209U == -100) {
            l.g gVar = f3185k0;
            Integer num = (Integer) gVar.get(obj.getClass().getName());
            if (num != null) {
                this.f3209U = num.intValue();
                gVar.remove(obj.getClass().getName());
            }
        }
        if (window != null) {
            V(window);
        }
        C0222k.h();
    }
}
