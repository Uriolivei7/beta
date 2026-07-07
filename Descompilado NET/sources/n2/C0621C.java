package n2;

import android.view.VelocityTracker;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: n2.C, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0621C {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final a f9936f = new a(null);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final C0621C f9937g = new C0621C(-1.0d, 0.0d);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final C0621C f9938h = new C0621C(1.0d, 0.0d);

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final C0621C f9939i = new C0621C(0.0d, -1.0d);

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final C0621C f9940j = new C0621C(0.0d, 1.0d);

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final C0621C f9941k = new C0621C(1.0d, -1.0d);

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final C0621C f9942l = new C0621C(1.0d, 1.0d);

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static final C0621C f9943m = new C0621C(-1.0d, -1.0d);

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static final C0621C f9944n = new C0621C(-1.0d, 1.0d);

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final C0621C f9945o = new C0621C(0.0d, 0.0d);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final double f9946a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final double f9947b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final double f9948c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final double f9949d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final double f9950e;

    /* JADX INFO: renamed from: n2.C$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final C0621C a(int i3) {
            switch (i3) {
                case 1:
                    return C0621C.f9938h;
                case 2:
                    return C0621C.f9937g;
                case 3:
                case 7:
                default:
                    return C0621C.f9945o;
                case 4:
                    return C0621C.f9939i;
                case 5:
                    return C0621C.f9941k;
                case 6:
                    return C0621C.f9943m;
                case 8:
                    return C0621C.f9940j;
                case 9:
                    return C0621C.f9942l;
                case 10:
                    return C0621C.f9944n;
            }
        }

        public final C0621C b(VelocityTracker velocityTracker) {
            D2.h.f(velocityTracker, "tracker");
            velocityTracker.computeCurrentVelocity(1000);
            return new C0621C(velocityTracker.getXVelocity(), velocityTracker.getYVelocity());
        }

        private a() {
        }
    }

    public C0621C(double d4, double d5) {
        this.f9946a = d4;
        this.f9947b = d5;
        double dHypot = Math.hypot(d4, d5);
        this.f9950e = dHypot;
        boolean z3 = dHypot > 0.1d;
        this.f9948c = z3 ? d4 / dHypot : 0.0d;
        this.f9949d = z3 ? d5 / dHypot : 0.0d;
    }

    private final double j(C0621C c0621c) {
        return (this.f9948c * c0621c.f9948c) + (this.f9949d * c0621c.f9949d);
    }

    public final double k() {
        return this.f9950e;
    }

    public final boolean l(C0621C c0621c, double d4) {
        D2.h.f(c0621c, "vector");
        return j(c0621c) > d4;
    }
}
