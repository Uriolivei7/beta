package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class j0 extends M {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f6157d = new a(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ContentResolver f6158c;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public j0(Executor executor, a0.i iVar, ContentResolver contentResolver) {
        super(executor, iVar);
        D2.h.f(executor, "executor");
        D2.h.f(iVar, "pooledByteBufferFactory");
        D2.h.f(contentResolver, "contentResolver");
        this.f6158c = contentResolver;
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected O0.j d(U0.b bVar) throws FileNotFoundException {
        D2.h.f(bVar, "imageRequest");
        InputStream inputStreamOpenInputStream = this.f6158c.openInputStream(bVar.v());
        if (inputStreamOpenInputStream == null) {
            throw new IllegalStateException("ContentResolver returned null InputStream");
        }
        O0.j jVarE = e(inputStreamOpenInputStream, -1);
        D2.h.e(jVarE, "getEncodedImage(...)");
        return jVarE;
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected String f() {
        return "QualifiedResourceFetchProducer";
    }
}
