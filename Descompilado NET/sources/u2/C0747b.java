package u2;

import D2.h;
import java.util.Comparator;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: u2.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0747b {
    public static int a(Comparable comparable, Comparable comparable2) {
        if (comparable == comparable2) {
            return 0;
        }
        if (comparable == null) {
            return -1;
        }
        if (comparable2 == null) {
            return 1;
        }
        return comparable.compareTo(comparable2);
    }

    public static Comparator b() {
        C0750e c0750e = C0750e.f10869a;
        h.d(c0750e, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.naturalOrder>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.naturalOrder> }");
        return c0750e;
    }

    public static Comparator c() {
        C0751f c0751f = C0751f.f10870a;
        h.d(c0751f, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reverseOrder>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reverseOrder> }");
        return c0751f;
    }
}
