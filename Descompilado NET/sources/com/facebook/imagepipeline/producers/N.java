package com.facebook.imagepipeline.producers;

import java.io.FileInputStream;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class N extends M {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f6044c = new a(null);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(Executor executor, a0.i iVar) {
        super(executor, iVar);
        D2.h.f(executor, "executor");
        D2.h.f(iVar, "pooledByteBufferFactory");
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected O0.j d(U0.b bVar) {
        D2.h.f(bVar, "imageRequest");
        return e(new FileInputStream(bVar.u().toString()), (int) bVar.u().length());
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected String f() {
        return "LocalFileFetchProducer";
    }
}
