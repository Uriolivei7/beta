package Z0;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final j f2811a = new j();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Class f2812b = j.class;

    private static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f2813a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f2814b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f2815c;

        public final int a() {
            return this.f2814b;
        }

        public final int b() {
            return this.f2815c;
        }

        public final boolean c() {
            return this.f2813a;
        }

        public final void d(int i3) {
            this.f2814b = i3;
        }

        public final void e(int i3) {
            this.f2815c = i3;
        }

        public final void f(boolean z3) {
            this.f2813a = z3;
        }
    }

    private j() {
    }

    public static final int a(int i3) {
        if (i3 == 0 || i3 == 1) {
            return 0;
        }
        if (i3 == 3) {
            return 180;
        }
        if (i3 != 6) {
            return i3 != 8 ? 0 : 270;
        }
        return 90;
    }

    private final int b(InputStream inputStream, int i3, boolean z3) {
        if (i3 >= 10 && i.a(inputStream, 2, z3) == 3 && i.a(inputStream, 4, z3) == 1) {
            return i.a(inputStream, 2, z3);
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0027, code lost:
    
        return 0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final int c(java.io.InputStream r7, int r8, boolean r9, int r10) throws java.io.IOException {
        /*
            r6 = this;
            r0 = 14
            r1 = 0
            if (r8 >= r0) goto L6
            return r1
        L6:
            r0 = 2
            int r2 = Z0.i.a(r7, r0, r9)
            int r8 = r8 + (-2)
        Ld:
            int r3 = r2 + (-1)
            if (r2 <= 0) goto L27
            r2 = 12
            if (r8 < r2) goto L27
            int r2 = Z0.i.a(r7, r0, r9)
            int r4 = r8 + (-2)
            if (r2 != r10) goto L1e
            return r4
        L1e:
            r4 = 10
            r7.skip(r4)
            int r8 = r8 + (-12)
            r2 = r3
            goto Ld
        L27:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: Z0.j.c(java.io.InputStream, int, boolean, int):int");
    }

    public static final int d(InputStream inputStream, int i3) throws IOException {
        D2.h.f(inputStream, "stream");
        a aVar = new a();
        j jVar = f2811a;
        int iE = jVar.e(inputStream, i3, aVar);
        int iB = aVar.b() - 8;
        if (iE == 0 || iB > iE) {
            return 0;
        }
        inputStream.skip(iB);
        return jVar.b(inputStream, jVar.c(inputStream, iE - iB, aVar.c(), 274), aVar.c());
    }

    private final int e(InputStream inputStream, int i3, a aVar) {
        if (i3 <= 8) {
            return 0;
        }
        aVar.d(i.a(inputStream, 4, false));
        if (aVar.a() != 1229531648 && aVar.a() != 1296891946) {
            Y.a.i(f2812b, "Invalid TIFF header");
            return 0;
        }
        aVar.f(aVar.a() == 1229531648);
        aVar.e(i.a(inputStream, 4, aVar.c()));
        int i4 = i3 - 8;
        if (aVar.b() >= 8 && aVar.b() - 8 <= i4) {
            return i4;
        }
        Y.a.i(f2812b, "Invalid offset");
        return 0;
    }
}
