package R1;

import java.util.Locale;
import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f2028b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final f f2029c = new f("SOLID", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final f f2030d = new f("DASHED", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final f f2031e = new f("DOTTED", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ f[] f2032f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f2033g;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final f a(String str) {
            D2.h.f(str, "borderStyle");
            String lowerCase = str.toLowerCase(Locale.ROOT);
            D2.h.e(lowerCase, "toLowerCase(...)");
            int iHashCode = lowerCase.hashCode();
            if (iHashCode != -1338941519) {
                if (iHashCode != -1325970902) {
                    if (iHashCode == 109618859 && lowerCase.equals("solid")) {
                        return f.f2029c;
                    }
                } else if (lowerCase.equals("dotted")) {
                    return f.f2031e;
                }
            } else if (lowerCase.equals("dashed")) {
                return f.f2030d;
            }
            return null;
        }

        private a() {
        }
    }

    static {
        f[] fVarArrA = a();
        f2032f = fVarArrA;
        f2033g = AbstractC0764a.a(fVarArrA);
        f2028b = new a(null);
    }

    private f(String str, int i3) {
    }

    private static final /* synthetic */ f[] a() {
        return new f[]{f2029c, f2030d, f2031e};
    }

    public static final f b(String str) {
        return f2028b.a(str);
    }

    public static f valueOf(String str) {
        return (f) Enum.valueOf(f.class, str);
    }

    public static f[] values() {
        return (f[]) f2032f.clone();
    }
}
