package com.facebook.imagepipeline.producers;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0347g extends C0349i {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f6143d = new a(null);

    /* JADX INFO: renamed from: com.facebook.imagepipeline.producers.g$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0347g(H0.x xVar, H0.k kVar, e0 e0Var) {
        super(xVar, kVar, e0Var);
        D2.h.f(xVar, "memoryCache");
        D2.h.f(kVar, "cacheKeyFactory");
        D2.h.f(e0Var, "inputProducer");
    }

    @Override // com.facebook.imagepipeline.producers.C0349i
    protected String d() {
        return "pipe_ui";
    }

    @Override // com.facebook.imagepipeline.producers.C0349i
    protected String e() {
        return "BitmapMemoryCacheGetProducer";
    }

    @Override // com.facebook.imagepipeline.producers.C0349i
    protected InterfaceC0354n g(InterfaceC0354n interfaceC0354n, R.d dVar, boolean z3) {
        D2.h.f(interfaceC0354n, "consumer");
        D2.h.f(dVar, "cacheKey");
        return interfaceC0354n;
    }
}
