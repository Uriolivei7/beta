package F0;

import D2.h;
import F0.b;
import M2.B;
import M2.C0193d;
import M2.E;
import M2.InterfaceC0194e;
import M2.InterfaceC0195f;
import M2.z;
import android.net.Uri;
import android.os.Looper;
import android.os.SystemClock;
import com.facebook.imagepipeline.producers.AbstractC0344d;
import com.facebook.imagepipeline.producers.C0346f;
import com.facebook.imagepipeline.producers.D;
import com.facebook.imagepipeline.producers.InterfaceC0354n;
import com.facebook.imagepipeline.producers.Y;
import com.facebook.imagepipeline.producers.f0;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.n;
import r2.r;
import s2.AbstractC0696D;

/* JADX INFO: loaded from: classes.dex */
public class b extends AbstractC0344d {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final a f218d = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final InterfaceC0194e.a f219a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Executor f220b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0193d f221c;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX INFO: renamed from: F0.b$b, reason: collision with other inner class name */
    public static final class C0004b extends D {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public long f222f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public long f223g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public long f224h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public C0004b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
            super(interfaceC0354n, f0Var);
            h.f(interfaceC0354n, "consumer");
            h.f(f0Var, "producerContext");
        }
    }

    public static final class c extends C0346f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ InterfaceC0194e f225a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ b f226b;

        c(InterfaceC0194e interfaceC0194e, b bVar) {
            this.f225a = interfaceC0194e;
            this.f226b = bVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void f(InterfaceC0194e interfaceC0194e) {
            interfaceC0194e.cancel();
        }

        @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
        public void a() {
            if (!h.b(Looper.myLooper(), Looper.getMainLooper())) {
                this.f225a.cancel();
                return;
            }
            Executor executor = this.f226b.f220b;
            final InterfaceC0194e interfaceC0194e = this.f225a;
            executor.execute(new Runnable() { // from class: F0.c
                @Override // java.lang.Runnable
                public final void run() {
                    b.c.f(interfaceC0194e);
                }
            });
        }
    }

    public static final class d implements InterfaceC0195f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ C0004b f227a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ b f228b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Y.a f229c;

        d(C0004b c0004b, b bVar, Y.a aVar) {
            this.f227a = c0004b;
            this.f228b = bVar;
            this.f229c = aVar;
        }

        @Override // M2.InterfaceC0195f
        public void a(InterfaceC0194e interfaceC0194e, IOException iOException) {
            h.f(interfaceC0194e, "call");
            h.f(iOException, "e");
            this.f228b.m(interfaceC0194e, iOException, this.f229c);
        }

        @Override // M2.InterfaceC0195f
        public void b(InterfaceC0194e interfaceC0194e, M2.D d4) throws IOException {
            h.f(interfaceC0194e, "call");
            h.f(d4, "response");
            this.f227a.f223g = SystemClock.elapsedRealtime();
            E eQ = d4.q();
            if (eQ == null) {
                b bVar = this.f228b;
                bVar.m(interfaceC0194e, bVar.n("Response body null: " + d4, d4), this.f229c);
                return;
            }
            b bVar2 = this.f228b;
            Y.a aVar = this.f229c;
            C0004b c0004b = this.f227a;
            try {
                try {
                    if (d4.e0()) {
                        I0.b bVarC = I0.b.f385c.c(d4.X("Content-Range"));
                        if (bVarC != null && (bVarC.f387a != 0 || bVarC.f388b != Integer.MAX_VALUE)) {
                            c0004b.j(bVarC);
                            c0004b.i(8);
                        }
                        aVar.c(eQ.a(), eQ.q() < 0 ? 0 : (int) eQ.q());
                    } else {
                        bVar2.m(interfaceC0194e, bVar2.n("Unexpected HTTP code " + d4, d4), aVar);
                    }
                } catch (Exception e4) {
                    bVar2.m(interfaceC0194e, e4, aVar);
                }
                r rVar = r.f10584a;
                A2.a.a(eQ, null);
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    A2.a.a(eQ, th);
                    throw th2;
                }
            }
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public b(InterfaceC0194e.a aVar, Executor executor) {
        this(aVar, executor, false, 4, null);
        h.f(aVar, "callFactory");
        h.f(executor, "cancellationExecutor");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void m(InterfaceC0194e interfaceC0194e, Exception exc, Y.a aVar) {
        if (interfaceC0194e.q()) {
            aVar.b();
        } else {
            aVar.a(exc);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final IOException n(String str, M2.D d4) {
        return new IOException(str, F0.d.f231d.a(d4));
    }

    @Override // com.facebook.imagepipeline.producers.Y
    /* JADX INFO: renamed from: i, reason: merged with bridge method [inline-methods] */
    public C0004b c(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        h.f(interfaceC0354n, "consumer");
        h.f(f0Var, "context");
        return new C0004b(interfaceC0354n, f0Var);
    }

    @Override // com.facebook.imagepipeline.producers.Y
    /* JADX INFO: renamed from: j, reason: merged with bridge method [inline-methods] */
    public void b(C0004b c0004b, Y.a aVar) {
        h.f(c0004b, "fetchState");
        h.f(aVar, "callback");
        c0004b.f222f = SystemClock.elapsedRealtime();
        Uri uriG = c0004b.g();
        h.e(uriG, "getUri(...)");
        try {
            B.a aVarD = new B.a().m(uriG.toString()).d();
            C0193d c0193d = this.f221c;
            if (c0193d != null) {
                aVarD.c(c0193d);
            }
            I0.b bVarB = c0004b.b().X().b();
            if (bVarB != null) {
                aVarD.a("Range", bVarB.f());
            }
            B b4 = aVarD.b();
            h.e(b4, "build(...)");
            k(c0004b, aVar, b4);
        } catch (Exception e4) {
            aVar.a(e4);
        }
    }

    protected void k(C0004b c0004b, Y.a aVar, B b4) {
        h.f(c0004b, "fetchState");
        h.f(aVar, "callback");
        h.f(b4, "request");
        InterfaceC0194e interfaceC0194eB = this.f219a.b(b4);
        c0004b.b().a0(new c(interfaceC0194eB, this));
        interfaceC0194eB.o(new d(c0004b, this, aVar));
    }

    @Override // com.facebook.imagepipeline.producers.AbstractC0344d, com.facebook.imagepipeline.producers.Y
    /* JADX INFO: renamed from: l, reason: merged with bridge method [inline-methods] */
    public Map e(C0004b c0004b, int i3) {
        h.f(c0004b, "fetchState");
        return AbstractC0696D.h(n.a("queue_time", String.valueOf(c0004b.f223g - c0004b.f222f)), n.a("fetch_time", String.valueOf(c0004b.f224h - c0004b.f223g)), n.a("total_time", String.valueOf(c0004b.f224h - c0004b.f222f)), n.a("image_size", String.valueOf(i3)));
    }

    @Override // com.facebook.imagepipeline.producers.AbstractC0344d, com.facebook.imagepipeline.producers.Y
    /* JADX INFO: renamed from: o, reason: merged with bridge method [inline-methods] */
    public void a(C0004b c0004b, int i3) {
        h.f(c0004b, "fetchState");
        c0004b.f224h = SystemClock.elapsedRealtime();
    }

    public /* synthetic */ b(InterfaceC0194e.a aVar, Executor executor, boolean z3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(aVar, executor, (i3 & 4) != 0 ? true : z3);
    }

    public b(InterfaceC0194e.a aVar, Executor executor, boolean z3) {
        h.f(aVar, "callFactory");
        h.f(executor, "cancellationExecutor");
        this.f219a = aVar;
        this.f220b = executor;
        this.f221c = z3 ? new C0193d.a().e().a() : null;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public b(z zVar) {
        h.f(zVar, "okHttpClient");
        ExecutorService executorServiceD = zVar.s().d();
        h.e(executorServiceD, "executorService(...)");
        this(zVar, executorServiceD, false, 4, null);
    }
}
