package h0;

import X.k;
import X.n;
import java.util.List;

/* JADX INFO: renamed from: h0.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0551f implements n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final List f9468a;

    /* JADX INFO: renamed from: h0.f$a */
    private class a extends AbstractC0546a {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private int f9469h = 0;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private InterfaceC0548c f9470i = null;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private InterfaceC0548c f9471j = null;

        /* JADX INFO: renamed from: h0.f$a$a, reason: collision with other inner class name */
        private class C0130a implements InterfaceC0550e {
            @Override // h0.InterfaceC0550e
            public void a(InterfaceC0548c interfaceC0548c) {
                if (interfaceC0548c.d()) {
                    a.this.G(interfaceC0548c);
                } else if (interfaceC0548c.e()) {
                    a.this.F(interfaceC0548c);
                }
            }

            @Override // h0.InterfaceC0550e
            public void b(InterfaceC0548c interfaceC0548c) {
                a.this.t(Math.max(a.this.g(), interfaceC0548c.g()));
            }

            @Override // h0.InterfaceC0550e
            public void c(InterfaceC0548c interfaceC0548c) {
                a.this.F(interfaceC0548c);
            }

            private C0130a() {
            }

            @Override // h0.InterfaceC0550e
            public void d(InterfaceC0548c interfaceC0548c) {
            }
        }

        public a() {
            if (I()) {
                return;
            }
            q(new RuntimeException("No data source supplier or supplier returned null."));
        }

        private synchronized boolean A(InterfaceC0548c interfaceC0548c) {
            if (!l() && interfaceC0548c == this.f9470i) {
                this.f9470i = null;
                return true;
            }
            return false;
        }

        private void B(InterfaceC0548c interfaceC0548c) {
            if (interfaceC0548c != null) {
                interfaceC0548c.close();
            }
        }

        private synchronized InterfaceC0548c C() {
            return this.f9471j;
        }

        private synchronized n D() {
            if (l() || this.f9469h >= C0551f.this.f9468a.size()) {
                return null;
            }
            List list = C0551f.this.f9468a;
            int i3 = this.f9469h;
            this.f9469h = i3 + 1;
            return (n) list.get(i3);
        }

        private void E(InterfaceC0548c interfaceC0548c, boolean z3) {
            InterfaceC0548c interfaceC0548c2;
            synchronized (this) {
                if (interfaceC0548c == this.f9470i && interfaceC0548c != (interfaceC0548c2 = this.f9471j)) {
                    if (interfaceC0548c2 == null || z3) {
                        this.f9471j = interfaceC0548c;
                    } else {
                        interfaceC0548c2 = null;
                    }
                    B(interfaceC0548c2);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void F(InterfaceC0548c interfaceC0548c) {
            if (A(interfaceC0548c)) {
                if (interfaceC0548c != C()) {
                    B(interfaceC0548c);
                }
                if (I()) {
                    return;
                }
                r(interfaceC0548c.f(), interfaceC0548c.a());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void G(InterfaceC0548c interfaceC0548c) {
            E(interfaceC0548c, interfaceC0548c.e());
            if (interfaceC0548c == C()) {
                v(null, interfaceC0548c.e(), interfaceC0548c.a());
            }
        }

        private synchronized boolean H(InterfaceC0548c interfaceC0548c) {
            if (l()) {
                return false;
            }
            this.f9470i = interfaceC0548c;
            return true;
        }

        private boolean I() {
            n nVarD = D();
            InterfaceC0548c interfaceC0548c = nVarD != null ? (InterfaceC0548c) nVarD.get() : null;
            if (!H(interfaceC0548c) || interfaceC0548c == null) {
                B(interfaceC0548c);
                return false;
            }
            interfaceC0548c.h(new C0130a(), V.a.b());
            return true;
        }

        @Override // h0.AbstractC0546a, h0.InterfaceC0548c
        public synchronized Object b() {
            InterfaceC0548c interfaceC0548cC;
            interfaceC0548cC = C();
            return interfaceC0548cC != null ? interfaceC0548cC.b() : null;
        }

        @Override // h0.AbstractC0546a, h0.InterfaceC0548c
        public boolean close() {
            synchronized (this) {
                try {
                    if (!super.close()) {
                        return false;
                    }
                    InterfaceC0548c interfaceC0548c = this.f9470i;
                    this.f9470i = null;
                    InterfaceC0548c interfaceC0548c2 = this.f9471j;
                    this.f9471j = null;
                    B(interfaceC0548c2);
                    B(interfaceC0548c);
                    return true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0011  */
        @Override // h0.AbstractC0546a, h0.InterfaceC0548c
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public synchronized boolean d() {
            /*
                r1 = this;
                monitor-enter(r1)
                h0.c r0 = r1.C()     // Catch: java.lang.Throwable -> Lf
                if (r0 == 0) goto L11
                boolean r0 = r0.d()     // Catch: java.lang.Throwable -> Lf
                if (r0 == 0) goto L11
                r0 = 1
                goto L12
            Lf:
                r0 = move-exception
                goto L14
            L11:
                r0 = 0
            L12:
                monitor-exit(r1)
                return r0
            L14:
                monitor-exit(r1)     // Catch: java.lang.Throwable -> Lf
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: h0.C0551f.a.d():boolean");
        }
    }

    private C0551f(List list) {
        k.c(!list.isEmpty(), "List of suppliers is empty!");
        this.f9468a = list;
    }

    public static C0551f b(List list) {
        return new C0551f(list);
    }

    @Override // X.n
    /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
    public InterfaceC0548c get() {
        return new a();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof C0551f) {
            return X.i.a(this.f9468a, ((C0551f) obj).f9468a);
        }
        return false;
    }

    public int hashCode() {
        return this.f9468a.hashCode();
    }

    public String toString() {
        return X.i.b(this).b("list", this.f9468a).toString();
    }
}
