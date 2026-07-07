package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.CancellationSignal;
import android.util.Size;
import b0.AbstractC0306a;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class T implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Executor f6047a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ContentResolver f6048b;

    class a extends n0 {

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        final /* synthetic */ h0 f6049g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ f0 f6050h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ U0.b f6051i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        final /* synthetic */ CancellationSignal f6052j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(InterfaceC0354n interfaceC0354n, h0 h0Var, f0 f0Var, String str, h0 h0Var2, f0 f0Var2, U0.b bVar, CancellationSignal cancellationSignal) {
            super(interfaceC0354n, h0Var, f0Var, str);
            this.f6049g = h0Var2;
            this.f6050h = f0Var2;
            this.f6051i = bVar;
            this.f6052j = cancellationSignal;
        }

        @Override // com.facebook.imagepipeline.producers.n0, V.e
        protected void d() {
            super.d();
            this.f6052j.cancel();
        }

        @Override // com.facebook.imagepipeline.producers.n0, V.e
        protected void e(Exception exc) {
            super.e(exc);
            this.f6049g.e(this.f6050h, "LocalThumbnailBitmapSdk29Producer", false);
            this.f6050h.n0("local", "thumbnail_bitmap");
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
        public AbstractC0306a c() throws IOException {
            String strE;
            Size size = new Size(this.f6051i.n(), this.f6051i.m());
            try {
                strE = T.this.e(this.f6051i);
            } catch (IllegalArgumentException unused) {
                strE = null;
            }
            Bitmap bitmapCreateVideoThumbnail = strE != null ? Z.a.c(Z.a.b(strE)) ? ThumbnailUtils.createVideoThumbnail(new File(strE), size, this.f6052j) : ThumbnailUtils.createImageThumbnail(new File(strE), size, this.f6052j) : null;
            if (bitmapCreateVideoThumbnail == null) {
                bitmapCreateVideoThumbnail = T.this.f6048b.loadThumbnail(this.f6051i.v(), size, this.f6052j);
            }
            if (bitmapCreateVideoThumbnail == null) {
                return null;
            }
            O0.e eVarK0 = O0.e.k0(bitmapCreateVideoThumbnail, G0.d.b(), O0.n.f1480d, 0);
            this.f6050h.A("image_format", "thumbnail");
            eVarK0.q(this.f6050h.a());
            return AbstractC0306a.d0(eVarK0);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.n0, V.e
        /* JADX INFO: renamed from: m, reason: merged with bridge method [inline-methods] */
        public void f(AbstractC0306a abstractC0306a) {
            super.f(abstractC0306a);
            this.f6049g.e(this.f6050h, "LocalThumbnailBitmapSdk29Producer", abstractC0306a != null);
            this.f6050h.n0("local", "thumbnail_bitmap");
        }
    }

    class b extends C0346f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ n0 f6054a;

        b(n0 n0Var) {
            this.f6054a = n0Var;
        }

        @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
        public void a() {
            this.f6054a.a();
        }
    }

    public T(Executor executor, ContentResolver contentResolver) {
        this.f6047a = executor;
        this.f6048b = contentResolver;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String e(U0.b bVar) {
        return f0.f.e(this.f6048b, bVar.v());
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        h0 h0VarP = f0Var.P();
        U0.b bVarX = f0Var.X();
        f0Var.n0("local", "thumbnail_bitmap");
        a aVar = new a(interfaceC0354n, h0VarP, f0Var, "LocalThumbnailBitmapSdk29Producer", h0VarP, f0Var, bVarX, new CancellationSignal());
        f0Var.a0(new b(aVar));
        this.f6047a.execute(aVar);
    }
}
