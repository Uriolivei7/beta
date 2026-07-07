package n2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0686i;
import s2.AbstractC0695C;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class o extends C0625d {

    /* JADX INFO: renamed from: X, reason: collision with root package name */
    public static final a f10036X = new a(null);

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private long f10037N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private final float f10038O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private float f10039P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private int f10040Q;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private float f10041R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private float f10042S;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    private long f10043T;

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    private long f10044U;

    /* JADX INFO: renamed from: V, reason: collision with root package name */
    private Handler f10045V;

    /* JADX INFO: renamed from: W, reason: collision with root package name */
    private int f10046W;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public o(Context context) {
        D2.h.f(context, "context");
        this.f10037N = 500L;
        E0(true);
        float f3 = context.getResources().getDisplayMetrics().density * 10.0f;
        float f4 = f3 * f3;
        this.f10038O = f4;
        this.f10039P = f4;
        this.f10040Q = 1;
    }

    private final C0686i T0(MotionEvent motionEvent, boolean z3) {
        if (z3) {
            int pointerCount = motionEvent.getPointerCount();
            float x3 = 0.0f;
            float y3 = 0.0f;
            for (int i3 = 0; i3 < pointerCount; i3++) {
                if (i3 != motionEvent.getActionIndex()) {
                    x3 += motionEvent.getX(i3);
                    y3 += motionEvent.getY(i3);
                }
            }
            return new C0686i(Float.valueOf(x3 / (motionEvent.getPointerCount() - 1)), Float.valueOf(y3 / (motionEvent.getPointerCount() - 1)));
        }
        H2.c cVarI = H2.d.i(0, motionEvent.getPointerCount());
        ArrayList arrayList = new ArrayList(AbstractC0717n.q(cVarI, 10));
        Iterator it = cVarI.iterator();
        while (it.hasNext()) {
            arrayList.add(Float.valueOf(motionEvent.getX(((AbstractC0695C) it).a())));
        }
        float fI = (float) AbstractC0717n.I(arrayList);
        H2.c cVarI2 = H2.d.i(0, motionEvent.getPointerCount());
        ArrayList arrayList2 = new ArrayList(AbstractC0717n.q(cVarI2, 10));
        Iterator it2 = cVarI2.iterator();
        while (it2.hasNext()) {
            arrayList2.add(Float.valueOf(motionEvent.getY(((AbstractC0695C) it2).a())));
        }
        return new C0686i(Float.valueOf(fI), Float.valueOf((float) AbstractC0717n.I(arrayList2)));
    }

    static /* synthetic */ C0686i U0(o oVar, MotionEvent motionEvent, boolean z3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z3 = false;
        }
        return oVar.T0(motionEvent, z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void W0(o oVar) {
        oVar.i();
    }

    public final int V0() {
        return (int) (this.f10044U - this.f10043T);
    }

    public final o X0(float f3) {
        this.f10039P = f3 * f3;
        return this;
    }

    public final void Y0(long j3) {
        this.f10037N = j3;
    }

    public final o Z0(int i3) {
        this.f10040Q = i3;
        return this;
    }

    @Override // n2.C0625d
    protected void h0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
        if (H0(motionEvent2)) {
            if (Q() == 0) {
                long jUptimeMillis = SystemClock.uptimeMillis();
                this.f10044U = jUptimeMillis;
                this.f10043T = jUptimeMillis;
                n();
                C0686i c0686iU0 = U0(this, motionEvent2, false, 2, null);
                float fFloatValue = ((Number) c0686iU0.a()).floatValue();
                float fFloatValue2 = ((Number) c0686iU0.b()).floatValue();
                this.f10041R = fFloatValue;
                this.f10042S = fFloatValue2;
                this.f10046W++;
            }
            if (motionEvent2.getActionMasked() == 5) {
                this.f10046W++;
                C0686i c0686iU02 = U0(this, motionEvent2, false, 2, null);
                float fFloatValue3 = ((Number) c0686iU02.a()).floatValue();
                float fFloatValue4 = ((Number) c0686iU02.b()).floatValue();
                this.f10041R = fFloatValue3;
                this.f10042S = fFloatValue4;
                if (this.f10046W > this.f10040Q) {
                    B();
                    this.f10046W = 0;
                }
            }
            if (Q() == 2 && this.f10046W == this.f10040Q && (motionEvent2.getActionMasked() == 0 || motionEvent2.getActionMasked() == 5)) {
                Handler handler = new Handler(Looper.getMainLooper());
                this.f10045V = handler;
                long j3 = this.f10037N;
                if (j3 > 0) {
                    D2.h.c(handler);
                    handler.postDelayed(new Runnable() { // from class: n2.n
                        @Override // java.lang.Runnable
                        public final void run() {
                            o.W0(this.f10035b);
                        }
                    }, this.f10037N);
                } else if (j3 == 0) {
                    i();
                }
            }
            if (motionEvent2.getActionMasked() == 1 || motionEvent2.getActionMasked() == 12) {
                this.f10046W--;
                Handler handler2 = this.f10045V;
                if (handler2 != null) {
                    handler2.removeCallbacksAndMessages(null);
                    this.f10045V = null;
                }
                if (Q() == 4) {
                    z();
                    return;
                } else {
                    B();
                    return;
                }
            }
            if (motionEvent2.getActionMasked() != 6) {
                C0686i c0686iU03 = U0(this, motionEvent2, false, 2, null);
                float fFloatValue5 = ((Number) c0686iU03.a()).floatValue();
                float fFloatValue6 = ((Number) c0686iU03.b()).floatValue();
                float f3 = fFloatValue5 - this.f10041R;
                float f4 = fFloatValue6 - this.f10042S;
                if ((f3 * f3) + (f4 * f4) > this.f10039P) {
                    if (Q() == 4) {
                        o();
                        return;
                    } else {
                        B();
                        return;
                    }
                }
                return;
            }
            int i3 = this.f10046W - 1;
            this.f10046W = i3;
            if (i3 < this.f10040Q && Q() != 4) {
                B();
                this.f10046W = 0;
                return;
            }
            C0686i c0686iT0 = T0(motionEvent2, true);
            float fFloatValue7 = ((Number) c0686iT0.a()).floatValue();
            float fFloatValue8 = ((Number) c0686iT0.b()).floatValue();
            this.f10041R = fFloatValue7;
            this.f10042S = fFloatValue8;
        }
    }

    @Override // n2.C0625d
    protected void k0() {
        super.k0();
        this.f10046W = 0;
    }

    @Override // n2.C0625d
    protected void l0(int i3, int i4) {
        Handler handler = this.f10045V;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.f10045V = null;
        }
    }

    @Override // n2.C0625d
    public void o0() {
        super.o0();
        this.f10037N = 500L;
        this.f10039P = this.f10038O;
    }

    @Override // n2.C0625d
    public void t(MotionEvent motionEvent) {
        D2.h.f(motionEvent, "event");
        this.f10044U = SystemClock.uptimeMillis();
        super.t(motionEvent);
    }

    @Override // n2.C0625d
    public void u(int i3, int i4) {
        this.f10044U = SystemClock.uptimeMillis();
        super.u(i3, i4);
    }
}
