package R1;

import java.util.Locale;
import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class o {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f2089b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final o f2090c = new o("SOLID", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final o f2091d = new o("DASHED", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final o f2092e = new o("DOTTED", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ o[] f2093f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f2094g;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final o a(String str) {
            D2.h.f(str, "outlineStyle");
            String lowerCase = str.toLowerCase(Locale.ROOT);
            D2.h.e(lowerCase, "toLowerCase(...)");
            int iHashCode = lowerCase.hashCode();
            if (iHashCode != -1338941519) {
                if (iHashCode != -1325970902) {
                    if (iHashCode == 109618859 && lowerCase.equals("solid")) {
                        return o.f2090c;
                    }
                } else if (lowerCase.equals("dotted")) {
                    return o.f2092e;
                }
            } else if (lowerCase.equals("dashed")) {
                return o.f2091d;
            }
            return null;
        }

        private a() {
        }
    }

    static {
        o[] oVarArrA = a();
        f2093f = oVarArrA;
        f2094g = AbstractC0764a.a(oVarArrA);
        f2089b = new a(null);
    }

    private o(String str, int i3) {
    }

    private static final /* synthetic */ o[] a() {
        return new o[]{f2090c, f2091d, f2092e};
    }

    public static final o b(String str) {
        return f2089b.a(str);
    }

    public static o valueOf(String str) {
        return (o) Enum.valueOf(o.class, str);
    }

    public static o[] values() {
        return (o[]) f2093f.clone();
    }
}
