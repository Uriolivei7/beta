package com.facebook.imagepipeline.platform;

import X.k;
import a0.h;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import b0.AbstractC0306a;
import com.facebook.imagepipeline.memory.d;
import com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder;

/* JADX INFO: loaded from: classes.dex */
public class KitKatPurgeableDecoder extends DalvikPurgeableDecoder {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final d f5956c;

    public KitKatPurgeableDecoder(d dVar) {
        this.f5956c = dVar;
    }

    private static void h(byte[] bArr, int i3) {
        bArr[i3] = -1;
        bArr[i3 + 1] = -39;
    }

    @Override // com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder
    protected Bitmap c(AbstractC0306a abstractC0306a, BitmapFactory.Options options) {
        h hVar = (h) abstractC0306a.P();
        int size = hVar.size();
        AbstractC0306a abstractC0306aA = this.f5956c.a(size);
        try {
            byte[] bArr = (byte[]) abstractC0306aA.P();
            hVar.c(0, bArr, 0, size);
            return (Bitmap) k.h(BitmapFactory.decodeByteArray(bArr, 0, size, options), "BitmapFactory returned null");
        } finally {
            AbstractC0306a.D(abstractC0306aA);
        }
    }

    @Override // com.facebook.imagepipeline.nativecode.DalvikPurgeableDecoder
    protected Bitmap d(AbstractC0306a abstractC0306a, int i3, BitmapFactory.Options options) {
        byte[] bArr = DalvikPurgeableDecoder.e(abstractC0306a, i3) ? null : DalvikPurgeableDecoder.f5944b;
        h hVar = (h) abstractC0306a.P();
        k.b(Boolean.valueOf(i3 <= hVar.size()));
        int i4 = i3 + 2;
        AbstractC0306a abstractC0306aA = this.f5956c.a(i4);
        try {
            byte[] bArr2 = (byte[]) abstractC0306aA.P();
            hVar.c(0, bArr2, 0, i3);
            if (bArr != null) {
                h(bArr2, i3);
                i3 = i4;
            }
            Bitmap bitmap = (Bitmap) k.h(BitmapFactory.decodeByteArray(bArr2, 0, i3, options), "BitmapFactory returned null");
            AbstractC0306a.D(abstractC0306aA);
            return bitmap;
        } catch (Throwable th) {
            AbstractC0306a.D(abstractC0306aA);
            throw th;
        }
    }
}
