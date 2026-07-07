package a3;

import b3.D;
import b3.i;
import b3.l;
import b3.m;
import java.io.Closeable;
import java.io.IOException;
import java.util.zip.Deflater;

/* JADX INFO: loaded from: classes.dex */
public final class a implements Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final i f2866b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Deflater f2867c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final m f2868d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final boolean f2869e;

    public a(boolean z3) {
        this.f2869e = z3;
        i iVar = new i();
        this.f2866b = iVar;
        Deflater deflater = new Deflater(-1, true);
        this.f2867c = deflater;
        this.f2868d = new m((D) iVar, deflater);
    }

    private final boolean i(i iVar, l lVar) {
        return iVar.u0(iVar.F0() - ((long) lVar.v()), lVar);
    }

    public final void a(i iVar) throws IOException {
        D2.h.f(iVar, "buffer");
        if (!(this.f2866b.F0() == 0)) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        if (this.f2869e) {
            this.f2867c.reset();
        }
        this.f2868d.Q(iVar, iVar.F0());
        this.f2868d.flush();
        if (i(this.f2866b, b.f2870a)) {
            long jF0 = this.f2866b.F0() - ((long) 4);
            i.a aVarY0 = i.y0(this.f2866b, null, 1, null);
            try {
                aVarY0.i(jF0);
                A2.a.a(aVarY0, null);
            } finally {
            }
        } else {
            this.f2866b.L(0);
        }
        i iVar2 = this.f2866b;
        iVar.Q(iVar2, iVar2.F0());
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws Throwable {
        this.f2868d.close();
    }
}
