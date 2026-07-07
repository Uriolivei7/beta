package com.facebook.imagepipeline.platform;

import X.a;
import X.b;
import X.k;
import X.p;
import a0.h;
import a0.j;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.MemoryFile;
import b0.AbstractC0306a;
import com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder;
import d0.C0489a;
import g0.C0541b;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class GingerbreadPurgeableDecoder extends DalvikPurgeableDecoder {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Method f5955c;

    public GingerbreadPurgeableDecoder() {
        C0541b.i();
    }

    private static MemoryFile h(AbstractC0306a abstractC0306a, int i3, byte[] bArr) throws Throwable {
        OutputStream outputStream;
        C0489a c0489a;
        j jVar;
        j jVar2 = null;
        OutputStream outputStream2 = null;
        MemoryFile memoryFile = new MemoryFile(null, (bArr == null ? 0 : bArr.length) + i3);
        memoryFile.allowPurging(false);
        try {
            jVar = new j((h) abstractC0306a.P());
            try {
                c0489a = new C0489a(jVar, i3);
            } catch (Throwable th) {
                th = th;
                outputStream = null;
                c0489a = null;
            }
        } catch (Throwable th2) {
            th = th2;
            outputStream = null;
            c0489a = null;
        }
        try {
            outputStream2 = memoryFile.getOutputStream();
            a.a(c0489a, outputStream2);
            if (bArr != null) {
                memoryFile.writeBytes(bArr, 0, i3, bArr.length);
            }
            AbstractC0306a.D(abstractC0306a);
            b.b(jVar);
            b.b(c0489a);
            b.a(outputStream2, true);
            return memoryFile;
        } catch (Throwable th3) {
            th = th3;
            outputStream = outputStream2;
            jVar2 = jVar;
            AbstractC0306a.D(abstractC0306a);
            b.b(jVar2);
            b.b(c0489a);
            b.a(outputStream, true);
            throw th;
        }
    }

    private Bitmap i(AbstractC0306a abstractC0306a, int i3, byte[] bArr, BitmapFactory.Options options) {
        MemoryFile memoryFileH = null;
        try {
            try {
                memoryFileH = h(abstractC0306a, i3, bArr);
                k(memoryFileH);
                throw new IllegalStateException("WebpBitmapFactory is null");
            } catch (IOException e4) {
                throw p.a(e4);
            }
        } catch (Throwable th) {
            if (memoryFileH != null) {
                memoryFileH.close();
            }
            throw th;
        }
    }

    private synchronized Method j() {
        if (f5955c == null) {
            try {
                f5955c = MemoryFile.class.getDeclaredMethod("getFileDescriptor", new Class[0]);
            } catch (Exception e4) {
                throw p.a(e4);
            }
        }
        return f5955c;
    }

    private FileDescriptor k(MemoryFile memoryFile) {
        try {
            return (FileDescriptor) k.g(j().invoke(memoryFile, new Object[0]));
        } catch (Exception e4) {
            throw p.a(e4);
        }
    }

    @Override // com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder
    protected Bitmap c(AbstractC0306a abstractC0306a, BitmapFactory.Options options) {
        return i(abstractC0306a, ((h) abstractC0306a.P()).size(), null, options);
    }

    @Override // com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder
    protected Bitmap d(AbstractC0306a abstractC0306a, int i3, BitmapFactory.Options options) {
        return i(abstractC0306a, i3, DalvikPurgeableDecoder.e(abstractC0306a, i3) ? null : DalvikPurgeableDecoder.f5944b, options);
    }
}
