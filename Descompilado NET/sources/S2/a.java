package S2;

import K2.o;
import M2.B;
import M2.C;
import M2.D;
import M2.E;
import M2.m;
import M2.n;
import M2.v;
import M2.x;
import b3.q;
import b3.t;
import java.util.List;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class a implements v {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final n f2315a;

    public a(n nVar) {
        D2.h.f(nVar, "cookieJar");
        this.f2315a = nVar;
    }

    private final String b(List list) {
        StringBuilder sb = new StringBuilder();
        int i3 = 0;
        for (Object obj : list) {
            int i4 = i3 + 1;
            if (i3 < 0) {
                AbstractC0717n.p();
            }
            m mVar = (m) obj;
            if (i3 > 0) {
                sb.append("; ");
            }
            sb.append(mVar.g());
            sb.append('=');
            sb.append(mVar.i());
            i3 = i4;
        }
        String string = sb.toString();
        D2.h.e(string, "StringBuilder().apply(builderAction).toString()");
        return string;
    }

    @Override // M2.v
    public D a(v.a aVar) {
        E eQ;
        D2.h.f(aVar, "chain");
        B bI = aVar.i();
        B.a aVarI = bI.i();
        C cA = bI.a();
        if (cA != null) {
            x xVarB = cA.b();
            if (xVarB != null) {
                aVarI.e("Content-Type", xVarB.toString());
            }
            long jA = cA.a();
            if (jA != -1) {
                aVarI.e("Content-Length", String.valueOf(jA));
                aVarI.i("Transfer-Encoding");
            } else {
                aVarI.e("Transfer-Encoding", "chunked");
                aVarI.i("Content-Length");
            }
        }
        boolean z3 = false;
        if (bI.d("Host") == null) {
            aVarI.e("Host", N2.c.Q(bI.l(), false, 1, null));
        }
        if (bI.d("Connection") == null) {
            aVarI.e("Connection", "Keep-Alive");
        }
        if (bI.d("Accept-Encoding") == null && bI.d("Range") == null) {
            aVarI.e("Accept-Encoding", "gzip");
            z3 = true;
        }
        List listC = this.f2315a.c(bI.l());
        if (!listC.isEmpty()) {
            aVarI.e("Cookie", b(listC));
        }
        if (bI.d("User-Agent") == null) {
            aVarI.e("User-Agent", "okhttp/4.9.2");
        }
        D dA = aVar.a(aVarI.b());
        e.f(this.f2315a, bI.l(), dA.d0());
        D.a aVarR = dA.u0().r(bI);
        if (z3 && o.n("gzip", D.c0(dA, "Content-Encoding", null, 2, null), true) && e.b(dA) && (eQ = dA.q()) != null) {
            q qVar = new q(eQ.z());
            aVarR.k(dA.d0().e().h("Content-Encoding").h("Content-Length").e());
            aVarR.b(new h(D.c0(dA, "Content-Type", null, 2, null), -1L, t.d(qVar)));
        }
        return aVarR.c();
    }
}
