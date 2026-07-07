package R0;

import android.util.SparseIntArray;

/* JADX INFO: loaded from: classes.dex */
public class E {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f1946a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f1947b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final SparseIntArray f1948c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f1949d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final int f1950e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public boolean f1951f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public final int f1952g;

    public E(int i3, SparseIntArray sparseIntArray) {
        this(i3, i3, sparseIntArray, 0, Integer.MAX_VALUE, -1);
    }

    public E(int i3, int i4, SparseIntArray sparseIntArray) {
        this(i3, i4, sparseIntArray, 0, Integer.MAX_VALUE, -1);
    }

    public E(int i3, int i4, SparseIntArray sparseIntArray, int i5, int i6, int i7) {
        X.k.i(i3 >= 0 && i4 >= i3);
        this.f1947b = i3;
        this.f1946a = i4;
        this.f1948c = sparseIntArray;
        this.f1949d = i5;
        this.f1950e = i6;
        this.f1952g = i7;
    }
}
