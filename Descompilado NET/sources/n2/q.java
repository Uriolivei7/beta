package n2;

import android.content.Context;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;
import com.facebook.react.views.textinput.C0478j;
import com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class q extends C0625d {

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    public static final b f10047Q = new b(null);

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private static final a f10048R = new a();

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private boolean f10049N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private boolean f10050O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private d f10051P = f10048R;

    public static final class a implements d {
        a() {
        }

        @Override // n2.q.d
        public boolean a() {
            return d.a.f(this);
        }

        @Override // n2.q.d
        public void b(MotionEvent motionEvent) {
            d.a.d(this, motionEvent);
        }

        @Override // n2.q.d
        public boolean c(MotionEvent motionEvent) {
            return d.a.c(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean d(View view, MotionEvent motionEvent) {
            return d.a.e(this, view, motionEvent);
        }

        @Override // n2.q.d
        public boolean e() {
            return d.a.h(this);
        }

        @Override // n2.q.d
        public void f(MotionEvent motionEvent) {
            d.a.a(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean g(C0625d c0625d) {
            return d.a.g(this, c0625d);
        }

        @Override // n2.q.d
        public boolean h(View view) {
            return d.a.b(this, view);
        }
    }

    public static final class b {
        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean b(View view, MotionEvent motionEvent) {
            return (view instanceof ViewGroup) && ((ViewGroup) view).onInterceptTouchEvent(motionEvent);
        }

        private b() {
        }
    }

    private static final class c implements d {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final q f10052b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final C0478j f10053c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private float f10054d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private float f10055e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private int f10056f;

        public c(q qVar, C0478j c0478j) {
            D2.h.f(qVar, "handler");
            D2.h.f(c0478j, "editText");
            this.f10052b = qVar;
            this.f10053c = c0478j;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(c0478j.getContext());
            this.f10056f = viewConfiguration.getScaledTouchSlop() * viewConfiguration.getScaledTouchSlop();
        }

        @Override // n2.q.d
        public boolean a() {
            return true;
        }

        @Override // n2.q.d
        public void b(MotionEvent motionEvent) {
            D2.h.f(motionEvent, "event");
            this.f10052b.i();
            this.f10053c.onTouchEvent(motionEvent);
            this.f10054d = motionEvent.getX();
            this.f10055e = motionEvent.getY();
        }

        @Override // n2.q.d
        public boolean c(MotionEvent motionEvent) {
            return d.a.c(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean d(View view, MotionEvent motionEvent) {
            return d.a.e(this, view, motionEvent);
        }

        @Override // n2.q.d
        public boolean e() {
            return true;
        }

        @Override // n2.q.d
        public void f(MotionEvent motionEvent) {
            D2.h.f(motionEvent, "event");
            if (((motionEvent.getX() - this.f10054d) * (motionEvent.getX() - this.f10054d)) + ((motionEvent.getY() - this.f10055e) * (motionEvent.getY() - this.f10055e)) < this.f10056f) {
                this.f10053c.S();
            }
        }

        @Override // n2.q.d
        public Boolean g(C0625d c0625d) {
            D2.h.f(c0625d, "handler");
            return Boolean.valueOf(c0625d.R() > 0 && !(c0625d instanceof q));
        }

        @Override // n2.q.d
        public boolean h(View view) {
            return d.a.b(this, view);
        }
    }

    public interface d {

        public static final class a {
            public static void a(d dVar, MotionEvent motionEvent) {
                D2.h.f(motionEvent, "event");
            }

            public static boolean b(d dVar, View view) {
                D2.h.f(view, "view");
                return view.isPressed();
            }

            public static boolean c(d dVar, MotionEvent motionEvent) {
                D2.h.f(motionEvent, "event");
                return true;
            }

            public static void d(d dVar, MotionEvent motionEvent) {
                D2.h.f(motionEvent, "event");
            }

            public static Boolean e(d dVar, View view, MotionEvent motionEvent) {
                D2.h.f(motionEvent, "event");
                if (view != null) {
                    return Boolean.valueOf(view.onTouchEvent(motionEvent));
                }
                return null;
            }

            public static boolean f(d dVar) {
                return false;
            }

            public static Boolean g(d dVar, C0625d c0625d) {
                D2.h.f(c0625d, "handler");
                return null;
            }

            public static boolean h(d dVar) {
                return false;
            }
        }

        boolean a();

        void b(MotionEvent motionEvent);

        boolean c(MotionEvent motionEvent);

        Boolean d(View view, MotionEvent motionEvent);

        boolean e();

        void f(MotionEvent motionEvent);

        Boolean g(C0625d c0625d);

        boolean h(View view);
    }

    private static final class e implements d {
        @Override // n2.q.d
        public boolean a() {
            return d.a.f(this);
        }

        @Override // n2.q.d
        public void b(MotionEvent motionEvent) {
            d.a.d(this, motionEvent);
        }

        @Override // n2.q.d
        public boolean c(MotionEvent motionEvent) {
            return d.a.c(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean d(View view, MotionEvent motionEvent) {
            D2.h.f(motionEvent, "event");
            if (view != null) {
                return Boolean.valueOf(view.dispatchTouchEvent(motionEvent));
            }
            return null;
        }

        @Override // n2.q.d
        public boolean e() {
            return d.a.h(this);
        }

        @Override // n2.q.d
        public void f(MotionEvent motionEvent) {
            d.a.a(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean g(C0625d c0625d) {
            return d.a.g(this, c0625d);
        }

        @Override // n2.q.d
        public boolean h(View view) {
            return d.a.b(this, view);
        }
    }

    private static final class f implements d {
        @Override // n2.q.d
        public boolean a() {
            return true;
        }

        @Override // n2.q.d
        public void b(MotionEvent motionEvent) {
            d.a.d(this, motionEvent);
        }

        @Override // n2.q.d
        public boolean c(MotionEvent motionEvent) {
            return d.a.c(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean d(View view, MotionEvent motionEvent) {
            return d.a.e(this, view, motionEvent);
        }

        @Override // n2.q.d
        public boolean e() {
            return d.a.h(this);
        }

        @Override // n2.q.d
        public void f(MotionEvent motionEvent) {
            d.a.a(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean g(C0625d c0625d) {
            return d.a.g(this, c0625d);
        }

        @Override // n2.q.d
        public boolean h(View view) {
            return d.a.b(this, view);
        }
    }

    private static final class g implements d {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final q f10057b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final com.facebook.react.views.swiperefresh.a f10058c;

        public g(q qVar, com.facebook.react.views.swiperefresh.a aVar) {
            D2.h.f(qVar, "handler");
            D2.h.f(aVar, "swipeRefreshLayout");
            this.f10057b = qVar;
            this.f10058c = aVar;
        }

        @Override // n2.q.d
        public boolean a() {
            return d.a.f(this);
        }

        @Override // n2.q.d
        public void b(MotionEvent motionEvent) {
            ArrayList<C0625d> arrayListS;
            D2.h.f(motionEvent, "event");
            View childAt = this.f10058c.getChildAt(0);
            c0625d = null;
            ScrollView scrollView = childAt instanceof ScrollView ? (ScrollView) childAt : null;
            if (scrollView == null) {
                return;
            }
            i iVarN = this.f10057b.N();
            if (iVarN != null && (arrayListS = iVarN.s(scrollView)) != null) {
                for (C0625d c0625d : arrayListS) {
                    if (c0625d instanceof q) {
                    }
                }
                throw new NoSuchElementException("Collection contains no element matching the predicate.");
            }
            if (c0625d == null || c0625d.Q() != 4 || scrollView.getScrollY() <= 0) {
                return;
            }
            this.f10057b.B();
        }

        @Override // n2.q.d
        public boolean c(MotionEvent motionEvent) {
            return d.a.c(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean d(View view, MotionEvent motionEvent) {
            return d.a.e(this, view, motionEvent);
        }

        @Override // n2.q.d
        public boolean e() {
            return true;
        }

        @Override // n2.q.d
        public void f(MotionEvent motionEvent) {
            d.a.a(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean g(C0625d c0625d) {
            return d.a.g(this, c0625d);
        }

        @Override // n2.q.d
        public boolean h(View view) {
            return d.a.b(this, view);
        }
    }

    private static final class h implements d {
        @Override // n2.q.d
        public boolean a() {
            return d.a.f(this);
        }

        @Override // n2.q.d
        public void b(MotionEvent motionEvent) {
            d.a.d(this, motionEvent);
        }

        @Override // n2.q.d
        public boolean c(MotionEvent motionEvent) {
            return d.a.c(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean d(View view, MotionEvent motionEvent) {
            return d.a.e(this, view, motionEvent);
        }

        @Override // n2.q.d
        public boolean e() {
            return d.a.h(this);
        }

        @Override // n2.q.d
        public void f(MotionEvent motionEvent) {
            d.a.a(this, motionEvent);
        }

        @Override // n2.q.d
        public Boolean g(C0625d c0625d) {
            D2.h.f(c0625d, "handler");
            return Boolean.FALSE;
        }

        @Override // n2.q.d
        public boolean h(View view) {
            D2.h.f(view, "view");
            return view instanceof com.facebook.react.views.text.m;
        }
    }

    public q() {
        E0(true);
    }

    @Override // n2.C0625d
    public boolean I0(C0625d c0625d) {
        D2.h.f(c0625d, "handler");
        return !this.f10050O;
    }

    @Override // n2.C0625d
    public boolean J0(C0625d c0625d) {
        D2.h.f(c0625d, "handler");
        Boolean boolG = this.f10051P.g(c0625d);
        if (boolG != null) {
            return boolG.booleanValue();
        }
        if (super.J0(c0625d)) {
            return true;
        }
        if (c0625d instanceof q) {
            q qVar = (q) c0625d;
            if (qVar.Q() == 4 && qVar.f10050O) {
                return false;
            }
        }
        boolean z3 = this.f10050O;
        return !(Q() == 4 && c0625d.Q() == 4 && !z3) && Q() == 4 && !z3 && (!this.f10051P.a() || c0625d.R() > 0);
    }

    public final boolean S0() {
        return this.f10050O;
    }

    public final q T0(boolean z3) {
        this.f10050O = z3;
        return this;
    }

    public final q U0(boolean z3) {
        this.f10049N = z3;
        return this;
    }

    @Override // n2.C0625d
    protected void g0() {
        long jUptimeMillis = SystemClock.uptimeMillis();
        MotionEvent motionEventObtain = MotionEvent.obtain(jUptimeMillis, jUptimeMillis, 3, 0.0f, 0.0f, 0);
        motionEventObtain.setAction(3);
        d dVar = this.f10051P;
        View viewU = U();
        D2.h.c(motionEventObtain);
        dVar.d(viewU, motionEventObtain);
        motionEventObtain.recycle();
    }

    @Override // n2.C0625d
    protected void h0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
        View viewU = U();
        D2.h.c(viewU);
        Context context = viewU.getContext();
        D2.h.e(context, "getContext(...)");
        boolean zC = com.swmansion.gesturehandler.react.a.c(context);
        if ((viewU instanceof RNGestureHandlerButtonViewManager.a) && zC) {
            return;
        }
        if (motionEvent.getActionMasked() == 1) {
            if (Q() != 0 || this.f10051P.c(motionEvent)) {
                this.f10051P.d(viewU, motionEvent);
                if ((Q() == 0 || Q() == 2) && this.f10051P.h(viewU)) {
                    i();
                }
                if (Q() == 0) {
                    o();
                } else {
                    z();
                }
            } else {
                o();
            }
            this.f10051P.f(motionEvent);
            return;
        }
        if (Q() != 0 && Q() != 2) {
            if (Q() == 4) {
                this.f10051P.d(viewU, motionEvent);
                return;
            }
            return;
        }
        if (this.f10049N) {
            f10047Q.b(viewU, motionEvent);
            this.f10051P.d(viewU, motionEvent);
            i();
        } else if (f10047Q.b(viewU, motionEvent)) {
            this.f10051P.d(viewU, motionEvent);
            i();
        } else if (this.f10051P.e()) {
            this.f10051P.b(motionEvent);
        } else {
            if (Q() == 2 || !this.f10051P.c(motionEvent)) {
                return;
            }
            n();
        }
    }

    @Override // n2.C0625d
    protected void j0() {
        KeyEvent.Callback callbackU = U();
        if (callbackU instanceof d) {
            this.f10051P = (d) callbackU;
            return;
        }
        if (callbackU instanceof C0478j) {
            this.f10051P = new c(this, (C0478j) callbackU);
            return;
        }
        if (callbackU instanceof com.facebook.react.views.swiperefresh.a) {
            this.f10051P = new g(this, (com.facebook.react.views.swiperefresh.a) callbackU);
            return;
        }
        if (callbackU instanceof com.facebook.react.views.scroll.g) {
            this.f10051P = new f();
            return;
        }
        if (callbackU instanceof com.facebook.react.views.scroll.f) {
            this.f10051P = new f();
        } else if (callbackU instanceof com.facebook.react.views.text.m) {
            this.f10051P = new h();
        } else if (callbackU instanceof com.facebook.react.views.view.g) {
            this.f10051P = new e();
        }
    }

    @Override // n2.C0625d
    protected void k0() {
        this.f10051P = f10048R;
    }

    @Override // n2.C0625d
    public void o0() {
        super.o0();
        this.f10049N = false;
        this.f10050O = false;
    }
}
