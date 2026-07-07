package com.facebook.imagepipeline.memory;

import R0.E;
import R0.F;
import R0.v;
import X.k;
import android.util.SparseIntArray;
import com.facebook.imagepipeline.memory.a;

/* JADX INFO: loaded from: classes.dex */
public abstract class f extends a {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final int[] f5938k;

    f(a0.d dVar, E e4, F f3) {
        super(dVar, e4, f3);
        SparseIntArray sparseIntArray = (SparseIntArray) k.g(e4.f1948c);
        this.f5938k = new int[sparseIntArray.size()];
        int i3 = 0;
        while (true) {
            int[] iArr = this.f5938k;
            if (i3 >= iArr.length) {
                r();
                return;
            } else {
                iArr[i3] = sparseIntArray.keyAt(i3);
                i3++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: A, reason: merged with bridge method [inline-methods] */
    public int n(v vVar) {
        k.g(vVar);
        return vVar.i();
    }

    int B() {
        return this.f5938k[0];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: C, reason: merged with bridge method [inline-methods] */
    public boolean t(v vVar) {
        k.g(vVar);
        return !vVar.b();
    }

    @Override // com.facebook.imagepipeline.memory.a
    protected int m(int i3) {
        if (i3 <= 0) {
            throw new a.b(Integer.valueOf(i3));
        }
        for (int i4 : this.f5938k) {
            if (i4 >= i3) {
                return i4;
            }
        }
        return i3;
    }

    @Override // com.facebook.imagepipeline.memory.a
    protected int o(int i3) {
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: z, reason: merged with bridge method [inline-methods] */
    public void j(v vVar) {
        k.g(vVar);
        vVar.close();
    }
}
