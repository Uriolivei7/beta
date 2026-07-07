package R0;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class y implements a0.i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final com.facebook.imagepipeline.memory.f f1994a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final a0.l f1995b;

    public y(com.facebook.imagepipeline.memory.f fVar, a0.l lVar) {
        D2.h.f(fVar, "pool");
        D2.h.f(lVar, "pooledByteStreams");
        this.f1994a = fVar;
        this.f1995b = lVar;
    }

    public final x f(InputStream inputStream, com.facebook.imagepipeline.memory.g gVar) {
        D2.h.f(inputStream, "inputStream");
        D2.h.f(gVar, "outputStream");
        this.f1995b.a(inputStream, gVar);
        return gVar.a();
    }

    @Override // a0.i
    /* JADX INFO: renamed from: g, reason: merged with bridge method [inline-methods] */
    public x d(InputStream inputStream) throws Throwable {
        D2.h.f(inputStream, "inputStream");
        com.facebook.imagepipeline.memory.g gVar = new com.facebook.imagepipeline.memory.g(this.f1994a, 0, 2, null);
        try {
            return f(inputStream, gVar);
        } finally {
            gVar.close();
        }
    }

    @Override // a0.i
    /* JADX INFO: renamed from: h, reason: merged with bridge method [inline-methods] */
    public x a(InputStream inputStream, int i3) throws Throwable {
        D2.h.f(inputStream, "inputStream");
        com.facebook.imagepipeline.memory.g gVar = new com.facebook.imagepipeline.memory.g(this.f1994a, i3);
        try {
            return f(inputStream, gVar);
        } finally {
            gVar.close();
        }
    }

    @Override // a0.i
    /* JADX INFO: renamed from: i, reason: merged with bridge method [inline-methods] */
    public x c(byte[] bArr) throws Throwable {
        D2.h.f(bArr, "bytes");
        com.facebook.imagepipeline.memory.g gVar = new com.facebook.imagepipeline.memory.g(this.f1994a, bArr.length);
        try {
            try {
                gVar.write(bArr, 0, bArr.length);
                return gVar.a();
            } catch (IOException e4) {
                throw X.p.a(e4);
            }
        } finally {
            gVar.close();
        }
    }

    @Override // a0.i
    /* JADX INFO: renamed from: j, reason: merged with bridge method [inline-methods] */
    public com.facebook.imagepipeline.memory.g b() {
        return new com.facebook.imagepipeline.memory.g(this.f1994a, 0, 2, null);
    }

    @Override // a0.i
    /* JADX INFO: renamed from: k, reason: merged with bridge method [inline-methods] */
    public com.facebook.imagepipeline.memory.g e(int i3) {
        return new com.facebook.imagepipeline.memory.g(this.f1994a, i3);
    }
}
