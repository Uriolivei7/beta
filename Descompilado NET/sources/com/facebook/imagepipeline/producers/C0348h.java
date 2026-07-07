package com.facebook.imagepipeline.producers;

import android.util.Pair;
import b0.AbstractC0306a;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0348h extends V {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final H0.k f6144f;

    public C0348h(H0.k kVar, e0 e0Var) {
        super(e0Var, "BitmapMemoryCacheKeyMultiplexProducer", "multiplex_bmp_cnt");
        this.f6144f = kVar;
    }

    @Override // com.facebook.imagepipeline.producers.V
    /* JADX INFO: renamed from: l, reason: merged with bridge method [inline-methods] */
    public AbstractC0306a g(AbstractC0306a abstractC0306a) {
        return AbstractC0306a.A(abstractC0306a);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.producers.V
    /* JADX INFO: renamed from: m, reason: merged with bridge method [inline-methods] */
    public Pair j(f0 f0Var) {
        return Pair.create(this.f6144f.b(f0Var.X(), f0Var.i()), f0Var.d0());
    }
}
