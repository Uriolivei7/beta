package com.facebook.imagepipeline.memory;

import R0.E;
import R0.F;
import a0.InterfaceC0207a;
import android.util.SparseIntArray;
import com.facebook.imagepipeline.memory.a;

/* JADX INFO: loaded from: classes.dex */
public class e extends a implements InterfaceC0207a {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final int[] f5937k;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public e(a0.d dVar, E e4, F f3) {
        super(dVar, e4, f3);
        D2.h.f(dVar, "memoryTrimmableRegistry");
        D2.h.f(e4, "poolParams");
        D2.h.f(f3, "poolStatsTracker");
        SparseIntArray sparseIntArray = e4.f1948c;
        if (sparseIntArray != null) {
            this.f5937k = new int[sparseIntArray.size()];
            int size = sparseIntArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                this.f5937k[i3] = sparseIntArray.keyAt(i3);
            }
        } else {
            this.f5937k = new int[0];
        }
        r();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: A, reason: merged with bridge method [inline-methods] */
    public void j(byte[] bArr) {
        D2.h.f(bArr, "value");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: B, reason: merged with bridge method [inline-methods] */
    public int n(byte[] bArr) {
        D2.h.f(bArr, "value");
        return bArr.length;
    }

    @Override // com.facebook.imagepipeline.memory.a
    protected int m(int i3) {
        if (i3 <= 0) {
            throw new a.b(Integer.valueOf(i3));
        }
        for (int i4 : this.f5937k) {
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
    public byte[] f(int i3) {
        return new byte[i3];
    }
}
