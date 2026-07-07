package com.facebook.imagepipeline.producers;

import J0.InterfaceC0187v;
import U0.b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0345e implements f0 {

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public static final Set f6128o = X.h.b("id", "uri_source");

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public static final Object f6129p = new Object();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final U0.b f6130b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f6131c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f6132d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final h0 f6133e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Object f6134f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final b.c f6135g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Map f6136h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f6137i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private I0.f f6138j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f6139k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f6140l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final List f6141m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final InterfaceC0187v f6142n;

    public C0345e(U0.b bVar, String str, h0 h0Var, Object obj, b.c cVar, boolean z3, boolean z4, I0.f fVar, InterfaceC0187v interfaceC0187v) {
        this(bVar, str, null, null, h0Var, obj, cVar, z3, z4, fVar, interfaceC0187v);
    }

    public static void c(List list) {
        if (list == null) {
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((g0) it.next()).a();
        }
    }

    public static void e(List list) {
        if (list == null) {
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((g0) it.next()).b();
        }
    }

    public static void f(List list) {
        if (list == null) {
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((g0) it.next()).d();
        }
    }

    public static void g(List list) {
        if (list == null) {
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((g0) it.next()).c();
        }
    }

    @Override // y0.InterfaceC0776a
    public void A(String str, Object obj) {
        if (f6128o.contains(str)) {
            return;
        }
        this.f6136h.put(str, obj);
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public void D(String str) {
        n0(str, "default");
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public h0 P() {
        return this.f6133e;
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public U0.b X() {
        return this.f6130b;
    }

    @Override // y0.InterfaceC0776a
    public Map a() {
        return this.f6136h;
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public void a0(g0 g0Var) {
        boolean z3;
        synchronized (this) {
            this.f6141m.add(g0Var);
            z3 = this.f6140l;
        }
        if (z3) {
            g0Var.a();
        }
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public synchronized boolean c0() {
        return this.f6139k;
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public b.c d0() {
        return this.f6135g;
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public InterfaceC0187v e0() {
        return this.f6142n;
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public String getId() {
        return this.f6131c;
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public Object i() {
        return this.f6134f;
    }

    public void j() {
        c(k());
    }

    public synchronized List k() {
        if (this.f6140l) {
            return null;
        }
        this.f6140l = true;
        return new ArrayList(this.f6141m);
    }

    public synchronized List m(boolean z3) {
        if (z3 == this.f6139k) {
            return null;
        }
        this.f6139k = z3;
        return new ArrayList(this.f6141m);
    }

    public synchronized List n(boolean z3) {
        if (z3 == this.f6137i) {
            return null;
        }
        this.f6137i = z3;
        return new ArrayList(this.f6141m);
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public void n0(String str, String str2) {
        this.f6136h.put("origin", str);
        this.f6136h.put("origin_sub", str2);
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public synchronized I0.f o() {
        return this.f6138j;
    }

    public synchronized List p(I0.f fVar) {
        if (fVar == this.f6138j) {
            return null;
        }
        this.f6138j = fVar;
        return new ArrayList(this.f6141m);
    }

    @Override // y0.InterfaceC0776a
    public void q(Map map) {
        if (map == null) {
            return;
        }
        for (Map.Entry entry : map.entrySet()) {
            A((String) entry.getKey(), entry.getValue());
        }
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public synchronized boolean v() {
        return this.f6137i;
    }

    @Override // y0.InterfaceC0776a
    public Object y(String str) {
        return this.f6136h.get(str);
    }

    @Override // com.facebook.imagepipeline.producers.f0
    public String z() {
        return this.f6132d;
    }

    public C0345e(U0.b bVar, String str, String str2, Map<String, ?> map, h0 h0Var, Object obj, b.c cVar, boolean z3, boolean z4, I0.f fVar, InterfaceC0187v interfaceC0187v) {
        this.f6130b = bVar;
        this.f6131c = str;
        HashMap map2 = new HashMap();
        this.f6136h = map2;
        map2.put("id", str);
        map2.put("uri_source", bVar == null ? "null-request" : bVar.v());
        q(map);
        this.f6132d = str2;
        this.f6133e = h0Var;
        this.f6134f = obj == null ? f6129p : obj;
        this.f6135g = cVar;
        this.f6137i = z3;
        this.f6138j = fVar;
        this.f6139k = z4;
        this.f6140l = false;
        this.f6141m = new ArrayList();
        this.f6142n = interfaceC0187v;
    }
}
