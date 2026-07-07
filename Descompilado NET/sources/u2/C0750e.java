package u2;

import D2.h;
import java.util.Comparator;

/* JADX INFO: renamed from: u2.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
final class C0750e implements Comparator {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0750e f10869a = new C0750e();

    private C0750e() {
    }

    @Override // java.util.Comparator
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(Comparable comparable, Comparable comparable2) {
        h.f(comparable, "a");
        h.f(comparable2, "b");
        return comparable.compareTo(comparable2);
    }

    @Override // java.util.Comparator
    public final Comparator reversed() {
        return C0751f.f10870a;
    }
}
