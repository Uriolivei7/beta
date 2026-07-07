package com.facebook.imagepipeline.producers;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import java.io.IOException;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class I extends M {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f6017d = new a(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final AssetManager f6018c;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String b(U0.b bVar) {
            String path = bVar.v().getPath();
            D2.h.c(path);
            String strSubstring = path.substring(1);
            D2.h.e(strSubstring, "substring(...)");
            return strSubstring;
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public I(Executor executor, a0.i iVar, AssetManager assetManager) {
        super(executor, iVar);
        D2.h.f(executor, "executor");
        D2.h.f(iVar, "pooledByteBufferFactory");
        D2.h.f(assetManager, "assetManager");
        this.f6018c = assetManager;
    }

    private final int g(U0.b bVar) {
        AssetFileDescriptor assetFileDescriptorOpenFd = null;
        try {
            assetFileDescriptorOpenFd = this.f6018c.openFd(f6017d.b(bVar));
            int length = (int) assetFileDescriptorOpenFd.getLength();
            try {
                assetFileDescriptorOpenFd.close();
                return length;
            } catch (IOException unused) {
                return length;
            }
        } catch (IOException unused2) {
            if (assetFileDescriptorOpenFd != null) {
                try {
                    assetFileDescriptorOpenFd.close();
                } catch (IOException unused3) {
                }
            }
            return -1;
        } catch (Throwable th) {
            if (assetFileDescriptorOpenFd != null) {
                try {
                    assetFileDescriptorOpenFd.close();
                } catch (IOException unused4) {
                }
            }
            throw th;
        }
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected O0.j d(U0.b bVar) {
        D2.h.f(bVar, "imageRequest");
        return e(this.f6018c.open(f6017d.b(bVar), 2), g(bVar));
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected String f() {
        return "LocalAssetFetchProducer";
    }
}
