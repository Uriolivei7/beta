package u2;

import D2.h;
import java.util.Comparator;

/* JADX INFO: renamed from: u2.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
final class C0751f implements Comparator {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0751f f10870a = new C0751f();

    private C0751f() {
    }

    @Override // java.util.Comparator
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(Comparable comparable, Comparable comparable2) {
        h.f(comparable, "a");
        h.f(comparable2, "b");
        return comparable2.compareTo(comparable);
    }

    @Override // java.util.Comparator
    public final Comparator reversed() {
        return C0750e.f10869a;
    }
}
