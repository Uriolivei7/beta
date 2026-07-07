package M2;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public abstract class E implements Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f942b = new a(null);

    public static final class a {

        /* JADX INFO: renamed from: M2.E$a$a, reason: collision with other inner class name */
        public static final class C0015a extends E {

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ b3.k f943c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            final /* synthetic */ x f944d;

            /* JADX INFO: renamed from: e, reason: collision with root package name */
            final /* synthetic */ long f945e;

            C0015a(b3.k kVar, x xVar, long j3) {
                this.f943c = kVar;
                this.f944d = xVar;
                this.f945e = j3;
            }

            @Override // M2.E
            public long q() {
                return this.f945e;
            }

            @Override // M2.E
            public x v() {
                return this.f944d;
            }

            @Override // M2.E
            public b3.k z() {
                return this.f943c;
            }
        }

        private a() {
        }

        public static /* synthetic */ E d(a aVar, byte[] bArr, x xVar, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                xVar = null;
            }
            return aVar.c(bArr, xVar);
        }

        public final E a(x xVar, long j3, b3.k kVar) {
            D2.h.f(kVar, "content");
            return b(kVar, xVar, j3);
        }

        public final E b(b3.k kVar, x xVar, long j3) {
            D2.h.f(kVar, "$this$asResponseBody");
            return new C0015a(kVar, xVar, j3);
        }

        public final E c(byte[] bArr, x xVar) {
            D2.h.f(bArr, "$this$toResponseBody");
            return b(new b3.i().R(bArr), xVar, bArr.length);
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private final Charset o() {
        Charset charsetC;
        x xVarV = v();
        return (xVarV == null || (charsetC = xVarV.c(K2.d.f816b)) == null) ? K2.d.f816b : charsetC;
    }

    public static final E y(x xVar, long j3, b3.k kVar) {
        return f942b.a(xVar, j3, kVar);
    }

    public final String A() throws IOException {
        b3.k kVarZ = z();
        try {
            String strP0 = kVarZ.p0(N2.c.G(kVarZ, o()));
            A2.a.a(kVarZ, null);
            return strP0;
        } finally {
        }
    }

    public final InputStream a() {
        return z().q0();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        N2.c.j(z());
    }

    public final byte[] i() throws IOException {
        long jQ = q();
        if (jQ > Integer.MAX_VALUE) {
            throw new IOException("Cannot buffer entire body for content length: " + jQ);
        }
        b3.k kVarZ = z();
        try {
            byte[] bArrH = kVarZ.H();
            A2.a.a(kVarZ, null);
            int length = bArrH.length;
            if (jQ == -1 || jQ == length) {
                return bArrH;
            }
            throw new IOException("Content-Length (" + jQ + ") and stream length (" + length + ") disagree");
        } finally {
        }
    }

    public abstract long q();

    public abstract x v();

    public abstract b3.k z();
}
