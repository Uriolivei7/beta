package S2;

import K2.o;
import M2.D;
import M2.m;
import M2.n;
import M2.t;
import M2.u;
import b3.l;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final l f2320a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final l f2321b;

    static {
        l.a aVar = l.f5639f;
        f2320a = aVar.e("\"\\");
        f2321b = aVar.e("\t ,=");
    }

    public static final List a(t tVar, String str) {
        D2.h.f(tVar, "$this$parseChallenges");
        D2.h.f(str, "headerName");
        ArrayList arrayList = new ArrayList();
        int size = tVar.size();
        for (int i3 = 0; i3 < size; i3++) {
            if (o.n(str, tVar.b(i3), true)) {
                try {
                    c(new b3.i().h0(tVar.h(i3)), arrayList);
                } catch (EOFException e4) {
                    W2.j.f2732c.g().k("Unable to parse challenge", 5, e4);
                }
            }
        }
        return arrayList;
    }

    public static final boolean b(D d4) {
        D2.h.f(d4, "$this$promisesBody");
        if (D2.h.b(d4.y0().h(), "HEAD")) {
            return false;
        }
        int iA = d4.A();
        return (((iA >= 100 && iA < 200) || iA == 204 || iA == 304) && N2.c.s(d4) == -1 && !o.n("chunked", D.c0(d4, "Transfer-Encoding", null, 2, null), true)) ? false : true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x0085, code lost:
    
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0085, code lost:
    
        continue;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0090  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static final void c(b3.i r7, java.util.List r8) throws java.io.EOFException {
        /*
            r0 = 0
        L1:
            r1 = r0
        L2:
            if (r1 != 0) goto Le
            g(r7)
            java.lang.String r1 = e(r7)
            if (r1 != 0) goto Le
            return
        Le:
            boolean r2 = g(r7)
            java.lang.String r3 = e(r7)
            if (r3 != 0) goto L2c
            boolean r7 = r7.J()
            if (r7 != 0) goto L1f
            return
        L1f:
            M2.h r7 = new M2.h
            java.util.Map r0 = s2.AbstractC0696D.f()
            r7.<init>(r1, r0)
            r8.add(r7)
            return
        L2c:
            r4 = 61
            byte r4 = (byte) r4
            int r5 = N2.c.I(r7, r4)
            boolean r6 = g(r7)
            if (r2 != 0) goto L68
            if (r6 != 0) goto L41
            boolean r2 = r7.J()
            if (r2 == 0) goto L68
        L41:
            M2.h r2 = new M2.h
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r3)
            java.lang.String r3 = "="
            java.lang.String r3 = K2.o.r(r3, r5)
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            java.util.Map r3 = java.util.Collections.singletonMap(r0, r3)
            java.lang.String r4 = "Collections.singletonMap…ek + \"=\".repeat(eqCount))"
            D2.h.e(r3, r4)
            r2.<init>(r1, r3)
            r8.add(r2)
            goto L1
        L68:
            java.util.LinkedHashMap r2 = new java.util.LinkedHashMap
            r2.<init>()
            int r6 = N2.c.I(r7, r4)
            int r5 = r5 + r6
        L72:
            if (r3 != 0) goto L83
            java.lang.String r3 = e(r7)
            boolean r5 = g(r7)
            if (r5 == 0) goto L7f
            goto L85
        L7f:
            int r5 = N2.c.I(r7, r4)
        L83:
            if (r5 != 0) goto L90
        L85:
            M2.h r4 = new M2.h
            r4.<init>(r1, r2)
            r8.add(r4)
            r1 = r3
            goto L2
        L90:
            r6 = 1
            if (r5 <= r6) goto L94
            return
        L94:
            boolean r6 = g(r7)
            if (r6 == 0) goto L9b
            return
        L9b:
            r6 = 34
            byte r6 = (byte) r6
            boolean r6 = h(r7, r6)
            if (r6 == 0) goto La9
            java.lang.String r6 = d(r7)
            goto Lad
        La9:
            java.lang.String r6 = e(r7)
        Lad:
            if (r6 == 0) goto Lc7
            java.lang.Object r3 = r2.put(r3, r6)
            java.lang.String r3 = (java.lang.String) r3
            if (r3 == 0) goto Lb8
            return
        Lb8:
            boolean r3 = g(r7)
            if (r3 != 0) goto Lc5
            boolean r3 = r7.J()
            if (r3 != 0) goto Lc5
            return
        Lc5:
            r3 = r0
            goto L72
        Lc7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: S2.e.c(b3.i, java.util.List):void");
    }

    private static final String d(b3.i iVar) throws EOFException {
        byte b4 = (byte) 34;
        if (!(iVar.r0() == b4)) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        b3.i iVar2 = new b3.i();
        while (true) {
            long jN0 = iVar.n0(f2320a);
            if (jN0 == -1) {
                return null;
            }
            if (iVar.a0(jN0) == b4) {
                iVar2.Q(iVar, jN0);
                iVar.r0();
                return iVar2.O();
            }
            if (iVar.F0() == jN0 + 1) {
                return null;
            }
            iVar2.Q(iVar, jN0);
            iVar.r0();
            iVar2.Q(iVar, 1L);
        }
    }

    private static final String e(b3.i iVar) {
        long jN0 = iVar.n0(f2321b);
        if (jN0 == -1) {
            jN0 = iVar.F0();
        }
        if (jN0 != 0) {
            return iVar.D0(jN0);
        }
        return null;
    }

    public static final void f(n nVar, u uVar, t tVar) {
        D2.h.f(nVar, "$this$receiveHeaders");
        D2.h.f(uVar, "url");
        D2.h.f(tVar, "headers");
        if (nVar == n.f1202a) {
            return;
        }
        List listE = m.f1183n.e(uVar, tVar);
        if (listE.isEmpty()) {
            return;
        }
        nVar.a(uVar, listE);
    }

    private static final boolean g(b3.i iVar) throws EOFException {
        boolean z3 = false;
        while (!iVar.J()) {
            byte bA0 = iVar.a0(0L);
            if (bA0 == 9 || bA0 == 32) {
                iVar.r0();
            } else {
                if (bA0 != 44) {
                    break;
                }
                iVar.r0();
                z3 = true;
            }
        }
        return z3;
    }

    private static final boolean h(b3.i iVar, byte b4) {
        return !iVar.J() && iVar.a0(0L) == b4;
    }
}
