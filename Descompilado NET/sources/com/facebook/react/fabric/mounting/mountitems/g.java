package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.A0;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final g f6858a = new g();

    private g() {
    }

    public static final MountItem a(int i3, int i4) {
        return new b(i3, i4);
    }

    public static final c b(int i3, int i4, int i5, ReadableArray readableArray) {
        return new d(i3, i4, i5, readableArray);
    }

    public static final c c(int i3, int i4, String str, ReadableArray readableArray) {
        D2.h.f(str, "commandId");
        return new e(i3, i4, str, readableArray);
    }

    public static final MountItem d(int i3, int[] iArr, Object[] objArr, int i4) {
        D2.h.f(iArr, "intBuf");
        D2.h.f(objArr, "objBuf");
        return new IntBufferBatchMountItem(i3, iArr, objArr, i4);
    }

    public static final MountItem e(int i3, int i4, String str, ReadableMap readableMap, A0 a02, boolean z3) {
        D2.h.f(str, "component");
        return new h(i3, i4, str, readableMap, a02, z3);
    }

    public static final MountItem f(int i3, int i4, int i5) {
        return new i(i3, i4, i5);
    }
}
