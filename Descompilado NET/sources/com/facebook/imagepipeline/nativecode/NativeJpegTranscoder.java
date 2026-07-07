package com.facebook.imagepipeline.nativecode;

import I0.h;
import O0.j;
import X.k;
import android.graphics.ColorSpace;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public class NativeJpegTranscoder implements W0.c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f5946a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f5947b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f5948c;

    public NativeJpegTranscoder(boolean z3, int i3, boolean z4, boolean z5) {
        this.f5946a = z3;
        this.f5947b = i3;
        this.f5948c = z4;
        if (z5) {
            g.a();
        }
    }

    public static void e(InputStream inputStream, OutputStream outputStream, int i3, int i4, int i5) {
        g.a();
        k.b(Boolean.valueOf(i4 >= 1));
        k.b(Boolean.valueOf(i4 <= 16));
        k.b(Boolean.valueOf(i5 >= 0));
        k.b(Boolean.valueOf(i5 <= 100));
        k.b(Boolean.valueOf(W0.e.j(i3)));
        k.c((i4 == 8 && i3 == 0) ? false : true, "no transformation requested");
        nativeTranscodeJpeg((InputStream) k.g(inputStream), (OutputStream) k.g(outputStream), i3, i4, i5);
    }

    public static void f(InputStream inputStream, OutputStream outputStream, int i3, int i4, int i5) {
        g.a();
        k.b(Boolean.valueOf(i4 >= 1));
        k.b(Boolean.valueOf(i4 <= 16));
        k.b(Boolean.valueOf(i5 >= 0));
        k.b(Boolean.valueOf(i5 <= 100));
        k.b(Boolean.valueOf(W0.e.i(i3)));
        k.c((i4 == 8 && i3 == 1) ? false : true, "no transformation requested");
        nativeTranscodeJpegWithExifOrientation((InputStream) k.g(inputStream), (OutputStream) k.g(outputStream), i3, i4, i5);
    }

    private static native void nativeTranscodeJpeg(InputStream inputStream, OutputStream outputStream, int i3, int i4, int i5);

    private static native void nativeTranscodeJpegWithExifOrientation(InputStream inputStream, OutputStream outputStream, int i3, int i4, int i5);

    @Override // W0.c
    public String a() {
        return "NativeJpegTranscoder";
    }

    @Override // W0.c
    public boolean b(D0.c cVar) {
        return cVar == D0.b.f135b;
    }

    @Override // W0.c
    public W0.b c(j jVar, OutputStream outputStream, h hVar, I0.g gVar, D0.c cVar, Integer num, ColorSpace colorSpace) {
        if (num == null) {
            num = 85;
        }
        if (hVar == null) {
            hVar = h.d();
        }
        int iB = W0.a.b(hVar, gVar, jVar, this.f5947b);
        try {
            int iF = W0.e.f(hVar, gVar, jVar, this.f5946a);
            int iA = W0.e.a(iB);
            if (this.f5948c) {
                iF = iA;
            }
            InputStream inputStreamP = jVar.P();
            if (W0.e.f2683b.contains(Integer.valueOf(jVar.s0()))) {
                f((InputStream) k.h(inputStreamP, "Cannot transcode from null input stream!"), outputStream, W0.e.d(hVar, jVar), iF, num.intValue());
            } else {
                e((InputStream) k.h(inputStreamP, "Cannot transcode from null input stream!"), outputStream, W0.e.e(hVar, jVar), iF, num.intValue());
            }
            X.b.b(inputStreamP);
            return new W0.b(iB != 1 ? 0 : 1);
        } catch (Throwable th) {
            X.b.b(null);
            throw th;
        }
    }

    @Override // W0.c
    public boolean d(j jVar, h hVar, I0.g gVar) {
        if (hVar == null) {
            hVar = h.d();
        }
        return W0.e.f(hVar, gVar, jVar, this.f5946a) < 8;
    }
}
