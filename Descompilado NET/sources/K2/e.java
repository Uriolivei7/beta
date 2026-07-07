package K2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
final class e implements J2.c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final CharSequence f824a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f825b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f826c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final C2.p f827d;

    public static final class a implements Iterator, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f828b = -1;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f829c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f830d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private H2.c f831e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private int f832f;

        a() {
            int iF = H2.d.f(e.this.f825b, 0, e.this.f824a.length());
            this.f829c = iF;
            this.f830d = iF;
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x0023  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private final void a() {
            /*
                r6 = this;
                int r0 = r6.f830d
                r1 = 0
                if (r0 >= 0) goto Lc
                r6.f828b = r1
                r0 = 0
                r6.f831e = r0
                goto L9e
            Lc:
                K2.e r0 = K2.e.this
                int r0 = K2.e.d(r0)
                r2 = -1
                r3 = 1
                if (r0 <= 0) goto L23
                int r0 = r6.f832f
                int r0 = r0 + r3
                r6.f832f = r0
                K2.e r4 = K2.e.this
                int r4 = K2.e.d(r4)
                if (r0 >= r4) goto L31
            L23:
                int r0 = r6.f830d
                K2.e r4 = K2.e.this
                java.lang.CharSequence r4 = K2.e.c(r4)
                int r4 = r4.length()
                if (r0 <= r4) goto L47
            L31:
                H2.c r0 = new H2.c
                int r1 = r6.f829c
                K2.e r4 = K2.e.this
                java.lang.CharSequence r4 = K2.e.c(r4)
                int r4 = K2.y.I(r4)
                r0.<init>(r1, r4)
                r6.f831e = r0
                r6.f830d = r2
                goto L9c
            L47:
                K2.e r0 = K2.e.this
                C2.p r0 = K2.e.b(r0)
                K2.e r4 = K2.e.this
                java.lang.CharSequence r4 = K2.e.c(r4)
                int r5 = r6.f830d
                java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
                java.lang.Object r0 = r0.b(r4, r5)
                r2.i r0 = (r2.C0686i) r0
                if (r0 != 0) goto L77
                H2.c r0 = new H2.c
                int r1 = r6.f829c
                K2.e r4 = K2.e.this
                java.lang.CharSequence r4 = K2.e.c(r4)
                int r4 = K2.y.I(r4)
                r0.<init>(r1, r4)
                r6.f831e = r0
                r6.f830d = r2
                goto L9c
            L77:
                java.lang.Object r2 = r0.a()
                java.lang.Number r2 = (java.lang.Number) r2
                int r2 = r2.intValue()
                java.lang.Object r0 = r0.b()
                java.lang.Number r0 = (java.lang.Number) r0
                int r0 = r0.intValue()
                int r4 = r6.f829c
                H2.c r4 = H2.d.i(r4, r2)
                r6.f831e = r4
                int r2 = r2 + r0
                r6.f829c = r2
                if (r0 != 0) goto L99
                r1 = r3
            L99:
                int r2 = r2 + r1
                r6.f830d = r2
            L9c:
                r6.f828b = r3
            L9e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: K2.e.a.a():void");
        }

        @Override // java.util.Iterator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public H2.c next() {
            if (this.f828b == -1) {
                a();
            }
            if (this.f828b == 0) {
                throw new NoSuchElementException();
            }
            H2.c cVar = this.f831e;
            D2.h.d(cVar, "null cannot be cast to non-null type kotlin.ranges.IntRange");
            this.f831e = null;
            this.f828b = -1;
            return cVar;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f828b == -1) {
                a();
            }
            return this.f828b == 1;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    public e(CharSequence charSequence, int i3, int i4, C2.p pVar) {
        D2.h.f(charSequence, "input");
        D2.h.f(pVar, "getNextMatch");
        this.f824a = charSequence;
        this.f825b = i3;
        this.f826c = i4;
        this.f827d = pVar;
    }

    @Override // J2.c
    public Iterator iterator() {
        return new a();
    }
}
