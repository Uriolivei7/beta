package n2;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import n2.y;

/* JADX INFO: loaded from: classes.dex */
public final class u extends C0625d {

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private double f10090N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private double f10091O;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private y f10094R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private float f10095S;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    private float f10096T;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private float f10092P = Float.NaN;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private float f10093Q = Float.NaN;

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    private final y.b f10097U = new a();

    public static final class a implements y.b {
        a() {
            u.this.E0(false);
        }

        @Override // n2.y.b
        public boolean a(y yVar) {
            D2.h.f(yVar, "detector");
            u.this.f10095S = yVar.d();
            return true;
        }

        @Override // n2.y.b
        public boolean b(y yVar) {
            D2.h.f(yVar, "detector");
            double dZ0 = u.this.Z0();
            u uVar = u.this;
            uVar.f10090N = uVar.Z0() * ((double) yVar.g());
            double dI = yVar.i();
            if (dI > 0.0d) {
                u uVar2 = u.this;
                uVar2.f10091O = (uVar2.Z0() - dZ0) / dI;
            }
            if (Math.abs(u.this.f10095S - yVar.d()) < u.this.f10096T || u.this.Q() != 2) {
                return true;
            }
            u.this.i();
            return true;
        }

        @Override // n2.y.b
        public void c(y yVar) {
            D2.h.f(yVar, "detector");
        }
    }

    public final float X0() {
        return this.f10092P;
    }

    public final float Y0() {
        return this.f10093Q;
    }

    public final double Z0() {
        return this.f10090N;
    }

    public final double a1() {
        return this.f10091O;
    }

    @Override // n2.C0625d
    protected void h0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
        if (Q() == 0) {
            View viewU = U();
            D2.h.c(viewU);
            Context context = viewU.getContext();
            p0();
            this.f10094R = new y(context, this.f10097U);
            this.f10096T = ViewConfiguration.get(context).getScaledTouchSlop();
            this.f10092P = motionEvent.getX();
            this.f10093Q = motionEvent.getY();
            n();
        }
        y yVar = this.f10094R;
        if (yVar != null) {
            yVar.k(motionEvent2);
        }
        y yVar2 = this.f10094R;
        if (yVar2 != null) {
            PointF pointFO0 = O0(new PointF(yVar2.e(), yVar2.f()));
            this.f10092P = pointFO0.x;
            this.f10093Q = pointFO0.y;
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
        this.f10094R = null;
        this.f10092P = Float.NaN;
        this.f10093Q = Float.NaN;
        p0();
    }

    @Override // n2.C0625d
    public void p0() {
        this.f10091O = 0.0d;
        this.f10090N = 1.0d;
    }
}
