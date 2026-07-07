package P1;

import android.view.View;
import d1.AbstractC0505m;
import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX INFO: loaded from: classes.dex */
public final class o {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final o f1669a = new o();

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    public static final class a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final a f1670b = new a("CANCEL", 0);

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final a f1671c = new a("CANCEL_CAPTURE", 1);

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public static final a f1672d = new a("CLICK", 2);

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public static final a f1673e = new a("CLICK_CAPTURE", 3);

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public static final a f1674f = new a("DOWN", 4);

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public static final a f1675g = new a("DOWN_CAPTURE", 5);

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public static final a f1676h = new a("ENTER", 6);

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public static final a f1677i = new a("ENTER_CAPTURE", 7);

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public static final a f1678j = new a("LEAVE", 8);

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        public static final a f1679k = new a("LEAVE_CAPTURE", 9);

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        public static final a f1680l = new a("MOVE", 10);

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public static final a f1681m = new a("MOVE_CAPTURE", 11);

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        public static final a f1682n = new a("UP", 12);

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        public static final a f1683o = new a("UP_CAPTURE", 13);

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        public static final a f1684p = new a("OUT", 14);

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        public static final a f1685q = new a("OUT_CAPTURE", 15);

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        public static final a f1686r = new a("OVER", 16);

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        public static final a f1687s = new a("OVER_CAPTURE", 17);

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        private static final /* synthetic */ a[] f1688t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        private static final /* synthetic */ EnumEntries f1689u;

        static {
            a[] aVarArrA = a();
            f1688t = aVarArrA;
            f1689u = AbstractC0764a.a(aVarArrA);
        }

        private a(String str, int i3) {
        }

        private static final /* synthetic */ a[] a() {
            return new a[]{f1670b, f1671c, f1672d, f1673e, f1674f, f1675g, f1676h, f1677i, f1678j, f1679k, f1680l, f1681m, f1682n, f1683o, f1684p, f1685q, f1686r, f1687s};
        }

        public static a valueOf(String str) {
            return (a) Enum.valueOf(a.class, str);
        }

        public static a[] values() {
            return (a[]) f1688t.clone();
        }
    }

    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f1690a;

        static {
            int[] iArr = new int[a.values().length];
            try {
                iArr[a.f1674f.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[a.f1675g.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[a.f1682n.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[a.f1683o.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[a.f1670b.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[a.f1671c.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[a.f1672d.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr[a.f1673e.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            f1690a = iArr;
        }
    }

    private o() {
    }

    public static final int a(String str, int i3, int i4) {
        D2.h.f(str, "pointerType");
        if (D2.h.b("touch", str)) {
            return 0;
        }
        int i5 = i4 ^ i3;
        if (i5 == 0) {
            return -1;
        }
        if (i5 == 1) {
            return 0;
        }
        if (i5 == 2) {
            return 2;
        }
        if (i5 == 4) {
            return 1;
        }
        if (i5 != 8) {
            return i5 != 16 ? -1 : 4;
        }
        return 3;
    }

    public static final int b(String str, String str2, int i3) {
        D2.h.f(str2, "pointerType");
        if (f1669a.g(str)) {
            return 0;
        }
        if (D2.h.b("touch", str2)) {
            return 1;
        }
        return i3;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static final int c(String str) {
        if (str == null) {
            return 2;
        }
        switch (str.hashCode()) {
            case -1786514288:
                if (!str.equals("topPointerEnter")) {
                }
                break;
            case -1780335505:
                if (!str.equals("topPointerLeave")) {
                }
                break;
            case -1304584214:
                if (!str.equals("topPointerDown")) {
                }
                break;
            case -1304316135:
                if (!str.equals("topPointerMove")) {
                }
                break;
            case -1304250340:
                if (!str.equals("topPointerOver")) {
                }
                break;
            case -1065042973:
                if (!str.equals("topPointerUp")) {
                }
                break;
            case 383186882:
                if (!str.equals("topPointerCancel")) {
                }
                break;
            case 1343400710:
                if (!str.equals("topPointerOut")) {
                }
                break;
        }
        return 2;
    }

    public static final double d(int i3, String str) {
        return (f1669a.g(str) || i3 == 0) ? 0.0d : 0.5d;
    }

    public static final String e(int i3) {
        return i3 != 1 ? i3 != 2 ? i3 != 3 ? "" : "mouse" : "pen" : "touch";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0040 A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final boolean f(java.lang.String r1) {
        /*
            if (r1 == 0) goto L42
            int r0 = r1.hashCode()
            switch(r0) {
                case -1304584214: goto L37;
                case -1304316135: goto L2e;
                case -1304250340: goto L25;
                case -1065042973: goto L1c;
                case 383186882: goto L13;
                case 1343400710: goto La;
                default: goto L9;
            }
        L9:
            goto L42
        La:
            java.lang.String r0 = "topPointerOut"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L40
            goto L42
        L13:
            java.lang.String r0 = "topPointerCancel"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L40
            goto L42
        L1c:
            java.lang.String r0 = "topPointerUp"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L40
            goto L42
        L25:
            java.lang.String r0 = "topPointerOver"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L42
            goto L40
        L2e:
            java.lang.String r0 = "topPointerMove"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L40
            goto L42
        L37:
            java.lang.String r0 = "topPointerDown"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L40
            goto L42
        L40:
            r1 = 1
            goto L43
        L42:
            r1 = 0
        L43:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: P1.o.f(java.lang.String):boolean");
    }

    public static final boolean h(View view, a aVar) {
        D2.h.f(aVar, "event");
        if (view == null) {
            return true;
        }
        switch (b.f1690a[aVar.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                break;
            default:
                Object tag = view.getTag(AbstractC0505m.f9246s);
                Integer num = tag instanceof Integer ? (Integer) tag : null;
                if (num == null || (num.intValue() & (1 << aVar.ordinal())) == 0) {
                }
                break;
        }
        return true;
    }

    public final boolean g(String str) {
        int iHashCode;
        return str != null && ((iHashCode = str.hashCode()) == -1780335505 ? str.equals("topPointerLeave") : !(iHashCode == -1065042973 ? !str.equals("topPointerUp") : !(iHashCode == 1343400710 && str.equals("topPointerOut"))));
    }
}
