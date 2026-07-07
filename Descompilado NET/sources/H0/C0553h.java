package h0;

import X.k;
import X.n;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: renamed from: h0.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0553h implements n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final List f9474a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f9475b;

    private C0553h(List list, boolean z3) {
        k.c(!list.isEmpty(), "List of suppliers is empty!");
        this.f9474a = list;
        this.f9475b = z3;
    }

    public static C0553h c(List list, boolean z3) {
        return new C0553h(list, z3);
    }

    @Override // X.n
    /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
    public InterfaceC0548c get() {
        return new a();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof C0553h) {
            return X.i.a(this.f9474a, ((C0553h) obj).f9474a);
        }
        return false;
    }

    public int hashCode() {
        return this.f9474a.hashCode();
    }

    public String toString() {
        return X.i.b(this).b("list", this.f9474a).toString();
    }

    /* JADX INFO: renamed from: h0.h$a */
    private class a extends AbstractC0546a {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private ArrayList f9476h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private int f9477i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private int f9478j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private AtomicInteger f9479k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private Throwable f9480l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        private Map f9481m;

        public a() {
            if (C0553h.this.f9475b) {
                return;
            }
            B();
        }

        private void A(InterfaceC0548c interfaceC0548c) {
            if (interfaceC0548c != null) {
                interfaceC0548c.close();
            }
        }

        private void B() {
            if (this.f9479k != null) {
                return;
            }
            synchronized (this) {
                try {
                    if (this.f9479k == null) {
                        this.f9479k = new AtomicInteger(0);
                        int size = C0553h.this.f9474a.size();
                        this.f9478j = size;
                        this.f9477i = size;
                        this.f9476h = new ArrayList(size);
                        for (int i3 = 0; i3 < size; i3++) {
                            InterfaceC0548c interfaceC0548c = (InterfaceC0548c) ((n) C0553h.this.f9474a.get(i3)).get();
                            this.f9476h.add(interfaceC0548c);
                            interfaceC0548c.h(new C0131a(i3), V.a.b());
                            if (!interfaceC0548c.d()) {
                            }
                        }
                    }
                } finally {
                }
            }
        }

        private synchronized InterfaceC0548c C(int i3) {
            InterfaceC0548c interfaceC0548c;
            ArrayList arrayList = this.f9476h;
            interfaceC0548c = null;
            if (arrayList != null && i3 < arrayList.size()) {
                interfaceC0548c = (InterfaceC0548c) this.f9476h.set(i3, null);
            }
            return interfaceC0548c;
        }

        private synchronized InterfaceC0548c D(int i3) {
            ArrayList arrayList;
            arrayList = this.f9476h;
            return (arrayList == null || i3 >= arrayList.size()) ? null : (InterfaceC0548c) this.f9476h.get(i3);
        }

        private synchronized InterfaceC0548c E() {
            return D(this.f9477i);
        }

        private void F() {
            Throwable th;
            if (this.f9479k.incrementAndGet() != this.f9478j || (th = this.f9480l) == null) {
                return;
            }
            r(th, this.f9481m);
        }

        private void G(int i3, InterfaceC0548c interfaceC0548c, boolean z3) {
            synchronized (this) {
                try {
                    int i4 = this.f9477i;
                    if (interfaceC0548c == D(i3) && i3 != this.f9477i) {
                        if (E() == null || (z3 && i3 < this.f9477i)) {
                            this.f9477i = i3;
                        } else {
                            i3 = i4;
                        }
                        while (i4 > i3) {
                            A(C(i4));
                            i4--;
                        }
                    }
                } finally {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void H(int i3, InterfaceC0548c interfaceC0548c) {
            A(J(i3, interfaceC0548c));
            if (i3 == 0) {
                this.f9480l = interfaceC0548c.f();
                this.f9481m = interfaceC0548c.a();
            }
            F();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void I(int i3, InterfaceC0548c interfaceC0548c) {
            G(i3, interfaceC0548c, interfaceC0548c.e());
            if (interfaceC0548c == E()) {
                v(null, i3 == 0 && interfaceC0548c.e(), interfaceC0548c.a());
            }
            F();
        }

        private synchronized InterfaceC0548c J(int i3, InterfaceC0548c interfaceC0548c) {
            if (interfaceC0548c == E()) {
                return null;
            }
            if (interfaceC0548c != D(i3)) {
                return interfaceC0548c;
            }
            return C(i3);
        }

        @Override // h0.AbstractC0546a, h0.InterfaceC0548c
        public synchronized Object b() {
            InterfaceC0548c interfaceC0548cE;
            try {
                if (C0553h.this.f9475b) {
                    B();
                }
                interfaceC0548cE = E();
            } catch (Throwable th) {
                throw th;
            }
            return interfaceC0548cE != null ? interfaceC0548cE.b() : null;
        }

        @Override // h0.AbstractC0546a, h0.InterfaceC0548c
        public boolean close() {
            if (C0553h.this.f9475b) {
                B();
            }
            synchronized (this) {
                try {
                    if (!super.close()) {
                        return false;
                    }
                    ArrayList arrayList = this.f9476h;
                    this.f9476h = null;
                    if (arrayList == null) {
                        return true;
                    }
                    for (int i3 = 0; i3 < arrayList.size(); i3++) {
                        A((InterfaceC0548c) arrayList.get(i3));
                    }
                    return true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x001d  */
        @Override // h0.AbstractC0546a, h0.InterfaceC0548c
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public synchronized boolean d() {
            /*
                r1 = this;
                monitor-enter(r1)
                h0.h r0 = h0.C0553h.this     // Catch: java.lang.Throwable -> Ld
                boolean r0 = h0.C0553h.a(r0)     // Catch: java.lang.Throwable -> Ld
                if (r0 == 0) goto Lf
                r1.B()     // Catch: java.lang.Throwable -> Ld
                goto Lf
            Ld:
                r0 = move-exception
                goto L20
            Lf:
                h0.c r0 = r1.E()     // Catch: java.lang.Throwable -> Ld
                if (r0 == 0) goto L1d
                boolean r0 = r0.d()     // Catch: java.lang.Throwable -> Ld
                if (r0 == 0) goto L1d
                r0 = 1
                goto L1e
            L1d:
                r0 = 0
            L1e:
                monitor-exit(r1)
                return r0
            L20:
                monitor-exit(r1)     // Catch: java.lang.Throwable -> Ld
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: h0.C0553h.a.d():boolean");
        }

        /* JADX INFO: renamed from: h0.h$a$a, reason: collision with other inner class name */
        private class C0131a implements InterfaceC0550e {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private int f9483a;

            public C0131a(int i3) {
                this.f9483a = i3;
            }

            @Override // h0.InterfaceC0550e
            public void a(InterfaceC0548c interfaceC0548c) {
                if (interfaceC0548c.d()) {
                    a.this.I(this.f9483a, interfaceC0548c);
                } else if (interfaceC0548c.e()) {
                    a.this.H(this.f9483a, interfaceC0548c);
                }
            }

            @Override // h0.InterfaceC0550e
            public void b(InterfaceC0548c interfaceC0548c) {
                if (this.f9483a == 0) {
                    a.this.t(interfaceC0548c.g());
                }
            }

            @Override // h0.InterfaceC0550e
            public void c(InterfaceC0548c interfaceC0548c) {
                a.this.H(this.f9483a, interfaceC0548c);
            }

            @Override // h0.InterfaceC0550e
            public void d(InterfaceC0548c interfaceC0548c) {
            }
        }
    }
}
