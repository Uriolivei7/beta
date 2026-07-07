package com.facebook.react.uimanager;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

/* JADX INFO: renamed from: com.facebook.react.uimanager.y0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0466y0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final SparseArray f7638a = new SparseArray();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final SparseBooleanArray f7639b = new SparseBooleanArray();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final e1.i f7640c = new e1.i();

    public void a(InterfaceC0451q0 interfaceC0451q0) {
        this.f7640c.a();
        this.f7638a.put(interfaceC0451q0.H(), interfaceC0451q0);
    }

    public void b(InterfaceC0451q0 interfaceC0451q0) {
        this.f7640c.a();
        int iH = interfaceC0451q0.H();
        this.f7638a.put(iH, interfaceC0451q0);
        this.f7639b.put(iH, true);
    }

    public InterfaceC0451q0 c(int i3) {
        this.f7640c.a();
        return (InterfaceC0451q0) this.f7638a.get(i3);
    }

    public int d() {
        this.f7640c.a();
        return this.f7639b.size();
    }

    public int e(int i3) {
        this.f7640c.a();
        return this.f7639b.keyAt(i3);
    }

    public boolean f(int i3) {
        this.f7640c.a();
        return this.f7639b.get(i3);
    }

    public void g(int i3) {
        this.f7640c.a();
        if (!this.f7639b.get(i3)) {
            this.f7638a.remove(i3);
            return;
        }
        throw new P("Trying to remove root node " + i3 + " without using removeRootNode!");
    }

    public void h(int i3) {
        this.f7640c.a();
        if (i3 == -1) {
            return;
        }
        if (this.f7639b.get(i3)) {
            this.f7638a.remove(i3);
            this.f7639b.delete(i3);
        } else {
            throw new P("View with tag " + i3 + " is not registered as a root view");
        }
    }
}
