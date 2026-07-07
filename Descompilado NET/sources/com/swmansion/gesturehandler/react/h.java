package com.swmansion.gesturehandler.react;

import android.util.SparseArray;
import android.view.View;
import com.facebook.react.bridge.UiThreadUtil;
import java.util.ArrayList;
import n2.C0625d;

/* JADX INFO: loaded from: classes.dex */
public final class h implements n2.j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final SparseArray f8628a = new SparseArray();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final SparseArray f8629b = new SparseArray();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final SparseArray f8630c = new SparseArray();

    private final synchronized void d(final C0625d c0625d) {
        try {
            Integer num = (Integer) this.f8629b.get(c0625d.R());
            if (num != null) {
                this.f8629b.remove(c0625d.R());
                ArrayList arrayList = (ArrayList) this.f8630c.get(num.intValue());
                if (arrayList != null) {
                    synchronized (arrayList) {
                        arrayList.remove(c0625d);
                    }
                    if (arrayList.size() == 0) {
                        this.f8630c.remove(num.intValue());
                    }
                }
            }
            if (c0625d.U() != null) {
                UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.swmansion.gesturehandler.react.g
                    @Override // java.lang.Runnable
                    public final void run() {
                        h.e(c0625d);
                    }
                });
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void e(C0625d c0625d) {
        c0625d.o();
    }

    private final synchronized void k(int i3, C0625d c0625d) {
        try {
            if (this.f8629b.get(c0625d.R()) != null) {
                throw new IllegalStateException(("Handler " + c0625d + " already attached").toString());
            }
            this.f8629b.put(c0625d.R(), Integer.valueOf(i3));
            Object obj = this.f8630c.get(i3);
            if (obj == null) {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(c0625d);
                this.f8630c.put(i3, arrayList);
            } else {
                synchronized (obj) {
                    ((ArrayList) obj).add(c0625d);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // n2.j
    public synchronized ArrayList a(View view) {
        D2.h.f(view, "view");
        return i(view.getId());
    }

    public final synchronized boolean c(int i3, int i4, int i5) {
        boolean z3;
        C0625d c0625d = (C0625d) this.f8628a.get(i3);
        if (c0625d != null) {
            d(c0625d);
            c0625d.r0(i5);
            k(i4, c0625d);
            z3 = true;
        } else {
            z3 = false;
        }
        return z3;
    }

    public final synchronized void f() {
        this.f8628a.clear();
        this.f8629b.clear();
        this.f8630c.clear();
    }

    public final synchronized void g(int i3) {
        C0625d c0625d = (C0625d) this.f8628a.get(i3);
        if (c0625d != null) {
            d(c0625d);
            this.f8628a.remove(i3);
        }
    }

    public final synchronized C0625d h(int i3) {
        return (C0625d) this.f8628a.get(i3);
    }

    public final synchronized ArrayList i(int i3) {
        return (ArrayList) this.f8630c.get(i3);
    }

    public final synchronized void j(C0625d c0625d) {
        D2.h.f(c0625d, "handler");
        this.f8628a.put(c0625d.R(), c0625d);
    }
}
