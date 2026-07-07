package n2;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.swmansion.gesturehandler.react.j;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class m extends C0625d {

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    public static final a f10030Q = new a(null);

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private static final com.swmansion.gesturehandler.react.n f10031R = new com.swmansion.gesturehandler.react.n();

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private Handler f10032N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private Runnable f10033O = new Runnable() { // from class: n2.l
        @Override // java.lang.Runnable
        public final void run() {
            m.U0(this.f10029b);
        }
    };

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private z f10034P = new z(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 31, null);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    private final void T0() {
        int iQ = Q();
        if (iQ == 0) {
            o();
        } else if (iQ == 2) {
            B();
        } else {
            if (iQ != 4) {
                return;
            }
            z();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void U0(m mVar) {
        mVar.T0();
    }

    private final boolean W0(C0625d c0625d) {
        View viewU = c0625d.U();
        while (viewU != null) {
            if (D2.h.b(viewU, U())) {
                return true;
            }
            Object parent = viewU.getParent();
            viewU = parent instanceof View ? (View) parent : null;
        }
        return false;
    }

    private final Boolean X0(View view, View view2, View view3) {
        if (D2.h.b(view3, view2)) {
            return Boolean.TRUE;
        }
        if (D2.h.b(view3, view)) {
            return Boolean.FALSE;
        }
        if (!(view3 instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view3;
        int childCount = viewGroup.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            Boolean boolX0 = X0(view, view2, f10031R.b(viewGroup, i3));
            if (boolX0 != null) {
                return boolX0;
            }
        }
        return null;
    }

    static /* synthetic */ Boolean Y0(m mVar, View view, View view2, View view3, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            view3 = view.getRootView();
        }
        return mVar.X0(view, view2, view3);
    }

    @Override // n2.C0625d
    public boolean I0(C0625d c0625d) {
        D2.h.f(c0625d, "handler");
        if (c0625d instanceof m) {
            m mVar = (m) c0625d;
            if (!mVar.W0(this)) {
                View viewU = mVar.U();
                D2.h.c(viewU);
                View viewU2 = U();
                D2.h.c(viewU2);
                Boolean boolY0 = Y0(this, viewU, viewU2, null, 4, null);
                D2.h.c(boolY0);
                return boolY0.booleanValue();
            }
        }
        return super.I0(c0625d);
    }

    @Override // n2.C0625d
    public boolean J0(C0625d c0625d) {
        D2.h.f(c0625d, "handler");
        if (((c0625d instanceof m) && (W0(c0625d) || ((m) c0625d).W0(this))) || (c0625d instanceof j.b)) {
            return true;
        }
        return super.J0(c0625d);
    }

    @Override // n2.C0625d
    public boolean K0(C0625d c0625d) {
        D2.h.f(c0625d, "handler");
        if ((c0625d instanceof m) && !W0(c0625d)) {
            m mVar = (m) c0625d;
            if (!mVar.W0(this)) {
                View viewU = U();
                D2.h.c(viewU);
                View viewU2 = mVar.U();
                D2.h.c(viewU2);
                Boolean boolY0 = Y0(this, viewU, viewU2, null, 4, null);
                if (boolY0 != null) {
                    return boolY0.booleanValue();
                }
            }
        }
        return super.K0(c0625d);
    }

    public final z V0() {
        return this.f10034P;
    }

    @Override // n2.C0625d
    protected void h0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
        if (motionEvent.getAction() == 0) {
            Handler handler = this.f10032N;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            this.f10032N = null;
            return;
        }
        if (motionEvent.getAction() != 1 || c0()) {
            return;
        }
        T0();
    }

    @Override // n2.C0625d
    protected void i0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
        if (motionEvent.getAction() == 10) {
            if (this.f10032N == null) {
                this.f10032N = new Handler(Looper.getMainLooper());
            }
            Handler handler = this.f10032N;
            D2.h.c(handler);
            handler.postDelayed(this.f10033O, 4L);
            return;
        }
        if (!c0()) {
            T0();
            return;
        }
        if (Q() == 4 && motionEvent.getToolType(0) == 2) {
            this.f10034P = z.f10148f.a(motionEvent);
            return;
        }
        if (Q() == 0) {
            if (motionEvent.getAction() == 7 || motionEvent.getAction() == 9) {
                n();
                i();
            }
        }
    }

    @Override // n2.C0625d
    protected void k0() {
        super.k0();
        this.f10034P = new z(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 31, null);
    }
}
