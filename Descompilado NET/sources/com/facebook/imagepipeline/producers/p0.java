package com.facebook.imagepipeline.producers;

import android.os.Looper;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class p0 implements e0 {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f6199c = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final e0 f6200a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final q0 f6201b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String c(f0 f0Var) {
            if (!P0.a.b()) {
                return null;
            }
            return "ThreadHandoffProducer_produceResults_" + f0Var.getId();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean d(f0 f0Var) {
            return f0Var.e0().G().k() && Looper.getMainLooper().getThread() != Thread.currentThread();
        }

        private a() {
        }
    }

    public static final class b extends C0346f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ n0 f6202a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ p0 f6203b;

        b(n0 n0Var, p0 p0Var) {
            this.f6202a = n0Var;
            this.f6203b = p0Var;
        }

        @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
        public void a() {
            this.f6202a.a();
            this.f6203b.d().b(this.f6202a);
        }
    }

    public static final class c extends n0 {

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        final /* synthetic */ InterfaceC0354n f6204g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ h0 f6205h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ f0 f6206i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        final /* synthetic */ p0 f6207j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(InterfaceC0354n interfaceC0354n, h0 h0Var, f0 f0Var, p0 p0Var) {
            super(interfaceC0354n, h0Var, f0Var, "BackgroundThreadHandoffProducer");
            this.f6204g = interfaceC0354n;
            this.f6205h = h0Var;
            this.f6206i = f0Var;
            this.f6207j = p0Var;
        }

        @Override // V.e
        protected void b(Object obj) {
        }

        @Override // V.e
        protected Object c() {
            return null;
        }

        @Override // com.facebook.imagepipeline.producers.n0, V.e
        protected void f(Object obj) {
            this.f6205h.d(this.f6206i, "BackgroundThreadHandoffProducer", null);
            this.f6207j.c().b(this.f6204g, this.f6206i);
        }
    }

    public p0(e0 e0Var, q0 q0Var) {
        D2.h.f(e0Var, "inputProducer");
        D2.h.f(q0Var, "threadHandoffProducerQueue");
        this.f6200a = e0Var;
        this.f6201b = q0Var;
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        D2.h.f(interfaceC0354n, "consumer");
        D2.h.f(f0Var, "context");
        if (!V0.b.d()) {
            h0 h0VarP = f0Var.P();
            a aVar = f6199c;
            if (aVar.d(f0Var)) {
                h0VarP.g(f0Var, "BackgroundThreadHandoffProducer");
                h0VarP.d(f0Var, "BackgroundThreadHandoffProducer", null);
                this.f6200a.b(interfaceC0354n, f0Var);
                return;
            } else {
                c cVar = new c(interfaceC0354n, h0VarP, f0Var, this);
                f0Var.a0(new b(cVar, this));
                this.f6201b.a(P0.a.a(cVar, aVar.c(f0Var)));
                return;
            }
        }
        V0.b.a("ThreadHandoffProducer#produceResults");
        try {
            h0 h0VarP2 = f0Var.P();
            a aVar2 = f6199c;
            if (aVar2.d(f0Var)) {
                h0VarP2.g(f0Var, "BackgroundThreadHandoffProducer");
                h0VarP2.d(f0Var, "BackgroundThreadHandoffProducer", null);
                this.f6200a.b(interfaceC0354n, f0Var);
            } else {
                c cVar2 = new c(interfaceC0354n, h0VarP2, f0Var, this);
                f0Var.a0(new b(cVar2, this));
                this.f6201b.a(P0.a.a(cVar2, aVar2.c(f0Var)));
                r2.r rVar = r2.r.f10584a;
            }
        } finally {
            V0.b.b();
        }
    }

    public final e0 c() {
        return this.f6200a;
    }

    public final q0 d() {
        return this.f6201b;
    }
}
