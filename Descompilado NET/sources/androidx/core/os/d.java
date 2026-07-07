package androidx.core.os;

import android.os.CancellationSignal;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f4516a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private a f4517b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Object f4518c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f4519d;

    public interface a {
        void a();
    }

    private void c() {
        while (this.f4519d) {
            try {
                wait();
            } catch (InterruptedException unused) {
            }
        }
    }

    public void a() {
        synchronized (this) {
            try {
                if (this.f4516a) {
                    return;
                }
                this.f4516a = true;
                this.f4519d = true;
                a aVar = this.f4517b;
                Object obj = this.f4518c;
                if (aVar != null) {
                    try {
                        aVar.a();
                    } catch (Throwable th) {
                        synchronized (this) {
                            this.f4519d = false;
                            notifyAll();
                            throw th;
                        }
                    }
                }
                if (obj != null) {
                    ((CancellationSignal) obj).cancel();
                }
                synchronized (this) {
                    this.f4519d = false;
                    notifyAll();
                }
            } finally {
            }
        }
    }

    public void b(a aVar) {
        synchronized (this) {
            try {
                c();
                if (this.f4517b == aVar) {
                    return;
                }
                this.f4517b = aVar;
                if (this.f4516a && aVar != null) {
                    aVar.a();
                }
            } finally {
            }
        }
    }
}
