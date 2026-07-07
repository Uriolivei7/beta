package n2;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import java.util.Arrays;
import kotlin.jvm.internal.DefaultConstructorMarker;
import m2.C0612f;
import s2.AbstractC0711h;

/* JADX INFO: renamed from: n2.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0625d {

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    public static final b f9964J = new b(null);

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private static MotionEvent.PointerProperties[] f9965K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    private static MotionEvent.PointerCoords[] f9966L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private static short f9967M;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private i f9968A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private r f9969B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private e f9970C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private int f9971D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private int f9972E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private int f9973F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private boolean f9974G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private boolean f9975H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private boolean f9976I;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int[] f9977a = new int[12];

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f9978b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int[] f9979c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f9980d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private View f9981e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f9982f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private float f9983g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private float f9984h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f9985i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f9986j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f9987k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private WritableArray f9988l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private WritableArray f9989m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f9990n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private int f9991o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final c[] f9992p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private boolean f9993q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private float[] f9994r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private short f9995s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private float f9996t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private float f9997u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private boolean f9998v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private float f9999w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private float f10000x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private boolean f10001y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private int f10002z;

    /* JADX INFO: renamed from: n2.d$a */
    public static final class a extends Exception {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(C0625d c0625d, MotionEvent motionEvent, IllegalArgumentException illegalArgumentException) {
            super(K2.o.f("\n    handler: " + D2.s.a(c0625d.getClass()).a() + "\n    state: " + c0625d.Q() + "\n    view: " + c0625d.U() + "\n    orchestrator: " + c0625d.N() + "\n    isEnabled: " + c0625d.b0() + "\n    isActive: " + c0625d.X() + "\n    isAwaiting: " + c0625d.Y() + "\n    trackedPointersCount: " + c0625d.f9978b + "\n    trackedPointers: " + AbstractC0711h.w(c0625d.f9977a, ", ", null, null, 0, null, null, 62, null) + "\n    while handling event: " + motionEvent + "\n    "), illegalArgumentException);
            D2.h.f(c0625d, "handler");
            D2.h.f(motionEvent, "event");
            D2.h.f(illegalArgumentException, "e");
        }
    }

    /* JADX INFO: renamed from: n2.d$b */
    public static final class b {
        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean c(float f3) {
            return !Float.isNaN(f3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void d(int i3) {
            if (C0625d.f9965K == null) {
                C0625d.f9965K = new MotionEvent.PointerProperties[12];
                C0625d.f9966L = new MotionEvent.PointerCoords[12];
            }
            while (i3 > 0) {
                MotionEvent.PointerProperties[] pointerPropertiesArr = C0625d.f9965K;
                MotionEvent.PointerCoords[] pointerCoordsArr = null;
                if (pointerPropertiesArr == null) {
                    D2.h.s("pointerProps");
                    pointerPropertiesArr = null;
                }
                int i4 = i3 - 1;
                if (pointerPropertiesArr[i4] != null) {
                    return;
                }
                MotionEvent.PointerProperties[] pointerPropertiesArr2 = C0625d.f9965K;
                if (pointerPropertiesArr2 == null) {
                    D2.h.s("pointerProps");
                    pointerPropertiesArr2 = null;
                }
                pointerPropertiesArr2[i4] = new MotionEvent.PointerProperties();
                MotionEvent.PointerCoords[] pointerCoordsArr2 = C0625d.f9966L;
                if (pointerCoordsArr2 == null) {
                    D2.h.s("pointerCoords");
                } else {
                    pointerCoordsArr = pointerCoordsArr2;
                }
                pointerCoordsArr[i4] = new MotionEvent.PointerCoords();
                i3--;
            }
        }

        private b() {
        }
    }

    /* JADX INFO: renamed from: n2.d$c */
    private static final class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f10003a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private float f10004b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private float f10005c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private float f10006d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private float f10007e;

        public c(int i3, float f3, float f4, float f5, float f6) {
            this.f10003a = i3;
            this.f10004b = f3;
            this.f10005c = f4;
            this.f10006d = f5;
            this.f10007e = f6;
        }

        public final float a() {
            return this.f10006d;
        }

        public final float b() {
            return this.f10007e;
        }

        public final int c() {
            return this.f10003a;
        }

        public final float d() {
            return this.f10004b;
        }

        public final float e() {
            return this.f10005c;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof c)) {
                return false;
            }
            c cVar = (c) obj;
            return this.f10003a == cVar.f10003a && Float.compare(this.f10004b, cVar.f10004b) == 0 && Float.compare(this.f10005c, cVar.f10005c) == 0 && Float.compare(this.f10006d, cVar.f10006d) == 0 && Float.compare(this.f10007e, cVar.f10007e) == 0;
        }

        public final void f(float f3) {
            this.f10006d = f3;
        }

        public final void g(float f3) {
            this.f10007e = f3;
        }

        public final void h(float f3) {
            this.f10004b = f3;
        }

        public int hashCode() {
            return (((((((Integer.hashCode(this.f10003a) * 31) + Float.hashCode(this.f10004b)) * 31) + Float.hashCode(this.f10005c)) * 31) + Float.hashCode(this.f10006d)) * 31) + Float.hashCode(this.f10007e);
        }

        public final void i(float f3) {
            this.f10005c = f3;
        }

        public String toString() {
            return "PointerData(pointerId=" + this.f10003a + ", x=" + this.f10004b + ", y=" + this.f10005c + ", absoluteX=" + this.f10006d + ", absoluteY=" + this.f10007e + ")";
        }
    }

    public C0625d() {
        int[] iArr = new int[2];
        for (int i3 = 0; i3 < 2; i3++) {
            iArr[i3] = 0;
        }
        this.f9979c = iArr;
        this.f9986j = true;
        c[] cVarArr = new c[12];
        for (int i4 = 0; i4 < 12; i4++) {
            cVarArr[i4] = null;
        }
        this.f9992p = cVarArr;
        this.f9971D = 3;
    }

    private final void A() {
        this.f9989m = null;
        for (c cVar : this.f9992p) {
            if (cVar != null) {
                m(cVar);
            }
        }
    }

    private final int C() {
        int[] iArr;
        int i3 = 0;
        while (i3 < this.f9978b) {
            int i4 = 0;
            while (true) {
                iArr = this.f9977a;
                if (i4 >= iArr.length || iArr[i4] == i3) {
                    break;
                }
                i4++;
            }
            if (i4 == iArr.length) {
                return i3;
            }
            i3++;
        }
        return i3;
    }

    private final void D0(MotionEvent motionEvent) {
        int toolType = motionEvent.getToolType(motionEvent.getActionIndex());
        int i3 = 1;
        if (toolType == 1) {
            i3 = 0;
        } else if (toolType != 2) {
            i3 = 3;
            if (toolType == 3) {
                i3 = 2;
            }
        }
        this.f9971D = i3;
    }

    private final Activity F(Context context) {
        if (context instanceof ReactContext) {
            return ((ReactContext) context).getCurrentActivity();
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return F(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    private final boolean Z(int i3) {
        int i4 = this.f9972E;
        return i4 == 0 ? i3 == 1 : (i3 & i4) != 0;
    }

    private final void e0(int i3) {
        UiThreadUtil.assertOnUiThread();
        if (this.f9982f == i3) {
            return;
        }
        if (this.f9991o > 0 && (i3 == 5 || i3 == 3 || i3 == 1)) {
            p();
        }
        int i4 = this.f9982f;
        this.f9982f = i3;
        if (i3 == 4) {
            short s3 = f9967M;
            f9967M = (short) (s3 + 1);
            this.f9995s = s3;
        }
        i iVar = this.f9968A;
        D2.h.c(iVar);
        iVar.A(this, i3, i4);
        l0(i3, i4);
    }

    private final boolean f0(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != this.f9978b) {
            return true;
        }
        int length = this.f9977a.length;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = this.f9977a[i3];
            if (i4 != -1 && i4 != i3) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0020  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final android.view.MotionEvent k(android.view.MotionEvent r26) throws n2.C0625d.a {
        /*
            Method dump skipped, instruction units count: 339
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: n2.C0625d.k(android.view.MotionEvent):android.view.MotionEvent");
    }

    private final void l(c cVar) {
        if (this.f9988l == null) {
            this.f9988l = Arguments.createArray();
        }
        WritableArray writableArray = this.f9988l;
        D2.h.c(writableArray);
        writableArray.pushMap(s(cVar));
    }

    private final void m(c cVar) {
        if (this.f9989m == null) {
            this.f9989m = Arguments.createArray();
        }
        WritableArray writableArray = this.f9989m;
        D2.h.c(writableArray);
        writableArray.pushMap(s(cVar));
    }

    private final void p() {
        this.f9990n = 4;
        this.f9988l = null;
        A();
        for (c cVar : this.f9992p) {
            if (cVar != null) {
                l(cVar);
            }
        }
        this.f9991o = 0;
        AbstractC0711h.k(this.f9992p, null, 0, 0, 6, null);
        w();
    }

    private final WritableMap s(c cVar) {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putInt("id", cVar.c());
        writableMapCreateMap.putDouble("x", C0429f0.f(cVar.d()));
        writableMapCreateMap.putDouble("y", C0429f0.f(cVar.e()));
        writableMapCreateMap.putDouble("absoluteX", C0429f0.f(cVar.a()));
        writableMapCreateMap.putDouble("absoluteY", C0429f0.f(cVar.b()));
        return writableMapCreateMap;
    }

    private final void v(MotionEvent motionEvent, MotionEvent motionEvent2) {
        this.f9988l = null;
        this.f9990n = 1;
        int pointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
        this.f9992p[pointerId] = new c(pointerId, motionEvent.getX(motionEvent.getActionIndex()), motionEvent.getY(motionEvent.getActionIndex()), (motionEvent2.getX(motionEvent.getActionIndex()) + (motionEvent2.getRawX() - motionEvent2.getX())) - this.f9979c[0], (motionEvent2.getY(motionEvent.getActionIndex()) + (motionEvent2.getRawY() - motionEvent2.getY())) - this.f9979c[1]);
        this.f9991o++;
        c cVar = this.f9992p[pointerId];
        D2.h.c(cVar);
        l(cVar);
        A();
        w();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void w0(C0625d c0625d) {
        c0625d.o();
    }

    private final void x(MotionEvent motionEvent, MotionEvent motionEvent2) {
        this.f9988l = null;
        this.f9990n = 2;
        float rawX = motionEvent2.getRawX() - motionEvent2.getX();
        float rawY = motionEvent2.getRawY() - motionEvent2.getY();
        int pointerCount = motionEvent.getPointerCount();
        int i3 = 0;
        for (int i4 = 0; i4 < pointerCount; i4++) {
            c cVar = this.f9992p[motionEvent.getPointerId(i4)];
            if (cVar != null && (cVar.d() != motionEvent.getX(i4) || cVar.e() != motionEvent.getY(i4))) {
                cVar.h(motionEvent.getX(i4));
                cVar.i(motionEvent.getY(i4));
                cVar.f((motionEvent2.getX(i4) + rawX) - this.f9979c[0]);
                cVar.g((motionEvent2.getY(i4) + rawY) - this.f9979c[1]);
                l(cVar);
                i3++;
            }
        }
        if (i3 > 0) {
            A();
            w();
        }
    }

    private final void y(MotionEvent motionEvent, MotionEvent motionEvent2) {
        A();
        this.f9988l = null;
        this.f9990n = 3;
        int pointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
        this.f9992p[pointerId] = new c(pointerId, motionEvent.getX(motionEvent.getActionIndex()), motionEvent.getY(motionEvent.getActionIndex()), (motionEvent2.getX(motionEvent.getActionIndex()) + (motionEvent2.getRawX() - motionEvent2.getX())) - this.f9979c[0], (motionEvent2.getY(motionEvent.getActionIndex()) + (motionEvent2.getRawY() - motionEvent2.getY())) - this.f9979c[1]);
        c cVar = this.f9992p[pointerId];
        D2.h.c(cVar);
        l(cVar);
        this.f9992p[pointerId] = null;
        this.f9991o--;
        w();
    }

    public final C0625d A0(int i3) {
        this.f9972E = i3;
        return this;
    }

    public final void B() {
        int i3 = this.f9982f;
        if (i3 == 4 || i3 == 0 || i3 == 2) {
            e0(1);
        }
    }

    public final void B0(boolean z3) {
        this.f9993q = z3;
    }

    public final C0625d C0(r rVar) {
        this.f9969B = rVar;
        return this;
    }

    public final int D() {
        return this.f9987k;
    }

    public final int E() {
        return this.f9973F;
    }

    public final C0625d E0(boolean z3) {
        C0625d c0625dQ0 = q0();
        c0625dQ0.f10001y = z3;
        return c0625dQ0;
    }

    public final void F0(boolean z3) {
        this.f9976I = z3;
    }

    public final short G() {
        return this.f9995s;
    }

    public final void G0(int i3) {
        this.f9980d = i3;
    }

    public final float H() {
        return (this.f9996t + this.f9999w) - this.f9979c[0];
    }

    protected final boolean H0(MotionEvent motionEvent) {
        D2.h.f(motionEvent, "sourceEvent");
        if (motionEvent.getToolType(0) == 3) {
            if (motionEvent.getAction() == 0 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6 || motionEvent.getAction() == 5 || !(motionEvent.getAction() == 2 || Z(motionEvent.getActionButton()))) {
                return false;
            }
            if (motionEvent.getAction() == 2 && !Z(motionEvent.getButtonState())) {
                return false;
            }
        }
        return true;
    }

    public final float I() {
        return (this.f9997u + this.f10000x) - this.f9979c[1];
    }

    public boolean I0(C0625d c0625d) {
        e eVar;
        D2.h.f(c0625d, "handler");
        if (c0625d == this || (eVar = this.f9970C) == null) {
            return false;
        }
        return eVar.b(this, c0625d);
    }

    public final float J() {
        return this.f9996t;
    }

    public boolean J0(C0625d c0625d) {
        D2.h.f(c0625d, "handler");
        if (c0625d == this) {
            return true;
        }
        e eVar = this.f9970C;
        if (eVar != null) {
            return eVar.c(this, c0625d);
        }
        return false;
    }

    public final float K() {
        return this.f9997u;
    }

    public boolean K0(C0625d c0625d) {
        e eVar;
        D2.h.f(c0625d, "handler");
        if (c0625d == this || (eVar = this.f9970C) == null) {
            return false;
        }
        return eVar.a(this, c0625d);
    }

    public final boolean L() {
        return this.f9993q;
    }

    public final boolean L0(C0625d c0625d) {
        e eVar;
        D2.h.f(c0625d, "handler");
        if (c0625d == this || (eVar = this.f9970C) == null) {
            return false;
        }
        return eVar.d(this, c0625d);
    }

    public final int M() {
        return this.f10002z;
    }

    public final void M0(int i3) {
        int[] iArr = this.f9977a;
        if (iArr[i3] == -1) {
            iArr[i3] = C();
            this.f9978b++;
        }
    }

    protected final i N() {
        return this.f9968A;
    }

    public final void N0(int i3) {
        int[] iArr = this.f9977a;
        if (iArr[i3] != -1) {
            iArr[i3] = -1;
            this.f9978b--;
        }
    }

    public final int O() {
        return this.f9971D;
    }

    protected final PointF O0(PointF pointF) {
        PointF pointFK;
        D2.h.f(pointF, "point");
        i iVar = this.f9968A;
        if (iVar != null && (pointFK = iVar.K(this.f9981e, pointF)) != null) {
            return pointFK;
        }
        pointF.x = Float.NaN;
        pointF.y = Float.NaN;
        return pointF;
    }

    public final boolean P() {
        return this.f9976I;
    }

    public final void P0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
            v(motionEvent, motionEvent2);
            x(motionEvent, motionEvent2);
        } else if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6) {
            x(motionEvent, motionEvent2);
            y(motionEvent, motionEvent2);
        } else if (motionEvent.getActionMasked() == 2) {
            x(motionEvent, motionEvent2);
        }
    }

    public final int Q() {
        return this.f9982f;
    }

    public final boolean Q0() {
        int i3;
        return (!this.f9986j || (i3 = this.f9982f) == 1 || i3 == 3 || i3 == 5 || this.f9978b <= 0) ? false : true;
    }

    public final int R() {
        return this.f9980d;
    }

    public final void R0(C2.a aVar) {
        D2.h.f(aVar, "closure");
        this.f9985i = true;
        aVar.a();
        this.f9985i = false;
    }

    public final int S() {
        return this.f9990n;
    }

    public final int T() {
        return this.f9991o;
    }

    public final View U() {
        return this.f9981e;
    }

    public final void V(MotionEvent motionEvent, MotionEvent motionEvent2) {
        int i3;
        D2.h.f(motionEvent, "transformedEvent");
        D2.h.f(motionEvent2, "sourceEvent");
        if (!this.f9986j || (i3 = this.f9982f) == 3 || i3 == 1 || i3 == 5 || this.f9978b < 1) {
            return;
        }
        try {
            MotionEvent[] motionEventArr = {k(motionEvent), k(motionEvent2)};
            MotionEvent motionEvent3 = motionEventArr[0];
            MotionEvent motionEvent4 = motionEventArr[1];
            this.f9983g = motionEvent3.getX();
            this.f9984h = motionEvent3.getY();
            this.f10002z = motionEvent3.getPointerCount();
            boolean zD0 = d0(this.f9981e, this.f9983g, this.f9984h);
            this.f9985i = zD0;
            if (this.f10001y && !zD0) {
                int i4 = this.f9982f;
                if (i4 == 4) {
                    o();
                    return;
                } else {
                    if (i4 == 2) {
                        B();
                        return;
                    }
                    return;
                }
            }
            k kVar = k.f10028a;
            this.f9996t = kVar.b(motionEvent3, true);
            this.f9997u = kVar.c(motionEvent3, true);
            this.f9999w = motionEvent3.getRawX() - motionEvent3.getX();
            this.f10000x = motionEvent3.getRawY() - motionEvent3.getY();
            if (motionEvent2.getAction() == 0 || motionEvent2.getAction() == 9 || motionEvent2.getAction() == 7) {
                D0(motionEvent2);
            }
            if (motionEvent2.getAction() == 9 || motionEvent2.getAction() == 7 || motionEvent2.getAction() == 10) {
                i0(motionEvent3, motionEvent4);
            } else {
                h0(motionEvent3, motionEvent4);
            }
            if (!D2.h.b(motionEvent3, motionEvent)) {
                motionEvent3.recycle();
            }
            if (D2.h.b(motionEvent4, motionEvent2)) {
                return;
            }
            motionEvent4.recycle();
        } catch (a unused) {
            B();
        }
    }

    public final boolean W(C0625d c0625d) {
        D2.h.f(c0625d, "other");
        int length = this.f9977a.length;
        for (int i3 = 0; i3 < length; i3++) {
            if (this.f9977a[i3] != -1 && c0625d.f9977a[i3] != -1) {
                return true;
            }
        }
        return false;
    }

    public final boolean X() {
        return this.f9974G;
    }

    public final boolean Y() {
        return this.f9975H;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0018, code lost:
    
        r0 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean a0(n2.C0625d r4) {
        /*
            r3 = this;
            java.lang.String r0 = "of"
            D2.h.f(r4, r0)
            android.view.View r0 = r3.f9981e
            r1 = 0
            if (r0 == 0) goto Lf
            android.view.ViewParent r0 = r0.getParent()
            goto L10
        Lf:
            r0 = r1
        L10:
            boolean r2 = r0 instanceof android.view.View
            if (r2 == 0) goto L17
            android.view.View r0 = (android.view.View) r0
            goto L18
        L17:
            r0 = r1
        L18:
            if (r0 == 0) goto L2f
            android.view.View r2 = r4.f9981e
            boolean r2 = D2.h.b(r0, r2)
            if (r2 == 0) goto L24
            r4 = 1
            return r4
        L24:
            android.view.ViewParent r0 = r0.getParent()
            boolean r2 = r0 instanceof android.view.View
            if (r2 == 0) goto L17
            android.view.View r0 = (android.view.View) r0
            goto L18
        L2f:
            r4 = 0
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: n2.C0625d.a0(n2.d):boolean");
    }

    public final boolean b0() {
        return this.f9986j;
    }

    public final boolean c0() {
        return this.f9985i;
    }

    public final boolean d0(View view, float f3, float f4) {
        float f5;
        C0612f.a aVar = C0612f.f9840a;
        D2.h.c(view);
        if (aVar.b(view)) {
            return aVar.a(view, f3, f4);
        }
        float width = view.getWidth();
        float height = view.getHeight();
        float[] fArr = this.f9994r;
        if (fArr != null) {
            float f6 = fArr[0];
            float f7 = fArr[1];
            float f8 = fArr[2];
            float f9 = fArr[3];
            b bVar = f9964J;
            float f10 = bVar.c(f6) ? 0.0f - f6 : 0.0f;
            f = bVar.c(f7) ? 0.0f - f7 : 0.0f;
            if (bVar.c(f8)) {
                width += f8;
            }
            if (bVar.c(f9)) {
                height += f9;
            }
            float f11 = fArr[4];
            float f12 = fArr[5];
            if (bVar.c(f11)) {
                if (!bVar.c(f6)) {
                    f10 = width - f11;
                } else if (!bVar.c(f8)) {
                    width = f11 + f10;
                }
            }
            if (bVar.c(f12)) {
                if (!bVar.c(f7)) {
                    f = height - f12;
                } else if (!bVar.c(f9)) {
                    height = f12 + f;
                }
            }
            f5 = f;
            f = f10;
        } else {
            f5 = 0.0f;
        }
        return f <= f3 && f3 <= width && f5 <= f4 && f4 <= height;
    }

    protected void h0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
        e0(1);
    }

    public final void i() {
        j(false);
    }

    protected void i0(MotionEvent motionEvent, MotionEvent motionEvent2) {
        D2.h.f(motionEvent, "event");
        D2.h.f(motionEvent2, "sourceEvent");
    }

    public void j(boolean z3) {
        if (!this.f9998v || z3) {
            int i3 = this.f9982f;
            if (i3 == 0 || i3 == 2) {
                e0(4);
            }
        }
    }

    public final void m0(View view, i iVar) {
        if (this.f9981e != null || this.f9968A != null) {
            throw new IllegalStateException("Already prepared or hasn't been reset");
        }
        Arrays.fill(this.f9977a, -1);
        this.f9978b = 0;
        this.f9982f = 0;
        this.f9981e = view;
        this.f9968A = iVar;
        Activity activityF = F(view != null ? view.getContext() : null);
        View viewFindViewById = activityF != null ? activityF.findViewById(R.id.content) : null;
        if (viewFindViewById != null) {
            viewFindViewById.getLocationOnScreen(this.f9979c);
        } else {
            int[] iArr = this.f9979c;
            iArr[0] = 0;
            iArr[1] = 0;
        }
        j0();
    }

    public final void n() {
        if (this.f9982f == 0) {
            e0(2);
        }
    }

    public final void n0() {
        this.f9981e = null;
        this.f9968A = null;
        Arrays.fill(this.f9977a, -1);
        this.f9978b = 0;
        this.f9991o = 0;
        AbstractC0711h.k(this.f9992p, null, 0, 0, 6, null);
        this.f9990n = 0;
        k0();
    }

    public final void o() {
        int i3 = this.f9982f;
        if (i3 == 4 || i3 == 0 || i3 == 2 || this.f9975H) {
            g0();
            e0(3);
        }
    }

    public void o0() {
        this.f9993q = false;
        this.f9998v = false;
        this.f10001y = false;
        this.f9986j = true;
        this.f9994r = null;
    }

    public final WritableArray q() {
        WritableArray writableArray = this.f9989m;
        this.f9989m = null;
        return writableArray;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final C0625d q0() {
        D2.h.d(this, "null cannot be cast to non-null type ConcreteGestureHandlerT of com.swmansion.gesturehandler.core.GestureHandler");
        return this;
    }

    public final WritableArray r() {
        WritableArray writableArray = this.f9988l;
        this.f9988l = null;
        return writableArray;
    }

    public final void r0(int i3) {
        this.f9987k = i3;
    }

    public final void s0(int i3) {
        this.f9973F = i3;
    }

    public void t(MotionEvent motionEvent) {
        D2.h.f(motionEvent, "event");
        r rVar = this.f9969B;
        if (rVar != null) {
            rVar.c(q0(), motionEvent);
        }
    }

    public final void t0(boolean z3) {
        this.f9974G = z3;
    }

    public String toString() {
        String simpleName;
        View view = this.f9981e;
        if (view == null) {
            simpleName = null;
        } else {
            D2.h.c(view);
            simpleName = view.getClass().getSimpleName();
        }
        return getClass().getSimpleName() + "@[" + this.f9980d + "]:" + simpleName;
    }

    public void u(int i3, int i4) {
        r rVar = this.f9969B;
        if (rVar != null) {
            rVar.a(q0(), i3, i4);
        }
    }

    public final void u0(boolean z3) {
        this.f9975H = z3;
    }

    public final C0625d v0(boolean z3) {
        final C0625d c0625dQ0 = q0();
        if (c0625dQ0.f9981e != null && c0625dQ0.f9986j != z3) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: n2.c
                @Override // java.lang.Runnable
                public final void run() {
                    C0625d.w0(this.f9963b);
                }
            });
        }
        c0625dQ0.f9986j = z3;
        return c0625dQ0;
    }

    public void w() {
        r rVar;
        if (this.f9988l == null || (rVar = this.f9969B) == null) {
            return;
        }
        rVar.b(q0());
    }

    public final C0625d x0(float f3, float f4, float f5, float f6, float f7, float f8) {
        C0625d c0625dQ0 = q0();
        if (c0625dQ0.f9994r == null) {
            c0625dQ0.f9994r = new float[6];
        }
        float[] fArr = c0625dQ0.f9994r;
        D2.h.c(fArr);
        fArr[0] = f3;
        float[] fArr2 = c0625dQ0.f9994r;
        D2.h.c(fArr2);
        fArr2[1] = f4;
        float[] fArr3 = c0625dQ0.f9994r;
        D2.h.c(fArr3);
        fArr3[2] = f5;
        float[] fArr4 = c0625dQ0.f9994r;
        D2.h.c(fArr4);
        fArr4[3] = f6;
        float[] fArr5 = c0625dQ0.f9994r;
        D2.h.c(fArr5);
        fArr5[4] = f7;
        float[] fArr6 = c0625dQ0.f9994r;
        D2.h.c(fArr6);
        fArr6[5] = f8;
        b bVar = f9964J;
        if (bVar.c(f7) && bVar.c(f3) && bVar.c(f5)) {
            throw new IllegalArgumentException("Cannot have all of left, right and width defined");
        }
        if (bVar.c(f7) && !bVar.c(f3) && !bVar.c(f5)) {
            throw new IllegalArgumentException("When width is set one of left or right pads need to be defined");
        }
        if (bVar.c(f8) && bVar.c(f6) && bVar.c(f4)) {
            throw new IllegalArgumentException("Cannot have all of top, bottom and height defined");
        }
        if (!bVar.c(f8) || bVar.c(f6) || bVar.c(f4)) {
            return c0625dQ0;
        }
        throw new IllegalArgumentException("When height is set one of top or bottom pads need to be defined");
    }

    public final C0625d y0(e eVar) {
        C0625d c0625dQ0 = q0();
        c0625dQ0.f9970C = eVar;
        return c0625dQ0;
    }

    public final void z() {
        int i3 = this.f9982f;
        if (i3 == 2 || i3 == 4) {
            e0(5);
        }
    }

    public final C0625d z0(boolean z3) {
        C0625d c0625dQ0 = q0();
        c0625dQ0.f9998v = z3;
        return c0625dQ0;
    }

    protected void g0() {
    }

    protected void j0() {
    }

    protected void k0() {
    }

    public void p0() {
    }

    protected void l0(int i3, int i4) {
    }
}
