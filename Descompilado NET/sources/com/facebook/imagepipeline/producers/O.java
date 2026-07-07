package com.facebook.imagepipeline.producers;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import java.io.IOException;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class O extends M {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f6045d = new a(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Resources f6046c;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int b(U0.b bVar) {
            String path = bVar.v().getPath();
            if (path == null) {
                throw new IllegalStateException("Required value was null.");
            }
            String strSubstring = path.substring(1);
            D2.h.e(strSubstring, "substring(...)");
            return Integer.parseInt(strSubstring);
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(Executor executor, a0.i iVar, Resources resources) {
        super(executor, iVar);
        D2.h.f(executor, "executor");
        D2.h.f(iVar, "pooledByteBufferFactory");
        D2.h.f(resources, "resources");
        this.f6046c = resources;
    }

    private final int g(U0.b bVar) {
        AssetFileDescriptor assetFileDescriptorOpenRawResourceFd = null;
        try {
            assetFileDescriptorOpenRawResourceFd = this.f6046c.openRawResourceFd(f6045d.b(bVar));
            int length = (int) assetFileDescriptorOpenRawResourceFd.getLength();
            try {
                assetFileDescriptorOpenRawResourceFd.close();
                return length;
            } catch (IOException unused) {
                return length;
            }
        } catch (Resources.NotFoundException unused2) {
            if (assetFileDescriptorOpenRawResourceFd != null) {
                try {
                    assetFileDescriptorOpenRawResourceFd.close();
                } catch (IOException unused3) {
                }
            }
            return -1;
        } catch (Throwable th) {
            if (assetFileDescriptorOpenRawResourceFd != null) {
                try {
                    assetFileDescriptorOpenRawResourceFd.close();
                } catch (IOException unused4) {
                }
            }
            throw th;
        }
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected O0.j d(U0.b bVar) {
        D2.h.f(bVar, "imageRequest");
        return e(this.f6046c.openRawResource(f6045d.b(bVar)), g(bVar));
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected String f() {
        return "LocalResourceFetchProducer";
    }
}
