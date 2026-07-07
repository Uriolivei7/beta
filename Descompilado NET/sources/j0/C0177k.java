package J0;

import J0.C0177k;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import kotlin.Lazy;
import r2.AbstractC0681d;
import r2.EnumC0684g;
import s2.AbstractC0696D;

/* JADX INFO: renamed from: J0.k, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0177k implements X.n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final InterfaceC0183q f582a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final R0.D f583b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final InterfaceC0182p f584c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final H0.t f585d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f586e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final S.d f587f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final S.d f588g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Map f589h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final Lazy f590i;

    /* JADX INFO: renamed from: J0.k$a */
    public static final class a implements InterfaceC0169c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Lazy f591a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Lazy f592b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Lazy f593c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final Lazy f594d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final Lazy f595e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final Lazy f596f;

        a(final C0177k c0177k) {
            EnumC0684g enumC0684g = EnumC0684g.f10565b;
            this.f591a = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: J0.e
                @Override // C2.a
                public final Object a() {
                    return C0177k.a.p(c0177k);
                }
            });
            this.f592b = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: J0.f
                @Override // C2.a
                public final Object a() {
                    return C0177k.a.o(this.f573b, c0177k);
                }
            });
            this.f593c = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: J0.g
                @Override // C2.a
                public final Object a() {
                    return C0177k.a.r(c0177k);
                }
            });
            this.f594d = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: J0.h
                @Override // C2.a
                public final Object a() {
                    return C0177k.a.q(this.f576b, c0177k);
                }
            });
            this.f595e = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: J0.i
                @Override // C2.a
                public final Object a() {
                    return C0177k.a.k(c0177k, this);
                }
            });
            this.f596f = AbstractC0681d.b(enumC0684g, new C2.a() { // from class: J0.j
                @Override // C2.a
                public final Object a() {
                    return C0177k.a.j(this.f580b, c0177k);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final X.g j(a aVar, C0177k c0177k) {
            D2.h.f(aVar, "this$0");
            D2.h.f(c0177k, "this$1");
            Map mapL = aVar.l();
            LinkedHashMap linkedHashMap = new LinkedHashMap(AbstractC0696D.c(mapL.size()));
            for (Map.Entry entry : mapL.entrySet()) {
                Object key = entry.getKey();
                S.k kVar = (S.k) entry.getValue();
                a0.i iVarG = c0177k.f583b.g(c0177k.f586e);
                D2.h.e(iVarG, "getPooledByteBufferFactory(...)");
                a0.l lVarH = c0177k.f583b.h();
                D2.h.e(lVarH, "getPooledByteStreams(...)");
                Executor executorC = c0177k.f584c.c();
                D2.h.e(executorC, "forLocalStorageRead(...)");
                Executor executorF = c0177k.f584c.f();
                D2.h.e(executorF, "forLocalStorageWrite(...)");
                linkedHashMap.put(key, new H0.j(kVar, iVarG, lVarH, executorC, executorF, c0177k.f585d));
            }
            return X.g.b(linkedHashMap);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final Map k(C0177k c0177k, a aVar) {
            D2.h.f(c0177k, "this$0");
            D2.h.f(aVar, "this$1");
            Map map = c0177k.f589h;
            if (map == null) {
                return AbstractC0696D.f();
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap(AbstractC0696D.c(map.size()));
            for (Map.Entry entry : map.entrySet()) {
                linkedHashMap.put(entry.getKey(), c0177k.f582a.a((S.d) entry.getValue()));
            }
            return linkedHashMap;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final H0.j o(a aVar, C0177k c0177k) {
            D2.h.f(aVar, "this$0");
            D2.h.f(c0177k, "this$1");
            S.k kVarM = aVar.m();
            a0.i iVarG = c0177k.f583b.g(c0177k.f586e);
            D2.h.e(iVarG, "getPooledByteBufferFactory(...)");
            a0.l lVarH = c0177k.f583b.h();
            D2.h.e(lVarH, "getPooledByteStreams(...)");
            Executor executorC = c0177k.f584c.c();
            D2.h.e(executorC, "forLocalStorageRead(...)");
            Executor executorF = c0177k.f584c.f();
            D2.h.e(executorF, "forLocalStorageWrite(...)");
            return new H0.j(kVarM, iVarG, lVarH, executorC, executorF, c0177k.f585d);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final S.k p(C0177k c0177k) {
            D2.h.f(c0177k, "this$0");
            return c0177k.f582a.a(c0177k.f587f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final H0.j q(a aVar, C0177k c0177k) {
            D2.h.f(aVar, "this$0");
            D2.h.f(c0177k, "this$1");
            S.k kVarN = aVar.n();
            a0.i iVarG = c0177k.f583b.g(c0177k.f586e);
            D2.h.e(iVarG, "getPooledByteBufferFactory(...)");
            a0.l lVarH = c0177k.f583b.h();
            D2.h.e(lVarH, "getPooledByteStreams(...)");
            Executor executorC = c0177k.f584c.c();
            D2.h.e(executorC, "forLocalStorageRead(...)");
            Executor executorF = c0177k.f584c.f();
            D2.h.e(executorF, "forLocalStorageWrite(...)");
            return new H0.j(kVarN, iVarG, lVarH, executorC, executorF, c0177k.f585d);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final S.k r(C0177k c0177k) {
            D2.h.f(c0177k, "this$0");
            return c0177k.f582a.a(c0177k.f588g);
        }

        @Override // J0.InterfaceC0169c
        public H0.j a() {
            return (H0.j) this.f592b.getValue();
        }

        @Override // J0.InterfaceC0169c
        public X.g b() {
            Object value = this.f596f.getValue();
            D2.h.e(value, "getValue(...)");
            return (X.g) value;
        }

        @Override // J0.InterfaceC0169c
        public H0.j c() {
            return (H0.j) this.f594d.getValue();
        }

        public Map l() {
            return (Map) this.f595e.getValue();
        }

        public S.k m() {
            return (S.k) this.f591a.getValue();
        }

        public S.k n() {
            return (S.k) this.f593c.getValue();
        }
    }

    public C0177k(InterfaceC0183q interfaceC0183q, R0.D d4, InterfaceC0182p interfaceC0182p, H0.t tVar, int i3, S.d dVar, S.d dVar2, Map<String, ? extends S.d> map) {
        D2.h.f(interfaceC0183q, "fileCacheFactory");
        D2.h.f(d4, "poolFactory");
        D2.h.f(interfaceC0182p, "executorSupplier");
        D2.h.f(tVar, "imageCacheStatsTracker");
        D2.h.f(dVar, "mainDiskCacheConfig");
        D2.h.f(dVar2, "smallImageDiskCacheConfig");
        this.f582a = interfaceC0183q;
        this.f583b = d4;
        this.f584c = interfaceC0182p;
        this.f585d = tVar;
        this.f586e = i3;
        this.f587f = dVar;
        this.f588g = dVar2;
        this.f589h = map;
        this.f590i = AbstractC0681d.b(EnumC0684g.f10565b, new C2.a() { // from class: J0.d
            @Override // C2.a
            public final Object a() {
                return C0177k.j(this.f571b);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final a j(C0177k c0177k) {
        D2.h.f(c0177k, "this$0");
        return new a(c0177k);
    }

    private final InterfaceC0169c l() {
        return (InterfaceC0169c) this.f590i.getValue();
    }

    @Override // X.n
    /* JADX INFO: renamed from: k, reason: merged with bridge method [inline-methods] */
    public InterfaceC0169c get() {
        return l();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public C0177k(InterfaceC0183q interfaceC0183q, InterfaceC0187v interfaceC0187v) {
        this(interfaceC0183q, interfaceC0187v.d(), interfaceC0187v.I(), interfaceC0187v.h(), interfaceC0187v.j(), interfaceC0187v.t(), interfaceC0187v.g(), interfaceC0187v.f());
        D2.h.f(interfaceC0183q, "fileCacheFactory");
        D2.h.f(interfaceC0187v, "config");
    }
}
