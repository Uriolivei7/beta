package b3;

import java.util.zip.Deflater;

/* JADX INFO: loaded from: classes.dex */
public final class m implements D {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f5643b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final j f5644c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Deflater f5645d;

    public m(j jVar, Deflater deflater) {
        D2.h.f(jVar, "sink");
        D2.h.f(deflater, "deflater");
        this.f5644c = jVar;
        this.f5645d = deflater;
    }

    private final void a(boolean z3) {
        A aI0;
        int iDeflate;
        i iVarE = this.f5644c.e();
        while (true) {
            aI0 = iVarE.I0(1);
            if (z3) {
                Deflater deflater = this.f5645d;
                byte[] bArr = aI0.f5590a;
                int i3 = aI0.f5592c;
                iDeflate = deflater.deflate(bArr, i3, 8192 - i3, 2);
            } else {
                Deflater deflater2 = this.f5645d;
                byte[] bArr2 = aI0.f5590a;
                int i4 = aI0.f5592c;
                iDeflate = deflater2.deflate(bArr2, i4, 8192 - i4);
            }
            if (iDeflate > 0) {
                aI0.f5592c += iDeflate;
                iVarE.E0(iVarE.F0() + ((long) iDeflate));
                this.f5644c.U();
            } else if (this.f5645d.needsInput()) {
                break;
            }
        }
        if (aI0.f5591b == aI0.f5592c) {
            iVarE.f5627b = aI0.b();
            B.b(aI0);
        }
    }

    @Override // b3.D
    public void Q(i iVar, long j3) {
        D2.h.f(iVar, "source");
        AbstractC0323f.b(iVar.F0(), 0L, j3);
        while (j3 > 0) {
            A a4 = iVar.f5627b;
            D2.h.c(a4);
            int iMin = (int) Math.min(j3, a4.f5592c - a4.f5591b);
            this.f5645d.setInput(a4.f5590a, a4.f5591b, iMin);
            a(false);
            long j4 = iMin;
            iVar.E0(iVar.F0() - j4);
            int i3 = a4.f5591b + iMin;
            a4.f5591b = i3;
            if (i3 == a4.f5592c) {
                iVar.f5627b = a4.b();
                B.b(a4);
            }
            j3 -= j4;
        }
    }

    @Override // b3.D, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws Throwable {
        if (this.f5643b) {
            return;
        }
        try {
            i();
            th = null;
        } catch (Throwable th) {
            th = th;
        }
        try {
            this.f5645d.end();
        } catch (Throwable th2) {
            if (th == null) {
                th = th2;
            }
        }
        try {
            this.f5644c.close();
        } catch (Throwable th3) {
            if (th == null) {
                th = th3;
            }
        }
        this.f5643b = true;
        if (th != null) {
            throw th;
        }
    }

    @Override // b3.D
    public G f() {
        return this.f5644c.f();
    }

    @Override // b3.D, java.io.Flushable
    public void flush() {
        a(true);
        this.f5644c.flush();
    }

    public final void i() {
        this.f5645d.finish();
        a(false);
    }

    public String toString() {
        return "DeflaterSink(" + this.f5644c + ')';
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public m(D d4, Deflater deflater) {
        this(t.c(d4), deflater);
        D2.h.f(d4, "sink");
        D2.h.f(deflater, "deflater");
    }
}
