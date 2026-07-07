package com.swmansion.gesturehandler.react;

import android.util.SparseArray;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.swmansion.gesturehandler.react.j;
import kotlin.jvm.internal.DefaultConstructorMarker;
import n2.C0625d;
import n2.q;

/* JADX INFO: loaded from: classes.dex */
public final class e implements n2.e {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f8622d = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final SparseArray f8623a = new SparseArray();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final SparseArray f8624b = new SparseArray();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final SparseArray f8625c = new SparseArray();

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    private final int[] f(ReadableMap readableMap, String str) {
        ReadableArray array = readableMap.getArray(str);
        D2.h.c(array);
        int size = array.size();
        int[] iArr = new int[size];
        for (int i3 = 0; i3 < size; i3++) {
            iArr[i3] = array.getInt(i3);
        }
        return iArr;
    }

    @Override // n2.e
    public boolean a(C0625d c0625d, C0625d c0625d2) {
        D2.h.f(c0625d, "handler");
        D2.h.f(c0625d2, "otherHandler");
        int[] iArr = (int[]) this.f8625c.get(c0625d.R());
        if (iArr == null) {
            return false;
        }
        for (int i3 : iArr) {
            if (i3 == c0625d2.R()) {
                return true;
            }
        }
        return false;
    }

    @Override // n2.e
    public boolean b(C0625d c0625d, C0625d c0625d2) {
        D2.h.f(c0625d, "handler");
        D2.h.f(c0625d2, "otherHandler");
        return c0625d2 instanceof q ? ((q) c0625d2).S0() : c0625d2 instanceof j.b;
    }

    @Override // n2.e
    public boolean c(C0625d c0625d, C0625d c0625d2) {
        D2.h.f(c0625d, "handler");
        D2.h.f(c0625d2, "otherHandler");
        int[] iArr = (int[]) this.f8624b.get(c0625d.R());
        if (iArr == null) {
            return false;
        }
        for (int i3 : iArr) {
            if (i3 == c0625d2.R()) {
                return true;
            }
        }
        return false;
    }

    @Override // n2.e
    public boolean d(C0625d c0625d, C0625d c0625d2) {
        D2.h.f(c0625d, "handler");
        D2.h.f(c0625d2, "otherHandler");
        int[] iArr = (int[]) this.f8623a.get(c0625d.R());
        if (iArr == null) {
            return false;
        }
        for (int i3 : iArr) {
            if (i3 == c0625d2.R()) {
                return true;
            }
        }
        return false;
    }

    public final void e(C0625d c0625d, ReadableMap readableMap) {
        D2.h.f(c0625d, "handler");
        D2.h.f(readableMap, "config");
        c0625d.y0(this);
        if (readableMap.hasKey("waitFor")) {
            this.f8623a.put(c0625d.R(), f(readableMap, "waitFor"));
        }
        if (readableMap.hasKey("simultaneousHandlers")) {
            this.f8624b.put(c0625d.R(), f(readableMap, "simultaneousHandlers"));
        }
        if (readableMap.hasKey("blocksHandlers")) {
            this.f8625c.put(c0625d.R(), f(readableMap, "blocksHandlers"));
        }
    }

    public final void g(int i3) {
        this.f8623a.remove(i3);
        this.f8624b.remove(i3);
    }

    public final void h() {
        this.f8623a.clear();
        this.f8624b.clear();
    }
}
