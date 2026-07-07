package P2;

import C2.l;
import D2.h;
import b3.D;
import b3.i;
import b3.n;
import java.io.EOFException;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class e extends n {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f1792c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final l f1793d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public e(D d4, l lVar) {
        super(d4);
        h.f(d4, "delegate");
        h.f(lVar, "onException");
        this.f1793d = lVar;
    }

    @Override // b3.n, b3.D
    public void Q(i iVar, long j3) throws EOFException {
        h.f(iVar, "source");
        if (this.f1792c) {
            iVar.s(j3);
            return;
        }
        try {
            super.Q(iVar, j3);
        } catch (IOException e4) {
            this.f1792c = true;
            this.f1793d.d(e4);
        }
    }

    @Override // b3.n, b3.D, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.f1792c) {
            return;
        }
        try {
            super.close();
        } catch (IOException e4) {
            this.f1792c = true;
            this.f1793d.d(e4);
        }
    }

    @Override // b3.n, b3.D, java.io.Flushable
    public void flush() {
        if (this.f1792c) {
            return;
        }
        try {
            super.flush();
        } catch (IOException e4) {
            this.f1792c = true;
            this.f1793d.d(e4);
        }
    }
}
