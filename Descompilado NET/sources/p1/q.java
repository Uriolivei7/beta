package P1;

import P1.s;
import a1.C0210a;
import android.view.MotionEvent;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.SoftAssertions;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.uimanager.events.RCTModernEventEmitter;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;

/* JADX INFO: loaded from: classes.dex */
public final class q extends d {

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public static final a f1691m = new a(null);

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static final String f1692n = q.class.getSimpleName();

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final q.f f1693o = new q.f(3);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private MotionEvent f1694h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private s f1695i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private short f1696j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private float f1697k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private float f1698l;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final q a(int i3, int i4, s sVar, MotionEvent motionEvent, long j3, float f3, float f4, r rVar) {
            D2.h.f(rVar, "touchEventCoalescingKeyHelper");
            q qVar = (q) q.f1693o.b();
            if (qVar == null) {
                qVar = new q(null);
            }
            Object objC = C0210a.c(motionEvent);
            D2.h.e(objC, "assertNotNull(...)");
            qVar.A(i3, i4, sVar, (MotionEvent) objC, j3, f3, f4, rVar);
            return qVar;
        }

        private a() {
        }
    }

    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f1699a;

        static {
            int[] iArr = new int[s.values().length];
            try {
                iArr[s.f1702d.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[s.f1703e.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[s.f1705g.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[s.f1704f.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f1699a = iArr;
        }
    }

    public /* synthetic */ q(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void A(int i3, int i4, s sVar, MotionEvent motionEvent, long j3, float f3, float f4, r rVar) {
        super.r(i3, i4, motionEvent.getEventTime());
        short sB = 0;
        SoftAssertions.assertCondition(j3 != Long.MIN_VALUE, "Gesture start time must be initialized");
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            rVar.a(j3);
        } else if (action == 1) {
            rVar.e(j3);
        } else if (action == 2) {
            sB = rVar.b(j3);
        } else if (action == 3) {
            rVar.e(j3);
        } else if (action == 5 || action == 6) {
            rVar.d(j3);
        }
        this.f1694h = MotionEvent.obtain(motionEvent);
        this.f1695i = sVar;
        this.f1696j = sB;
        this.f1697k = f3;
        this.f1698l = f4;
    }

    public static final q B(int i3, int i4, s sVar, MotionEvent motionEvent, long j3, float f3, float f4, r rVar) {
        return f1691m.a(i3, i4, sVar, motionEvent, j3, f3, f4, rVar);
    }

    private final boolean C() {
        if (this.f1694h != null) {
            return true;
        }
        String str = f1692n;
        D2.h.e(str, "TAG");
        ReactSoftExceptionLogger.logSoftException(str, new IllegalStateException("Cannot dispatch a TouchEvent that has no MotionEvent; the TouchEvent has been recycled"));
        return false;
    }

    @Override // P1.d
    public boolean a() {
        s sVar = (s) C0210a.c(this.f1695i);
        int i3 = sVar == null ? -1 : b.f1699a[sVar.ordinal()];
        if (i3 == 1 || i3 == 2 || i3 == 3) {
            return false;
        }
        if (i3 == 4) {
            return true;
        }
        throw new RuntimeException("Unknown touch event type: " + this.f1695i);
    }

    @Override // P1.d
    public void c(RCTEventEmitter rCTEventEmitter) {
        D2.h.f(rCTEventEmitter, "rctEventEmitter");
        if (C()) {
            t.d(rCTEventEmitter, this);
        }
    }

    @Override // P1.d
    public void d(RCTModernEventEmitter rCTModernEventEmitter) {
        D2.h.f(rCTModernEventEmitter, "rctEventEmitter");
        if (C()) {
            rCTModernEventEmitter.receiveTouches(this);
        }
    }

    @Override // P1.d
    public short g() {
        return this.f1696j;
    }

    @Override // P1.d
    public int i() {
        s sVar = this.f1695i;
        if (sVar == null) {
            return 2;
        }
        int i3 = b.f1699a[sVar.ordinal()];
        if (i3 == 1) {
            return 0;
        }
        if (i3 == 2 || i3 == 3) {
            return 1;
        }
        if (i3 == 4) {
            return 4;
        }
        throw new C0685h();
    }

    @Override // P1.d
    public String k() {
        s.a aVar = s.f1701c;
        Object objC = C0210a.c(this.f1695i);
        D2.h.e(objC, "assertNotNull(...)");
        return aVar.a((s) objC);
    }

    @Override // P1.d
    public void t() {
        MotionEvent motionEvent = this.f1694h;
        if (motionEvent != null) {
            motionEvent.recycle();
        }
        this.f1694h = null;
        try {
            f1693o.a(this);
        } catch (IllegalStateException e4) {
            String str = f1692n;
            D2.h.e(str, "TAG");
            ReactSoftExceptionLogger.logSoftException(str, e4);
        }
    }

    public final MotionEvent w() {
        Object objC = C0210a.c(this.f1694h);
        D2.h.e(objC, "assertNotNull(...)");
        return (MotionEvent) objC;
    }

    public final s x() {
        Object objC = C0210a.c(this.f1695i);
        D2.h.e(objC, "assertNotNull(...)");
        return (s) objC;
    }

    public final float y() {
        return this.f1697k;
    }

    public final float z() {
        return this.f1698l;
    }

    private q() {
    }
}
