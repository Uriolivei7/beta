package e1;

import a1.C0210a;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Thread f9370a;

    public final void a() {
        Thread threadCurrentThread = Thread.currentThread();
        if (this.f9370a == null) {
            this.f9370a = threadCurrentThread;
        }
        C0210a.a(D2.h.b(this.f9370a, threadCurrentThread));
    }
}
