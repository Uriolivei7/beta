package Q1;

import java.util.Locale;
import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f1812b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final d f1813c = new d("LINEAR", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final d f1814d = new d("EASE_IN", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final d f1815e = new d("EASE_OUT", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final d f1816f = new d("EASE_IN_EASE_OUT", 3);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final d f1817g = new d("SPRING", 4);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final /* synthetic */ d[] f1818h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f1819i;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        public final d a(String str) {
            D2.h.f(str, "name");
            String lowerCase = str.toLowerCase(Locale.ROOT);
            D2.h.e(lowerCase, "toLowerCase(...)");
            switch (lowerCase.hashCode()) {
                case -1965056864:
                    if (lowerCase.equals("easeout")) {
                        return d.f1815e;
                    }
                    break;
                case -1310315117:
                    if (lowerCase.equals("easein")) {
                        return d.f1814d;
                    }
                    break;
                case -1102672091:
                    if (lowerCase.equals("linear")) {
                        return d.f1813c;
                    }
                    break;
                case -895679987:
                    if (lowerCase.equals("spring")) {
                        return d.f1817g;
                    }
                    break;
                case 1164546989:
                    if (lowerCase.equals("easeineaseout")) {
                        return d.f1816f;
                    }
                    break;
            }
            throw new IllegalArgumentException("Unsupported interpolation type : " + str);
        }

        private a() {
        }
    }

    static {
        d[] dVarArrA = a();
        f1818h = dVarArrA;
        f1819i = AbstractC0764a.a(dVarArrA);
        f1812b = new a(null);
    }

    private d(String str, int i3) {
    }

    private static final /* synthetic */ d[] a() {
        return new d[]{f1813c, f1814d, f1815e, f1816f, f1817g};
    }

    public static final d b(String str) {
        return f1812b.a(str);
    }

    public static d valueOf(String str) {
        return (d) Enum.valueOf(d.class, str);
    }

    public static d[] values() {
        return (d[]) f1818h.clone();
    }
}
