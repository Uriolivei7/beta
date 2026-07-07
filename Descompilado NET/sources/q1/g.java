package Q1;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f1833b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final g f1834c = new g("CREATE", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final g f1835d = new g("UPDATE", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final g f1836e = new g("DELETE", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ g[] f1837f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f1838g;

    public static final class a {

        /* JADX INFO: renamed from: Q1.g$a$a, reason: collision with other inner class name */
        public /* synthetic */ class C0027a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f1839a;

            static {
                int[] iArr = new int[g.values().length];
                try {
                    iArr[g.f1834c.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[g.f1835d.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[g.f1836e.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                f1839a = iArr;
            }
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(g gVar) {
            D2.h.f(gVar, "type");
            int i3 = C0027a.f1839a[gVar.ordinal()];
            if (i3 == 1) {
                return "create";
            }
            if (i3 == 2) {
                return "update";
            }
            if (i3 == 3) {
                return "delete";
            }
            throw new C0685h();
        }

        private a() {
        }
    }

    static {
        g[] gVarArrA = a();
        f1837f = gVarArrA;
        f1838g = AbstractC0764a.a(gVarArrA);
        f1833b = new a(null);
    }

    private g(String str, int i3) {
    }

    private static final /* synthetic */ g[] a() {
        return new g[]{f1834c, f1835d, f1836e};
    }

    public static final String b(g gVar) {
        return f1833b.a(gVar);
    }

    public static g valueOf(String str) {
        return (g) Enum.valueOf(g.class, str);
    }

    public static g[] values() {
        return (g[]) f1837f.clone();
    }
}
