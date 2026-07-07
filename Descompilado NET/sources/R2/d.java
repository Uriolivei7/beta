package R2;

import M2.C0190a;
import M2.F;
import M2.r;
import M2.u;
import M2.z;
import R2.k;
import U2.n;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private k.b f2125a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private k f2126b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f2127c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f2128d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f2129e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private F f2130f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final h f2131g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final C0190a f2132h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final e f2133i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final r f2134j;

    public d(h hVar, C0190a c0190a, e eVar, r rVar) {
        D2.h.f(hVar, "connectionPool");
        D2.h.f(c0190a, "address");
        D2.h.f(eVar, "call");
        D2.h.f(rVar, "eventListener");
        this.f2131g = hVar;
        this.f2132h = c0190a;
        this.f2133i = eVar;
        this.f2134j = rVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x014d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final R2.f b(int r15, int r16, int r17, int r18, boolean r19) throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 381
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: R2.d.b(int, int, int, int, boolean):R2.f");
    }

    private final f c(int i3, int i4, int i5, int i6, boolean z3, boolean z4) throws IOException {
        while (true) {
            f fVarB = b(i3, i4, i5, i6, z3);
            if (fVarB.u(z4)) {
                return fVarB;
            }
            fVarB.z();
            if (this.f2130f == null) {
                k.b bVar = this.f2125a;
                if (bVar != null ? bVar.b() : true) {
                    continue;
                } else {
                    k kVar = this.f2126b;
                    if (!(kVar != null ? kVar.b() : true)) {
                        throw new IOException("exhausted all routes");
                    }
                }
            }
        }
    }

    private final F f() {
        f fVarM;
        if (this.f2127c > 1 || this.f2128d > 1 || this.f2129e > 0 || (fVarM = this.f2133i.m()) == null) {
            return null;
        }
        synchronized (fVarM) {
            if (fVarM.q() != 0) {
                return null;
            }
            if (N2.c.g(fVarM.A().a().l(), this.f2132h.l())) {
                return fVarM.A();
            }
            return null;
        }
    }

    public final S2.d a(z zVar, S2.g gVar) {
        D2.h.f(zVar, "client");
        D2.h.f(gVar, "chain");
        try {
            return c(gVar.e(), gVar.g(), gVar.j(), zVar.E(), zVar.K(), !D2.h.b(gVar.h().h(), "GET")).w(zVar, gVar);
        } catch (j e4) {
            h(e4.c());
            throw e4;
        } catch (IOException e5) {
            h(e5);
            throw new j(e5);
        }
    }

    public final C0190a d() {
        return this.f2132h;
    }

    public final boolean e() {
        k kVar;
        if (this.f2127c == 0 && this.f2128d == 0 && this.f2129e == 0) {
            return false;
        }
        if (this.f2130f != null) {
            return true;
        }
        F f3 = f();
        if (f3 != null) {
            this.f2130f = f3;
            return true;
        }
        k.b bVar = this.f2125a;
        if ((bVar == null || !bVar.b()) && (kVar = this.f2126b) != null) {
            return kVar.b();
        }
        return true;
    }

    public final boolean g(u uVar) {
        D2.h.f(uVar, "url");
        u uVarL = this.f2132h.l();
        return uVar.l() == uVarL.l() && D2.h.b(uVar.h(), uVarL.h());
    }

    public final void h(IOException iOException) {
        D2.h.f(iOException, "e");
        this.f2130f = null;
        if ((iOException instanceof n) && ((n) iOException).f2667b == U2.b.REFUSED_STREAM) {
            this.f2127c++;
        } else if (iOException instanceof U2.a) {
            this.f2128d++;
        } else {
            this.f2129e++;
        }
    }
}
