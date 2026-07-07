package a3;

import b3.F;
import b3.i;
import b3.r;
import java.io.Closeable;
import java.io.IOException;
import java.util.zip.Inflater;

/* JADX INFO: loaded from: classes.dex */
public final class c implements Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final i f2871b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Inflater f2872c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final r f2873d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final boolean f2874e;

    public c(boolean z3) {
        this.f2874e = z3;
        i iVar = new i();
        this.f2871b = iVar;
        Inflater inflater = new Inflater(true);
        this.f2872c = inflater;
        this.f2873d = new r((F) iVar, inflater);
    }

    public final void a(i iVar) throws IOException {
        D2.h.f(iVar, "buffer");
        if (!(this.f2871b.F0() == 0)) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        if (this.f2874e) {
            this.f2872c.reset();
        }
        this.f2871b.T(iVar);
        this.f2871b.E(65535);
        long bytesRead = this.f2872c.getBytesRead() + this.f2871b.F0();
        do {
            this.f2873d.a(iVar, Long.MAX_VALUE);
        } while (this.f2872c.getBytesRead() < bytesRead);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f2873d.close();
    }
}
