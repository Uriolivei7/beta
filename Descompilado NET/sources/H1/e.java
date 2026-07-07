package H1;

import M2.B;
import M2.D;
import M2.H;
import M2.I;
import M2.z;
import android.os.Handler;
import android.os.Looper;
import b3.l;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class e extends I {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final String f358i = "e";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f359a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final z f361c;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f363e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private H f364f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private c f365g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private b f366h;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f362d = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Handler f360b = new Handler(Looper.getMainLooper());

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            e.this.l();
        }
    }

    public interface b {
        void a();

        void b();
    }

    public interface c {
        void a(l lVar);

        void onMessage(String str);
    }

    public e(String str, c cVar, b bVar) {
        this.f359a = str;
        this.f365g = cVar;
        this.f366h = bVar;
        z.a aVar = new z.a();
        TimeUnit timeUnit = TimeUnit.SECONDS;
        this.f361c = aVar.e(10L, timeUnit).N(10L, timeUnit).M(0L, TimeUnit.MINUTES).b();
    }

    private void h(String str, Throwable th) {
        Y.a.n(f358i, "Error occurred, shutting down websocket connection: " + str, th);
        j();
    }

    private void j() {
        H h3 = this.f364f;
        if (h3 != null) {
            try {
                h3.a(1000, "End of session");
            } catch (Exception unused) {
            }
            this.f364f = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void l() {
        if (!this.f362d) {
            k();
        }
    }

    private void m() {
        if (this.f362d) {
            throw new IllegalStateException("Can't reconnect closed client");
        }
        if (!this.f363e) {
            Y.a.I(f358i, "Couldn't connect to \"" + this.f359a + "\", will silently retry");
            this.f363e = true;
        }
        this.f360b.postDelayed(new a(), 2000L);
    }

    @Override // M2.I
    public synchronized void a(H h3, int i3, String str) {
        try {
            this.f364f = null;
            if (!this.f362d) {
                b bVar = this.f366h;
                if (bVar != null) {
                    bVar.a();
                }
                m();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // M2.I
    public synchronized void c(H h3, Throwable th, D d4) {
        try {
            if (this.f364f != null) {
                h("Websocket exception", th);
            }
            if (!this.f362d) {
                b bVar = this.f366h;
                if (bVar != null) {
                    bVar.a();
                }
                m();
            }
        } catch (Throwable th2) {
            throw th2;
        }
    }

    @Override // M2.I
    public synchronized void d(H h3, l lVar) {
        c cVar = this.f365g;
        if (cVar != null) {
            cVar.a(lVar);
        }
    }

    @Override // M2.I
    public synchronized void e(H h3, String str) {
        c cVar = this.f365g;
        if (cVar != null) {
            cVar.onMessage(str);
        }
    }

    @Override // M2.I
    public synchronized void f(H h3, D d4) {
        this.f364f = h3;
        this.f363e = false;
        b bVar = this.f366h;
        if (bVar != null) {
            bVar.b();
        }
    }

    public void i() {
        this.f362d = true;
        j();
        this.f365g = null;
        b bVar = this.f366h;
        if (bVar != null) {
            bVar.a();
        }
    }

    public void k() {
        if (this.f362d) {
            throw new IllegalStateException("Can't connect closed client");
        }
        this.f361c.D(new B.a().m(this.f359a).b(), this);
    }

    public synchronized void n(String str) {
        H h3 = this.f364f;
        if (h3 == null) {
            throw new ClosedChannelException();
        }
        h3.b(str);
    }
}
