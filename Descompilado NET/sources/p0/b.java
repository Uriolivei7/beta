package p0;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import p0.AbstractC0635a;

/* JADX INFO: loaded from: classes.dex */
class b extends AbstractC0635a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Object f10266b = new Object();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Runnable f10270f = new a();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private ArrayList f10268d = new ArrayList();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private ArrayList f10269e = new ArrayList();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Handler f10267c = new Handler(Looper.getMainLooper());

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (b.this.f10266b) {
                ArrayList arrayList = b.this.f10269e;
                b bVar = b.this;
                bVar.f10269e = bVar.f10268d;
                b.this.f10268d = arrayList;
            }
            int size = b.this.f10269e.size();
            for (int i3 = 0; i3 < size; i3++) {
                ((AbstractC0635a.InterfaceC0140a) b.this.f10269e.get(i3)).release();
            }
            b.this.f10269e.clear();
        }
    }

    @Override // p0.AbstractC0635a
    public void a(AbstractC0635a.InterfaceC0140a interfaceC0140a) {
        synchronized (this.f10266b) {
            this.f10268d.remove(interfaceC0140a);
        }
    }

    @Override // p0.AbstractC0635a
    public void d(AbstractC0635a.InterfaceC0140a interfaceC0140a) {
        if (!AbstractC0635a.c()) {
            interfaceC0140a.release();
            return;
        }
        synchronized (this.f10266b) {
            try {
                if (this.f10268d.contains(interfaceC0140a)) {
                    return;
                }
                this.f10268d.add(interfaceC0140a);
                boolean z3 = true;
                if (this.f10268d.size() != 1) {
                    z3 = false;
                }
                if (z3) {
                    this.f10267c.post(this.f10270f);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
