package n2;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import n2.C0621C;

/* JADX INFO: renamed from: n2.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0623b extends C0625d {

    /* JADX INFO: renamed from: V, reason: collision with root package name */
    public static final a f9952V = new a(null);

    /* JADX INFO: renamed from: W, reason: collision with root package name */
    private static final double f9953W;

    /* JADX INFO: renamed from: X, reason: collision with root package name */
    private static final double f9954X;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private Handler f9959R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private int f9960S;

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    private VelocityTracker f9962U;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private int f9955N = 1;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private int f9956O = 1;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private final long f9957P = 800;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private final long f9958Q = 2000;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    private final Runnable f9961T = new Runnable() { // from class: n2.a
        @Override // java.lang.Runnable
        public final void run() {
            C0623b.V0(this.f9951b);
        }
    };

    /* JADX INFO: renamed from: n2.b$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    static {
        k kVar = k.f10028a;
        f9953W = kVar.a(30.0d);
        f9954X = kVar.a(60.0d);
    }

    private final void T0(VelocityTracker velocityTracker, MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        D2.h.c(velocityTracker);
        velocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }

    private final void U0(MotionEvent motionEvent) {
        if (Z0(motionEvent)) {
            return;
        }
        B();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void V0(C0623b c0623b) {
        c0623b.B();
    }

    private final void Y0(MotionEvent motionEvent) {
        this.f9962U = VelocityTracker.obtain();
        n();
        this.f9960S = 1;
        Handler handler = this.f9959R;
        if (handler == null) {
            this.f9959R = new Handler(Looper.getMainLooper());
        } else {
            D2.h.c(handler);
            handler.removeCallbacksAndMessages(null);
        }
        Handler handler2 = this.f9959R;
        D2.h.c(handler2);
        handler2.postDelayed(this.f9961T, this.f9957P);
    }

    private final boolean Z0(MotionEvent motionEvent) {
        boolean z3;
        boolean z4;
        T0(this.f9962U, motionEvent);
        C0621C.a aVar = C0621C.f9936f;
        VelocityTracker velocityTracker = this.f9962U;
        D2.h.c(velocityTracker);
        C0621C c0621cB = aVar.b(velocityTracker);
        Integer[] numArr = {2, 1, 4, 8};
        ArrayList arrayList = new ArrayList(4);
        for (int i3 = 0; i3 < 4; i3++) {
            arrayList.add(Boolean.valueOf(a1(this, c0621cB, numArr[i3].intValue(), f9953W)));
        }
        Integer[] numArr2 = {5, 9, 6, 10};
        ArrayList arrayList2 = new ArrayList(4);
        for (int i4 = 0; i4 < 4; i4++) {
            arrayList2.add(Boolean.valueOf(a1(this, c0621cB, numArr2[i4].intValue(), f9954X)));
        }
        if (arrayList.isEmpty()) {
            z3 = false;
        } else {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((Boolean) it.next()).booleanValue()) {
                    z3 = true;
                    break;
                }
            }
            z3 = false;
        }
        if (arrayList2.isEmpty()) {
            z4 = false;
        } else {
            Iterator it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                if (((Boolean) it2.next()).booleanValue()) {
                    z4 = true;
                    break;
                }
            }
            z4 = false;
        }
        boolean z5 = z3 | z4;
        boolean z6 = c0621cB.k() > ((double) this.f9958Q);
        if (this.f9960S != this.f9955N || !z5 || !z6) {
            return false;
        }
        Handler handler = this.f9959R;
        D2.h.c(handler);
        handler.removeCallbacksAndMessages(null);
        i();
        return true;
    }

    private static final boolean a1(C0623b c0623b, C0621C c0621c, int i3, double d4) {
        return (c0623b.f9956O & i3) == i3 && c0621c.l(C0621C.f9936f.a(i3), d4);
    }

    public final void W0(int i3) {
        this.f9956O = i3;
    }

    public final void X0(int i3) {
        this.f9955N = i3;
    }

    @Override // n2.C0625d
    protected void g0() {
        Handler handler = this.f9959R;
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
            if (iQ == 0) {
                Y0(motionEvent2);
            }
            if (iQ == 2) {
                Z0(motionEvent2);
                if (motionEvent2.getPointerCount() > this.f9960S) {
                    this.f9960S = motionEvent2.getPointerCount();
                }
                if (motionEvent2.getActionMasked() == 1) {
                    U0(motionEvent2);
                }
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
        VelocityTracker velocityTracker = this.f9962U;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.f9962U = null;
        Handler handler = this.f9959R;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override // n2.C0625d
    public void o0() {
        super.o0();
        this.f9955N = 1;
        this.f9956O = 1;
    }
}
