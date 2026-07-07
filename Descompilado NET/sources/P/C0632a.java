package p;

import android.graphics.Typeface;
import android.os.Handler;
import p.f;
import p.g;

/* JADX INFO: renamed from: p.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0632a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final g.c f10217a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Handler f10218b;

    /* JADX INFO: renamed from: p.a$a, reason: collision with other inner class name */
    class RunnableC0138a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ g.c f10219b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Typeface f10220c;

        RunnableC0138a(g.c cVar, Typeface typeface) {
            this.f10219b = cVar;
            this.f10220c = typeface;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f10219b.b(this.f10220c);
        }
    }

    /* JADX INFO: renamed from: p.a$b */
    class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ g.c f10222b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f10223c;

        b(g.c cVar, int i3) {
            this.f10222b = cVar;
            this.f10223c = i3;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f10222b.a(this.f10223c);
        }
    }

    C0632a(g.c cVar, Handler handler) {
        this.f10217a = cVar;
        this.f10218b = handler;
    }

    private void a(int i3) {
        this.f10218b.post(new b(this.f10217a, i3));
    }

    private void c(Typeface typeface) {
        this.f10218b.post(new RunnableC0138a(this.f10217a, typeface));
    }

    void b(f.e eVar) {
        if (eVar.a()) {
            c(eVar.f10247a);
        } else {
            a(eVar.f10248b);
        }
    }
}
