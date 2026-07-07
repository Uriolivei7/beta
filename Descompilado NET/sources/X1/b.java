package X1;

import D2.h;
import J0.C0185t;
import J0.y;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b f2753a = new b();

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final X1.a f2754a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final X1.a f2755b;

        public a(X1.a aVar, X1.a aVar2) {
            this.f2754a = aVar;
            this.f2755b = aVar2;
        }
    }

    private b() {
    }

    public static final a a(int i3, int i4, List list) {
        h.f(list, "sources");
        return b(i3, i4, list, 1.0d);
    }

    public static final a b(int i3, int i4, List list, double d4) {
        h.f(list, "sources");
        if (list.isEmpty()) {
            return new a(null, null);
        }
        if (list.size() == 1) {
            return new a((X1.a) list.get(0), null);
        }
        if (i3 <= 0 || i4 <= 0) {
            return new a(null, null);
        }
        C0185t c0185tJ = y.l().j();
        h.e(c0185tJ, "getImagePipeline(...)");
        double d5 = ((double) (i3 * i4)) * d4;
        Iterator it = list.iterator();
        double d6 = Double.MAX_VALUE;
        double d7 = Double.MAX_VALUE;
        X1.a aVar = null;
        X1.a aVar2 = null;
        while (it.hasNext()) {
            X1.a aVar3 = (X1.a) it.next();
            double dAbs = Math.abs(1.0d - (aVar3.d() / d5));
            if (dAbs < d6) {
                aVar2 = aVar3;
                d6 = dAbs;
            }
            if (dAbs < d7 && aVar3.c() != E1.a.f197c && (c0185tJ.r(aVar3.f()) || c0185tJ.t(aVar3.f()))) {
                aVar = aVar3;
                d7 = dAbs;
            }
        }
        return new a(aVar2, (aVar == null || aVar2 == null || !h.b(aVar.e(), aVar2.e())) ? aVar : null);
    }
}
