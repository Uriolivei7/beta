package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import b0.AbstractC0306a;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class U implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Executor f6056a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ContentResolver f6057b;

    class a extends n0 {

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        final /* synthetic */ h0 f6058g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ f0 f6059h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ U0.b f6060i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(InterfaceC0354n interfaceC0354n, h0 h0Var, f0 f0Var, String str, h0 h0Var2, f0 f0Var2, U0.b bVar) {
            super(interfaceC0354n, h0Var, f0Var, str);
            this.f6058g = h0Var2;
            this.f6059h = f0Var2;
            this.f6060i = bVar;
        }

        @Override // com.facebook.imagepipeline.producers.n0, V.e
        protected void e(Exception exc) {
            super.e(exc);
            this.f6058g.e(this.f6059h, "VideoThumbnailProducer", false);
            this.f6059h.n0("local", "video");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // V.e
        /* JADX INFO: renamed from: j, reason: merged with bridge method [inline-methods] */
        public void b(AbstractC0306a abstractC0306a) {
            AbstractC0306a.D(abstractC0306a);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.n0
        /* JADX INFO: renamed from: k, reason: merged with bridge method [inline-methods] */
        public Map i(AbstractC0306a abstractC0306a) {
            return X.g.of("createdThumbnail", String.valueOf(abstractC0306a != null));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // V.e
        /* JADX INFO: renamed from: l, reason: merged with bridge method [inline-methods] */
        public AbstractC0306a c() throws Throwable {
            String strI;
            try {
                strI = U.this.i(this.f6060i);
            } catch (IllegalArgumentException unused) {
                strI = null;
            }
            Bitmap bitmapCreateVideoThumbnail = strI != null ? ThumbnailUtils.createVideoThumbnail(strI, U.g(this.f6060i)) : null;
            if (bitmapCreateVideoThumbnail == null) {
                bitmapCreateVideoThumbnail = U.h(U.this.f6057b, this.f6060i.v());
            }
            if (bitmapCreateVideoThumbnail == null) {
                return null;
            }
            O0.e eVarK0 = O0.e.k0(bitmapCreateVideoThumbnail, G0.d.b(), O0.n.f1480d, 0);
            this.f6059h.A("image_format", "thumbnail");
            eVarK0.q(this.f6059h.a());
            return AbstractC0306a.d0(eVarK0);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.n0, V.e
        /* JADX INFO: renamed from: m, reason: merged with bridge method [inline-methods] */
        public void f(AbstractC0306a abstractC0306a) {
            super.f(abstractC0306a);
            this.f6058g.e(this.f6059h, "VideoThumbnailProducer", abstractC0306a != null);
            this.f6059h.n0("local", "video");
        }
    }

    class b extends C0346f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ n0 f6062a;

        b(n0 n0Var) {
            this.f6062a = n0Var;
        }

        @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
        public void a() {
            this.f6062a.a();
        }
    }

    public U(Executor executor, ContentResolver contentResolver) {
        this.f6056a = executor;
        this.f6057b = contentResolver;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int g(U0.b bVar) {
        return (bVar.n() > 96 || bVar.m() > 96) ? 1 : 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap h(ContentResolver contentResolver, Uri uri) throws Throwable {
        MediaMetadataRetriever mediaMetadataRetriever;
        MediaMetadataRetriever mediaMetadataRetriever2 = null;
        try {
            ParcelFileDescriptor parcelFileDescriptorOpenFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
            X.k.g(parcelFileDescriptorOpenFileDescriptor);
            mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                mediaMetadataRetriever.setDataSource(parcelFileDescriptorOpenFileDescriptor.getFileDescriptor());
                Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime(-1L);
                try {
                    mediaMetadataRetriever.release();
                } catch (IOException unused) {
                }
                return frameAtTime;
            } catch (FileNotFoundException unused2) {
                if (mediaMetadataRetriever != null) {
                    try {
                        mediaMetadataRetriever.release();
                    } catch (IOException unused3) {
                    }
                }
                return null;
            } catch (Throwable th) {
                th = th;
                mediaMetadataRetriever2 = mediaMetadataRetriever;
                if (mediaMetadataRetriever2 != null) {
                    try {
                        mediaMetadataRetriever2.release();
                    } catch (IOException unused4) {
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException unused5) {
            mediaMetadataRetriever = null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String i(U0.b bVar) {
        return f0.f.e(this.f6057b, bVar.v());
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        h0 h0VarP = f0Var.P();
        U0.b bVarX = f0Var.X();
        f0Var.n0("local", "video");
        a aVar = new a(interfaceC0354n, h0VarP, f0Var, "VideoThumbnailProducer", h0VarP, f0Var, bVarX);
        f0Var.a0(new b(aVar));
        this.f6056a.execute(aVar);
    }
}
