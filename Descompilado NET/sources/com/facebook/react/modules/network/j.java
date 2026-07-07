package com.facebook.react.modules.network;

import M2.C;
import M2.x;
import b3.AbstractC0320c;
import b3.D;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public final class j extends C {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C f7012b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final i f7013c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f7014d;

    public j(C c4, i iVar) {
        D2.h.f(c4, "requestBody");
        D2.h.f(iVar, "progressListener");
        this.f7012b = c4;
        this.f7013c = iVar;
    }

    private final D j(b3.j jVar) {
        return AbstractC0320c.a().b(new a(jVar.j0()));
    }

    @Override // M2.C
    public long a() {
        if (this.f7014d == 0) {
            this.f7014d = this.f7012b.a();
        }
        return this.f7014d;
    }

    @Override // M2.C
    public x b() {
        return this.f7012b.b();
    }

    @Override // M2.C
    public void h(b3.j jVar) {
        D2.h.f(jVar, "sink");
        b3.j jVarA = AbstractC0320c.a().a(j(jVar));
        a();
        this.f7012b.h(jVarA);
        jVarA.flush();
    }

    public static final class a extends FilterOutputStream {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private long f7015b;

        a(OutputStream outputStream) {
            super(outputStream);
        }

        public final void a() {
            long j3 = this.f7015b;
            long jA = j.this.a();
            j.this.f7013c.a(j3, jA, j3 == jA);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] bArr, int i3, int i4) throws IOException {
            D2.h.f(bArr, "data");
            super.write(bArr, i3, i4);
            this.f7015b += (long) i4;
            a();
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(int i3) throws IOException {
            super.write(i3);
            this.f7015b++;
            a();
        }
    }
}
