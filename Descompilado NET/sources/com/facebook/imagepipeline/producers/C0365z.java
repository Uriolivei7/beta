package com.facebook.imagepipeline.producers;

import android.util.Pair;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.z, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0365z extends V {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final H0.k f6277f;

    public C0365z(H0.k kVar, boolean z3, e0 e0Var) {
        super(e0Var, "EncodedCacheKeyMultiplexProducer", "multiplex_enc_cnt", z3);
        this.f6277f = kVar;
    }

    @Override // com.facebook.imagepipeline.producers.V
    /* JADX INFO: renamed from: l, reason: merged with bridge method [inline-methods] */
    public O0.j g(O0.j jVar) {
        return O0.j.i(jVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.producers.V
    /* JADX INFO: renamed from: m, reason: merged with bridge method [inline-methods] */
    public Pair j(f0 f0Var) {
        return Pair.create(this.f6277f.c(f0Var.X(), f0Var.i()), f0Var.d0());
    }
}
