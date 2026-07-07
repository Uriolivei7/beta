package com.facebook.imagepipeline.producers;

import android.graphics.Bitmap;
import b0.AbstractC0306a;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.j, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0350j implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final e0 f6151a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f6152b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f6153c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final boolean f6154d;

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.j$a */
    private static class a extends AbstractC0360u {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f6155c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int f6156d;

        a(InterfaceC0354n interfaceC0354n, int i3, int i4) {
            super(interfaceC0354n);
            this.f6155c = i3;
            this.f6156d = i4;
        }

        private void q(AbstractC0306a abstractC0306a) {
            O0.d dVar;
            Bitmap bitmapC;
            int rowBytes;
            if (abstractC0306a == null || !abstractC0306a.a0() || (dVar = (O0.d) abstractC0306a.P()) == null || dVar.b() || !(dVar instanceof O0.e) || (bitmapC = ((O0.e) dVar).C()) == null || (rowBytes = bitmapC.getRowBytes() * bitmapC.getHeight()) < this.f6155c || rowBytes > this.f6156d) {
                return;
            }
            bitmapC.prepareToDraw();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.AbstractC0343c
        /* JADX INFO: renamed from: r, reason: merged with bridge method [inline-methods] */
        public void i(AbstractC0306a abstractC0306a, int i3) {
            q(abstractC0306a);
            p().d(abstractC0306a, i3);
        }
    }

    public C0350j(e0 e0Var, int i3, int i4, boolean z3) {
        X.k.b(Boolean.valueOf(i3 <= i4));
        this.f6151a = (e0) X.k.g(e0Var);
        this.f6152b = i3;
        this.f6153c = i4;
        this.f6154d = z3;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        if (!f0Var.v() || this.f6154d) {
            this.f6151a.b(new a(interfaceC0354n, this.f6152b, this.f6153c), f0Var);
        } else {
            this.f6151a.b(interfaceC0354n, f0Var);
        }
    }
}
