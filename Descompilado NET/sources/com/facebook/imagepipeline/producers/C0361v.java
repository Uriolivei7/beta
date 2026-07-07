package com.facebook.imagepipeline.producers;

import U0.b;
import java.util.Map;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.v, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0361v {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0361v f6260a = new C0361v();

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.v$a */
    public static final class a extends Exception {
        public a(String str) {
            super(str);
        }
    }

    private C0361v() {
    }

    public static final H0.j a(U0.b bVar, H0.j jVar, H0.j jVar2, Map map) {
        String strF;
        D2.h.f(bVar, "imageRequest");
        if (bVar.c() == b.EnumC0034b.SMALL) {
            return jVar;
        }
        if (bVar.c() == b.EnumC0034b.DEFAULT) {
            return jVar2;
        }
        if (bVar.c() != b.EnumC0034b.DYNAMIC || map == null || (strF = bVar.f()) == null) {
            return null;
        }
        return (H0.j) map.get(strF);
    }
}
