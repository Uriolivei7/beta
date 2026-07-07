package H2;

import D2.h;

/* JADX INFO: loaded from: classes.dex */
class e {
    public static final void a(boolean z3, Number number) {
        h.f(number, "step");
        if (z3) {
            return;
        }
        throw new IllegalArgumentException("Step must be positive, was: " + number + '.');
    }
}
