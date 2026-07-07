package com.facebook.imagepipeline.nativecode;

/* JADX INFO: loaded from: classes.dex */
public class NativeJpegTranscoderFactory implements W0.d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f5949a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f5950b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f5951c;

    public NativeJpegTranscoderFactory(int i3, boolean z3, boolean z4) {
        this.f5949a = i3;
        this.f5950b = z3;
        this.f5951c = z4;
    }

    @Override // W0.d
    public W0.c createImageTranscoder(D0.c cVar, boolean z3) {
        if (cVar != D0.b.f135b) {
            return null;
        }
        return new NativeJpegTranscoder(z3, this.f5949a, this.f5950b, this.f5951c);
    }
}
