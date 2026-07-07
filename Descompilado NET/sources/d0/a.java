package D0;

import D0.c;
import D2.h;
import g0.C0541b;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0711h;

/* JADX INFO: loaded from: classes.dex */
public final class a implements c.b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final byte[] f115c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final int f116d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final byte[] f117e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final int f118f;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final byte[] f121i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final int f122j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final byte[] f123k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final int f124l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static final byte[] f125m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static final byte[][] f126n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final byte[] f127o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static final byte[] f128p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private static final int f129q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private static final byte[] f130r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private static final byte[] f131s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private static final byte[] f132t;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f133a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final C0002a f114b = new C0002a(null);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final byte[] f119g = f.a("GIF87a");

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final byte[] f120h = f.a("GIF89a");

    /* JADX INFO: renamed from: D0.a$a, reason: collision with other inner class name */
    public static final class C0002a {
        public /* synthetic */ C0002a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final int k(byte[] bArr) {
            if (bArr.length < 4) {
                return -1;
            }
            return (bArr[3] & 255) | ((bArr[0] & 255) << 24) | ((bArr[1] & 255) << 16) | ((bArr[2] & 255) << 8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final c l(byte[] bArr, int i3) {
            if (C0541b.h(bArr, 0, i3)) {
                return C0541b.g(bArr, 0) ? b.f140g : C0541b.f(bArr, 0) ? b.f141h : C0541b.c(bArr, 0, i3) ? C0541b.b(bArr, 0) ? b.f144k : C0541b.d(bArr, 0) ? b.f143j : b.f142i : c.f151d;
            }
            throw new IllegalStateException("Check failed.");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean m(byte[] bArr, int i3) {
            if (i3 >= 12 && k(bArr) >= 8 && f.b(bArr, a.f131s, 4)) {
                return f.b(bArr, a.f132t, 8);
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean n(byte[] bArr, int i3) {
            return i3 >= 4 && f.c(bArr, a.f130r);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean o(byte[] bArr, int i3) {
            if (i3 < a.f121i.length) {
                return false;
            }
            return f.c(bArr, a.f121i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean p(byte[] bArr, int i3) {
            return i3 >= a.f129q && (f.c(bArr, a.f127o) || f.c(bArr, a.f128p));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean q(byte[] bArr, int i3) {
            if (i3 < 6) {
                return false;
            }
            return f.c(bArr, a.f119g) || f.c(bArr, a.f120h);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean r(byte[] bArr, int i3) {
            if (i3 < 12 || bArr[3] < 8 || !f.b(bArr, a.f125m, 4)) {
                return false;
            }
            for (byte[] bArr2 : a.f126n) {
                if (f.b(bArr, bArr2, 8)) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean s(byte[] bArr, int i3) {
            if (i3 < a.f123k.length) {
                return false;
            }
            return f.c(bArr, a.f123k);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean t(byte[] bArr, int i3) {
            return i3 >= a.f115c.length && f.c(bArr, a.f115c);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean u(byte[] bArr, int i3) {
            return i3 >= a.f117e.length && f.c(bArr, a.f117e);
        }

        private C0002a() {
        }
    }

    static {
        byte[] bArr = {-1, -40, -1};
        f115c = bArr;
        f116d = bArr.length;
        byte[] bArr2 = {-119, 80, 78, 71, 13, 10, 26, 10};
        f117e = bArr2;
        f118f = bArr2.length;
        byte[] bArrA = f.a("BM");
        f121i = bArrA;
        f122j = bArrA.length;
        byte[] bArr3 = {0, 0, 1, 0};
        f123k = bArr3;
        f124l = bArr3.length;
        f125m = f.a("ftyp");
        f126n = new byte[][]{f.a("heic"), f.a("heix"), f.a("hevc"), f.a("hevx"), f.a("mif1"), f.a("msf1")};
        byte[] bArr4 = {73, 73, 42, 0};
        f127o = bArr4;
        f128p = new byte[]{77, 77, 0, 42};
        f129q = bArr4.length;
        f130r = new byte[]{3, 0, 8, 0};
        f131s = f.a("ftyp");
        f132t = f.a("avif");
    }

    public a() {
        Object objY = AbstractC0711h.y(new Integer[]{21, 20, Integer.valueOf(f116d), Integer.valueOf(f118f), 6, Integer.valueOf(f122j), Integer.valueOf(f124l), 12, 4, 12});
        if (objY == null) {
            throw new IllegalStateException("Required value was null.");
        }
        this.f133a = ((Number) objY).intValue();
    }

    @Override // D0.c.b
    public int a() {
        return this.f133a;
    }

    @Override // D0.c.b
    public c b(byte[] bArr, int i3) {
        h.f(bArr, "headerBytes");
        if (C0541b.h(bArr, 0, i3)) {
            return f114b.l(bArr, i3);
        }
        C0002a c0002a = f114b;
        return c0002a.t(bArr, i3) ? b.f135b : c0002a.u(bArr, i3) ? b.f136c : c0002a.q(bArr, i3) ? b.f137d : c0002a.o(bArr, i3) ? b.f138e : c0002a.s(bArr, i3) ? b.f139f : c0002a.m(bArr, i3) ? b.f148o : c0002a.r(bArr, i3) ? b.f145l : c0002a.n(bArr, i3) ? b.f147n : c0002a.p(bArr, i3) ? b.f146m : c.f151d;
    }
}
