package R1;

import java.util.Locale;
import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class p {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f2095b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final p f2096c = new p("VISIBLE", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final p f2097d = new p("HIDDEN", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final p f2098e = new p("SCROLL", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ p[] f2099f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f2100g;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final p a(String str) {
            D2.h.f(str, "overflow");
            String lowerCase = str.toLowerCase(Locale.ROOT);
            D2.h.e(lowerCase, "toLowerCase(...)");
            int iHashCode = lowerCase.hashCode();
            if (iHashCode != -1217487446) {
                if (iHashCode != -907680051) {
                    if (iHashCode == 466743410 && lowerCase.equals("visible")) {
                        return p.f2096c;
                    }
                } else if (lowerCase.equals("scroll")) {
                    return p.f2098e;
                }
            } else if (lowerCase.equals("hidden")) {
                return p.f2097d;
            }
            return null;
        }

        private a() {
        }
    }

    static {
        p[] pVarArrA = a();
        f2099f = pVarArrA;
        f2100g = AbstractC0764a.a(pVarArrA);
        f2095b = new a(null);
    }

    private p(String str, int i3) {
    }

    private static final /* synthetic */ p[] a() {
        return new p[]{f2096c, f2097d, f2098e};
    }

    public static final p b(String str) {
        return f2095b.a(str);
    }

    public static p valueOf(String str) {
        return (p) Enum.valueOf(p.class, str);
    }

    public static p[] values() {
        return (p[]) f2099f.clone();
    }
}
