package q1;

import D2.h;
import d2.C0518a;

/* JADX INFO: renamed from: q1.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0652c implements AutoCloseable {
    public C0652c(String str) {
        h.f(str, "sectionName");
        C0518a.c(0L, str);
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        C0518a.i(0L);
    }
}
