package com.facebook.imagepipeline.memory;

import R0.E;
import R0.F;
import R0.i;
import X.k;
import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class c extends a implements i {
    public c(a0.d dVar, E e4, F f3, boolean z3) {
        super(dVar, e4, f3, z3);
        r();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: A, reason: merged with bridge method [inline-methods] */
    public void j(Bitmap bitmap) {
        k.g(bitmap);
        bitmap.recycle();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: B, reason: merged with bridge method [inline-methods] */
    public int n(Bitmap bitmap) {
        k.g(bitmap);
        return bitmap.getAllocationByteCount();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: C, reason: merged with bridge method [inline-methods] */
    public Bitmap p(b bVar) {
        Bitmap bitmap = (Bitmap) super.p(bVar);
        if (bitmap != null) {
            bitmap.eraseColor(0);
        }
        return bitmap;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
    public boolean t(Bitmap bitmap) {
        k.g(bitmap);
        return !bitmap.isRecycled() && bitmap.isMutable();
    }

    @Override // com.facebook.imagepipeline.memory.a
    protected int m(int i3) {
        return i3;
    }

    @Override // com.facebook.imagepipeline.memory.a
    protected int o(int i3) {
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: z, reason: merged with bridge method [inline-methods] */
    public Bitmap f(int i3) {
        return Bitmap.createBitmap(1, (int) Math.ceil(((double) i3) / 2.0d), Bitmap.Config.RGB_565);
    }
}
