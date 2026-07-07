package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Pair;
import b0.AbstractC0306a;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class LocalExifThumbnailProducer implements v0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Executor f6028a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final a0.i f6029b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ContentResolver f6030c;

    private class Api24Utils {
        ExifInterface a(FileDescriptor fileDescriptor) {
            return new ExifInterface(fileDescriptor);
        }

        private Api24Utils() {
        }
    }

    class a extends n0 {

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        final /* synthetic */ U0.b f6032g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(InterfaceC0354n interfaceC0354n, h0 h0Var, f0 f0Var, String str, U0.b bVar) {
            super(interfaceC0354n, h0Var, f0Var, str);
            this.f6032g = bVar;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // V.e
        /* JADX INFO: renamed from: j, reason: merged with bridge method [inline-methods] */
        public void b(O0.j jVar) {
            O0.j.o(jVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.n0
        /* JADX INFO: renamed from: k, reason: merged with bridge method [inline-methods] */
        public Map i(O0.j jVar) {
            return X.g.of("createdThumbnail", Boolean.toString(jVar != null));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // V.e
        /* JADX INFO: renamed from: l, reason: merged with bridge method [inline-methods] */
        public O0.j c() {
            ExifInterface exifInterfaceG = LocalExifThumbnailProducer.this.g(this.f6032g.v());
            if (exifInterfaceG == null || !exifInterfaceG.hasThumbnail()) {
                return null;
            }
            return LocalExifThumbnailProducer.this.e(LocalExifThumbnailProducer.this.f6029b.c((byte[]) X.k.g(exifInterfaceG.getThumbnail())), exifInterfaceG);
        }
    }

    class b extends C0346f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ n0 f6034a;

        b(n0 n0Var) {
            this.f6034a = n0Var;
        }

        @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
        public void a() {
            this.f6034a.a();
        }
    }

    public LocalExifThumbnailProducer(Executor executor, a0.i iVar, ContentResolver contentResolver) {
        this.f6028a = executor;
        this.f6029b = iVar;
        this.f6030c = contentResolver;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public O0.j e(a0.h hVar, ExifInterface exifInterface) {
        Pair pairD = Z0.e.d(new a0.j(hVar));
        int iH = h(exifInterface);
        int iIntValue = pairD != null ? ((Integer) pairD.first).intValue() : -1;
        int iIntValue2 = pairD != null ? ((Integer) pairD.second).intValue() : -1;
        AbstractC0306a abstractC0306aD0 = AbstractC0306a.d0(hVar);
        try {
            O0.j jVar = new O0.j(abstractC0306aD0);
            AbstractC0306a.D(abstractC0306aD0);
            jVar.E0(D0.b.f135b);
            jVar.F0(iH);
            jVar.I0(iIntValue);
            jVar.D0(iIntValue2);
            return jVar;
        } catch (Throwable th) {
            AbstractC0306a.D(abstractC0306aD0);
            throw th;
        }
    }

    private int h(ExifInterface exifInterface) {
        return Z0.h.a(Integer.parseInt((String) X.k.g(exifInterface.getAttribute("Orientation"))));
    }

    @Override // com.facebook.imagepipeline.producers.v0
    public boolean a(I0.g gVar) {
        return w0.b(512, 512, gVar);
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        h0 h0VarP = f0Var.P();
        U0.b bVarX = f0Var.X();
        f0Var.n0("local", "exif");
        a aVar = new a(interfaceC0354n, h0VarP, f0Var, "LocalExifThumbnailProducer", bVarX);
        f0Var.a0(new b(aVar));
        this.f6028a.execute(aVar);
    }

    boolean f(String str) {
        if (str == null) {
            return false;
        }
        File file = new File(str);
        return file.exists() && file.canRead();
    }

    ExifInterface g(Uri uri) {
        String strE = f0.f.e(this.f6030c, uri);
        if (strE == null) {
            return null;
        }
        try {
        } catch (IOException unused) {
        } catch (StackOverflowError unused2) {
            Y.a.i(LocalExifThumbnailProducer.class, "StackOverflowError in ExifInterface constructor");
        }
        if (f(strE)) {
            return new ExifInterface(strE);
        }
        AssetFileDescriptor assetFileDescriptorA = f0.f.a(this.f6030c, uri);
        if (assetFileDescriptorA != null) {
            ExifInterface exifInterfaceA = new Api24Utils().a(assetFileDescriptorA.getFileDescriptor());
            assetFileDescriptorA.close();
            return exifInterfaceA;
        }
        return null;
    }
}
