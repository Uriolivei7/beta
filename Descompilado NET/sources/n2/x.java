package n2;

import android.graphics.PointF;
import android.view.MotionEvent;
import kotlin.jvm.internal.DefaultConstructorMarker;
import n2.w;

/* JADX INFO: loaded from: classes.dex */
public final class x extends C0625d {

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    public static final a f10115T = new a(null);

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private w f10116N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private double f10117O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private double f10118P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private float f10119Q = Float.NaN;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private float f10120R = Float.NaN;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private final w.a f10121S;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public static final class b implements w.a {
        b() {
        }

        @Override // n2.w.a
        public boolean a(w wVar) {
            D2.h.f(wVar, "detector");
            double dW0 = x.this.W0();
            x xVar = x.this;
            xVar.f10117O = xVar.W0() + wVar.d();
            long jE = wVar.e();
            if (jE > 0) {
                x xVar2 = x.this;
                xVar2.f10118P = (xVar2.W0() - dW0) / jE;
            }
            if (Math.abs(x.this.W0()) < 0.08726646259971647d || x.this.Q() != 2) {
                return true;
            }
            x.this.i();
            return true;
        }

        @Override // n2.w.a
        public boolean b(w wVar) {
            D2.h.f(wVar, "detector");
            return true;
        }

        @Override // n2.w.a
        public void c(w wVar) {
            D2.h.f(wVar, "detector");
            x.this.z();
        }
    }

    public x() {
        E0(false);
        this.f10121S = new b();
    }

    public final float U0() {
        return this.f10119Q;
    }

    public final float V0() {
        return this.f10120R;
    }

    public final double W0() {
        return this.f10117O;
    }

    public final double X0() {
        return this.f10118P;
    }

    @Override // n2.C0625d
    protected void h0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
        if (Q() == 0) {
            p0();
            this.f10116N = new w(this.f10121S);
            this.f10119Q = motionEvent.getX();
            this.f10120R = motionEvent.getY();
            n();
        }
        w wVar = this.f10116N;
        if (wVar != null) {
            wVar.f(motionEvent2);
        }
        w wVar2 = this.f10116N;
        if (wVar2 != null) {
            PointF pointFO0 = O0(new PointF(wVar2.b(), wVar2.c()));
            this.f10119Q = pointFO0.x;
            this.f10120R = pointFO0.y;
        }
        if (motionEvent2.getActionMasked() == 1) {
            if (Q() == 4) {
                z();
            } else {
                B();
            }
        }
    }

    @Override // n2.C0625d
    public void j(boolean z3) {
        if (Q() != 4) {
            p0();
        }
        super.j(z3);
    }

    @Override // n2.C0625d
    protected void k0() {
        this.f10116N = null;
        this.f10119Q = Float.NaN;
        this.f10120R = Float.NaN;
        p0();
    }

    @Override // n2.C0625d
    public void p0() {
        this.f10118P = 0.0d;
        this.f10117O = 0.0d;
    }
}
