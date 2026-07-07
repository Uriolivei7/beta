package Q1;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f1804b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final b f1805c = new b("OPACITY", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final b f1806d = new b("SCALE_X", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final b f1807e = new b("SCALE_Y", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final b f1808f = new b("SCALE_XY", 3);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ b[] f1809g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f1810h;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        public final b a(String str) {
            D2.h.f(str, "name");
            switch (str.hashCode()) {
                case -1267206133:
                    if (str.equals("opacity")) {
                        return b.f1805c;
                    }
                    break;
                case -908189618:
                    if (str.equals("scaleX")) {
                        return b.f1806d;
                    }
                    break;
                case -908189617:
                    if (str.equals("scaleY")) {
                        return b.f1807e;
                    }
                    break;
                case 1910893003:
                    if (str.equals("scaleXY")) {
                        return b.f1808f;
                    }
                    break;
            }
            throw new IllegalArgumentException("Unsupported animated property: " + str);
        }

        private a() {
        }
    }

    static {
        b[] bVarArrA = a();
        f1809g = bVarArrA;
        f1810h = AbstractC0764a.a(bVarArrA);
        f1804b = new a(null);
    }

    private b(String str, int i3) {
    }

    private static final /* synthetic */ b[] a() {
        return new b[]{f1805c, f1806d, f1807e, f1808f};
    }

    public static final b b(String str) {
        return f1804b.a(str);
    }

    public static b valueOf(String str) {
        return (b) Enum.valueOf(b.class, str);
    }

    public static b[] values() {
        return (b[]) f1809g.clone();
    }
}
