package M2;

import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: M2.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0193d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f1006a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f1007b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f1008c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f1009d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final boolean f1010e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final boolean f1011f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final boolean f1012g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f1013h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final int f1014i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final boolean f1015j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final boolean f1016k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final boolean f1017l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private String f1018m;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public static final b f1005p = new b(null);

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public static final C0193d f1003n = new a().d().a();

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public static final C0193d f1004o = new a().f().c(Integer.MAX_VALUE, TimeUnit.SECONDS).a();

    /* JADX INFO: renamed from: M2.d$a */
    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f1019a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f1020b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f1021c = -1;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f1022d = -1;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f1023e = -1;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f1024f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private boolean f1025g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private boolean f1026h;

        private final int b(long j3) {
            if (j3 > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            return (int) j3;
        }

        public final C0193d a() {
            return new C0193d(this.f1019a, this.f1020b, this.f1021c, -1, false, false, false, this.f1022d, this.f1023e, this.f1024f, this.f1025g, this.f1026h, null, null);
        }

        public final a c(int i3, TimeUnit timeUnit) {
            D2.h.f(timeUnit, "timeUnit");
            if (i3 >= 0) {
                this.f1022d = b(timeUnit.toSeconds(i3));
                return this;
            }
            throw new IllegalArgumentException(("maxStale < 0: " + i3).toString());
        }

        public final a d() {
            this.f1019a = true;
            return this;
        }

        public final a e() {
            this.f1020b = true;
            return this;
        }

        public final a f() {
            this.f1024f = true;
            return this;
        }
    }

    /* JADX INFO: renamed from: M2.d$b */
    public static final class b {
        private b() {
        }

        private final int a(String str, String str2, int i3) {
            int length = str.length();
            while (i3 < length) {
                if (K2.o.D(str2, str.charAt(i3), false, 2, null)) {
                    return i3;
                }
                i3++;
            }
            return str.length();
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x004b  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00de  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00e2  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final M2.C0193d b(M2.t r32) {
            /*
                Method dump skipped, instruction units count: 416
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: M2.C0193d.b.b(M2.t):M2.d");
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private C0193d(boolean z3, boolean z4, int i3, int i4, boolean z5, boolean z6, boolean z7, int i5, int i6, boolean z8, boolean z9, boolean z10, String str) {
        this.f1006a = z3;
        this.f1007b = z4;
        this.f1008c = i3;
        this.f1009d = i4;
        this.f1010e = z5;
        this.f1011f = z6;
        this.f1012g = z7;
        this.f1013h = i5;
        this.f1014i = i6;
        this.f1015j = z8;
        this.f1016k = z9;
        this.f1017l = z10;
        this.f1018m = str;
    }

    public final boolean a() {
        return this.f1010e;
    }

    public final boolean b() {
        return this.f1011f;
    }

    public final int c() {
        return this.f1008c;
    }

    public final int d() {
        return this.f1013h;
    }

    public final int e() {
        return this.f1014i;
    }

    public final boolean f() {
        return this.f1012g;
    }

    public final boolean g() {
        return this.f1006a;
    }

    public final boolean h() {
        return this.f1007b;
    }

    public final boolean i() {
        return this.f1015j;
    }

    public String toString() {
        String str = this.f1018m;
        if (str != null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        if (this.f1006a) {
            sb.append("no-cache, ");
        }
        if (this.f1007b) {
            sb.append("no-store, ");
        }
        if (this.f1008c != -1) {
            sb.append("max-age=");
            sb.append(this.f1008c);
            sb.append(", ");
        }
        if (this.f1009d != -1) {
            sb.append("s-maxage=");
            sb.append(this.f1009d);
            sb.append(", ");
        }
        if (this.f1010e) {
            sb.append("private, ");
        }
        if (this.f1011f) {
            sb.append("public, ");
        }
        if (this.f1012g) {
            sb.append("must-revalidate, ");
        }
        if (this.f1013h != -1) {
            sb.append("max-stale=");
            sb.append(this.f1013h);
            sb.append(", ");
        }
        if (this.f1014i != -1) {
            sb.append("min-fresh=");
            sb.append(this.f1014i);
            sb.append(", ");
        }
        if (this.f1015j) {
            sb.append("only-if-cached, ");
        }
        if (this.f1016k) {
            sb.append("no-transform, ");
        }
        if (this.f1017l) {
            sb.append("immutable, ");
        }
        if (sb.length() == 0) {
            return "";
        }
        sb.delete(sb.length() - 2, sb.length());
        String string = sb.toString();
        D2.h.e(string, "StringBuilder().apply(builderAction).toString()");
        this.f1018m = string;
        return string;
    }

    public /* synthetic */ C0193d(boolean z3, boolean z4, int i3, int i4, boolean z5, boolean z6, boolean z7, int i5, int i6, boolean z8, boolean z9, boolean z10, String str, DefaultConstructorMarker defaultConstructorMarker) {
        this(z3, z4, i3, i4, z5, z6, z7, i5, i6, z8, z9, z10, str);
    }
}
