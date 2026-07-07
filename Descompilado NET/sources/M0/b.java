package M0;

import O0.j;
import O0.o;
import X.k;
import X.n;
import android.graphics.ColorSpace;
import b0.AbstractC0306a;
import java.io.InputStream;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class b implements c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final c f870a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final c f871b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final c f872c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final S0.f f873d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final n f874e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final c f875f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final Map f876g;

    class a implements c {
        a() {
        }

        @Override // M0.c
        public O0.d a(j jVar, int i3, o oVar, I0.d dVar) {
            ColorSpace colorSpaceZ;
            D0.c cVarD = jVar.D();
            if (((Boolean) b.this.f874e.get()).booleanValue()) {
                colorSpaceZ = dVar.f400k;
                if (colorSpaceZ == null) {
                    colorSpaceZ = jVar.z();
                }
            } else {
                colorSpaceZ = dVar.f400k;
            }
            ColorSpace colorSpace = colorSpaceZ;
            if (cVarD == D0.b.f135b) {
                return b.this.f(jVar, i3, oVar, dVar, colorSpace);
            }
            if (cVarD == D0.b.f137d) {
                return b.this.e(jVar, i3, oVar, dVar);
            }
            if (cVarD == D0.b.f144k) {
                return b.this.d(jVar, i3, oVar, dVar);
            }
            if (cVarD == D0.b.f147n) {
                return b.this.h(jVar, i3, oVar, dVar);
            }
            if (cVarD != D0.c.f151d) {
                return b.this.g(jVar, dVar);
            }
            throw new M0.a("unknown image format", jVar);
        }
    }

    public b(c cVar, c cVar2, c cVar3, S0.f fVar) {
        this(cVar, cVar2, cVar3, fVar, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public O0.d h(j jVar, int i3, o oVar, I0.d dVar) {
        c cVar = this.f872c;
        if (cVar != null) {
            return cVar.a(jVar, i3, oVar, dVar);
        }
        return null;
    }

    @Override // M0.c
    public O0.d a(j jVar, int i3, o oVar, I0.d dVar) {
        InputStream inputStreamP;
        c cVar;
        c cVar2 = dVar.f399j;
        if (cVar2 != null) {
            return cVar2.a(jVar, i3, oVar, dVar);
        }
        D0.c cVarD = jVar.D();
        if ((cVarD == null || cVarD == D0.c.f151d) && (inputStreamP = jVar.P()) != null) {
            cVarD = D0.e.d(inputStreamP);
            jVar.E0(cVarD);
        }
        Map map = this.f876g;
        return (map == null || (cVar = (c) map.get(cVarD)) == null) ? this.f875f.a(jVar, i3, oVar, dVar) : cVar.a(jVar, i3, oVar, dVar);
    }

    public O0.d d(j jVar, int i3, o oVar, I0.d dVar) {
        c cVar;
        return (dVar.f396g || (cVar = this.f871b) == null) ? g(jVar, dVar) : cVar.a(jVar, i3, oVar, dVar);
    }

    public O0.d e(j jVar, int i3, o oVar, I0.d dVar) {
        c cVar;
        if (jVar.h() == -1 || jVar.d() == -1) {
            throw new M0.a("image width or height is incorrect", jVar);
        }
        return (dVar.f396g || (cVar = this.f870a) == null) ? g(jVar, dVar) : cVar.a(jVar, i3, oVar, dVar);
    }

    public O0.e f(j jVar, int i3, o oVar, I0.d dVar, ColorSpace colorSpace) {
        AbstractC0306a abstractC0306aA = this.f873d.a(jVar, dVar.f397h, null, i3, colorSpace);
        try {
            X0.b.a(null, abstractC0306aA);
            k.g(abstractC0306aA);
            O0.e eVarI = O0.e.I(abstractC0306aA, oVar, jVar.N(), jVar.s0());
            eVarI.A("is_rounded", false);
            return eVarI;
        } finally {
            AbstractC0306a.D(abstractC0306aA);
        }
    }

    public O0.e g(j jVar, I0.d dVar) {
        AbstractC0306a abstractC0306aB = this.f873d.b(jVar, dVar.f397h, null, dVar.f400k);
        try {
            X0.b.a(null, abstractC0306aB);
            k.g(abstractC0306aB);
            O0.e eVarI = O0.e.I(abstractC0306aB, O0.n.f1480d, jVar.N(), jVar.s0());
            eVarI.A("is_rounded", false);
            return eVarI;
        } finally {
            AbstractC0306a.D(abstractC0306aB);
        }
    }

    public b(c cVar, c cVar2, c cVar3, S0.f fVar, Map<D0.c, c> map) {
        this(cVar, cVar2, cVar3, fVar, map, X.o.f2743b);
    }

    public b(c cVar, c cVar2, c cVar3, S0.f fVar, Map<D0.c, c> map, n nVar) {
        this.f875f = new a();
        this.f870a = cVar;
        this.f871b = cVar2;
        this.f872c = cVar3;
        this.f873d = fVar;
        this.f876g = map;
        this.f874e = nVar;
    }
}
