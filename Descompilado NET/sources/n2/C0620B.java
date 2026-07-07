package n2;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: n2.B, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0620B extends C0625d {

    /* JADX INFO: renamed from: e0, reason: collision with root package name */
    public static final a f9918e0 = new a(null);

    /* JADX INFO: renamed from: V, reason: collision with root package name */
    private float f9927V;

    /* JADX INFO: renamed from: W, reason: collision with root package name */
    private float f9928W;

    /* JADX INFO: renamed from: X, reason: collision with root package name */
    private float f9929X;

    /* JADX INFO: renamed from: Y, reason: collision with root package name */
    private float f9930Y;

    /* JADX INFO: renamed from: Z, reason: collision with root package name */
    private float f9931Z;

    /* JADX INFO: renamed from: a0, reason: collision with root package name */
    private float f9932a0;

    /* JADX INFO: renamed from: b0, reason: collision with root package name */
    private Handler f9933b0;

    /* JADX INFO: renamed from: c0, reason: collision with root package name */
    private int f9934c0;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private float f9919N = Float.MIN_VALUE;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private float f9920O = Float.MIN_VALUE;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private float f9921P = Float.MIN_VALUE;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private long f9922Q = 500;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private long f9923R = 200;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private int f9924S = 1;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    private int f9925T = 1;

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    private int f9926U = 1;

    /* JADX INFO: renamed from: d0, reason: collision with root package name */
    private final Runnable f9935d0 = new Runnable() { // from class: n2.A
        @Override // java.lang.Runnable
        public final void run() {
            C0620B.U0(this.f9917b);
        }
    };

    /* JADX INFO: renamed from: n2.B$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public C0620B() {
        E0(true);
    }

    private final void T0() {
        Handler handler = this.f9933b0;
        if (handler == null) {
            this.f9933b0 = new Handler(Looper.getMainLooper());
        } else {
            D2.h.c(handler);
            handler.removeCallbacksAndMessages(null);
        }
        int i3 = this.f9934c0 + 1;
        this.f9934c0 = i3;
        if (i3 == this.f9924S && this.f9926U >= this.f9925T) {
            i();
            return;
        }
        Handler handler2 = this.f9933b0;
        D2.h.c(handler2);
        handler2.postDelayed(this.f9935d0, this.f9923R);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void U0(C0620B c0620b) {
        c0620b.B();
    }

    private final boolean c1() {
        float f3 = (this.f9931Z - this.f9927V) + this.f9929X;
        if (this.f9919N != Float.MIN_VALUE && Math.abs(f3) > this.f9919N) {
            return true;
        }
        float f4 = (this.f9932a0 - this.f9928W) + this.f9930Y;
        if (this.f9920O != Float.MIN_VALUE && Math.abs(f4) > this.f9920O) {
            return true;
        }
        float f5 = (f4 * f4) + (f3 * f3);
        float f6 = this.f9921P;
        return f6 != Float.MIN_VALUE && f5 > f6;
    }

    private final void d1() {
        Handler handler = this.f9933b0;
        if (handler == null) {
            this.f9933b0 = new Handler(Looper.getMainLooper());
        } else {
            D2.h.c(handler);
            handler.removeCallbacksAndMessages(null);
        }
        Handler handler2 = this.f9933b0;
        D2.h.c(handler2);
        handler2.postDelayed(this.f9935d0, this.f9922Q);
    }

    public final C0620B V0(long j3) {
        this.f9923R = j3;
        return this;
    }

    public final C0620B W0(float f3) {
        this.f9921P = f3 * f3;
        return this;
    }

    public final C0620B X0(long j3) {
        this.f9922Q = j3;
        return this;
    }

    public final C0620B Y0(float f3) {
        this.f9919N = f3;
        return this;
    }

    public final C0620B Z0(float f3) {
        this.f9920O = f3;
        return this;
    }

    public final C0620B a1(int i3) {
        this.f9925T = i3;
        return this;
    }

    public final C0620B b1(int i3) {
        this.f9924S = i3;
        return this;
    }

    @Override // n2.C0625d
    protected void g0() {
        Handler handler = this.f9933b0;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override // n2.C0625d
    protected void h0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
        if (H0(motionEvent2)) {
            int iQ = Q();
            int actionMasked = motionEvent2.getActionMasked();
            if (iQ == 0) {
                this.f9929X = 0.0f;
                this.f9930Y = 0.0f;
                k kVar = k.f10028a;
                this.f9927V = kVar.b(motionEvent2, true);
                this.f9928W = kVar.c(motionEvent2, true);
            }
            if (actionMasked == 5 || actionMasked == 6) {
                this.f9929X += this.f9931Z - this.f9927V;
                this.f9930Y += this.f9932a0 - this.f9928W;
                k kVar2 = k.f10028a;
                this.f9931Z = kVar2.b(motionEvent2, true);
                float fC = kVar2.c(motionEvent2, true);
                this.f9932a0 = fC;
                this.f9927V = this.f9931Z;
                this.f9928W = fC;
            } else {
                k kVar3 = k.f10028a;
                this.f9931Z = kVar3.b(motionEvent2, true);
                this.f9932a0 = kVar3.c(motionEvent2, true);
            }
            if (this.f9926U < motionEvent2.getPointerCount()) {
                this.f9926U = motionEvent2.getPointerCount();
            }
            if (c1()) {
                B();
                return;
            }
            if (iQ == 0) {
                if (actionMasked == 0 || actionMasked == 11) {
                    n();
                }
                d1();
                return;
            }
            if (iQ == 2) {
                if (actionMasked != 0) {
                    if (actionMasked != 1) {
                        if (actionMasked != 11) {
                            if (actionMasked != 12) {
                                return;
                            }
                        }
                    }
                    T0();
                    return;
                }
                d1();
            }
        }
    }

    @Override // n2.C0625d
    public void j(boolean z3) {
        super.j(z3);
        z();
    }

    @Override // n2.C0625d
    protected void k0() {
        this.f9934c0 = 0;
        this.f9926U = 0;
        Handler handler = this.f9933b0;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override // n2.C0625d
    public void o0() {
        super.o0();
        this.f9919N = Float.MIN_VALUE;
        this.f9920O = Float.MIN_VALUE;
        this.f9921P = Float.MIN_VALUE;
        this.f9922Q = 500L;
        this.f9923R = 200L;
        this.f9924S = 1;
        this.f9925T = 1;
    }
}
