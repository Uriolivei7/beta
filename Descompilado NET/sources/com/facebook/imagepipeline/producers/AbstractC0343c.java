package com.facebook.imagepipeline.producers;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0343c implements InterfaceC0354n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f6111a = false;

    public static boolean e(int i3) {
        return (i3 & 1) == 1;
    }

    public static boolean f(int i3) {
        return !e(i3);
    }

    public static int l(boolean z3) {
        return z3 ? 1 : 0;
    }

    public static boolean m(int i3, int i4) {
        return (i3 & i4) != 0;
    }

    public static boolean n(int i3, int i4) {
        return (i3 & i4) == i4;
    }

    public static int o(int i3, int i4) {
        return i3 & (~i4);
    }

    @Override // com.facebook.imagepipeline.producers.InterfaceC0354n
    public synchronized void a(Throwable th) {
        if (this.f6111a) {
            return;
        }
        this.f6111a = true;
        try {
            h(th);
        } catch (Exception e4) {
            k(e4);
        }
    }

    @Override // com.facebook.imagepipeline.producers.InterfaceC0354n
    public synchronized void b() {
        if (this.f6111a) {
            return;
        }
        this.f6111a = true;
        try {
            g();
        } catch (Exception e4) {
            k(e4);
        }
    }

    @Override // com.facebook.imagepipeline.producers.InterfaceC0354n
    public synchronized void c(float f3) {
        if (this.f6111a) {
            return;
        }
        try {
            j(f3);
        } catch (Exception e4) {
            k(e4);
        }
    }

    @Override // com.facebook.imagepipeline.producers.InterfaceC0354n
    public synchronized void d(Object obj, int i3) {
        if (this.f6111a) {
            return;
        }
        this.f6111a = e(i3);
        try {
            i(obj, i3);
        } catch (Exception e4) {
            k(e4);
        }
    }

    protected abstract void g();

    protected abstract void h(Throwable th);

    protected abstract void i(Object obj, int i3);

    protected void j(float f3) {
    }

    protected void k(Exception exc) {
        Y.a.M(getClass(), "unhandled exception", exc);
    }
}
