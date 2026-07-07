package S2;

import K2.o;
import M2.B;
import M2.C;
import M2.D;
import M2.E;
import M2.v;
import b3.t;
import java.io.IOException;
import java.net.ProtocolException;

/* JADX INFO: loaded from: classes.dex */
public final class b implements v {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f2316a;

    public b(boolean z3) {
        this.f2316a = z3;
    }

    @Override // M2.v
    public D a(v.a aVar) throws IOException {
        boolean z3;
        D.a aVarQ;
        D2.h.f(aVar, "chain");
        g gVar = (g) aVar;
        R2.c cVarF = gVar.f();
        D2.h.c(cVarF);
        B bH = gVar.h();
        C cA = bH.a();
        long jCurrentTimeMillis = System.currentTimeMillis();
        cVarF.v(bH);
        if (!f.b(bH.h()) || cA == null) {
            cVarF.o();
            z3 = true;
            aVarQ = null;
        } else {
            if (o.n("100-continue", bH.d("Expect"), true)) {
                cVarF.f();
                aVarQ = cVarF.q(true);
                cVarF.s();
                z3 = false;
            } else {
                z3 = true;
                aVarQ = null;
            }
            if (aVarQ != null) {
                cVarF.o();
                if (!cVarF.h().v()) {
                    cVarF.n();
                }
            } else if (cA.f()) {
                cVarF.f();
                cA.h(t.c(cVarF.c(bH, true)));
            } else {
                b3.j jVarC = t.c(cVarF.c(bH, false));
                cA.h(jVarC);
                jVarC.close();
            }
        }
        if (cA == null || !cA.f()) {
            cVarF.e();
        }
        if (aVarQ == null) {
            aVarQ = cVarF.q(false);
            D2.h.c(aVarQ);
            if (z3) {
                cVarF.s();
                z3 = false;
            }
        }
        D dC = aVarQ.r(bH).i(cVarF.h().r()).s(jCurrentTimeMillis).q(System.currentTimeMillis()).c();
        int iA = dC.A();
        if (iA == 100) {
            D.a aVarQ2 = cVarF.q(false);
            D2.h.c(aVarQ2);
            if (z3) {
                cVarF.s();
            }
            dC = aVarQ2.r(bH).i(cVarF.h().r()).s(jCurrentTimeMillis).q(System.currentTimeMillis()).c();
            iA = dC.A();
        }
        cVarF.r(dC);
        D dC2 = (this.f2316a && iA == 101) ? dC.u0().b(N2.c.f1404c).c() : dC.u0().b(cVarF.p(dC)).c();
        if (o.n("close", dC2.y0().d("Connection"), true) || o.n("close", D.c0(dC2, "Connection", null, 2, null), true)) {
            cVarF.n();
        }
        if (iA == 204 || iA == 205) {
            E eQ = dC2.q();
            if ((eQ != null ? eQ.q() : -1L) > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("HTTP ");
                sb.append(iA);
                sb.append(" had non-zero Content-Length: ");
                E eQ2 = dC2.q();
                sb.append(eQ2 != null ? Long.valueOf(eQ2.q()) : null);
                throw new ProtocolException(sb.toString());
            }
        }
        return dC2;
    }
}
