package l;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/* JADX INFO: renamed from: l.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0589a extends g implements Map {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    f f9609i;

    /* JADX INFO: renamed from: l.a$a, reason: collision with other inner class name */
    class C0135a extends f {
        C0135a() {
        }

        @Override // l.f
        protected void a() {
            C0589a.this.clear();
        }

        @Override // l.f
        protected Object b(int i3, int i4) {
            return C0589a.this.f9658c[(i3 << 1) + i4];
        }

        @Override // l.f
        protected Map c() {
            return C0589a.this;
        }

        @Override // l.f
        protected int d() {
            return C0589a.this.f9659d;
        }

        @Override // l.f
        protected int e(Object obj) {
            return C0589a.this.f(obj);
        }

        @Override // l.f
        protected int f(Object obj) {
            return C0589a.this.h(obj);
        }

        @Override // l.f
        protected void g(Object obj, Object obj2) {
            C0589a.this.put(obj, obj2);
        }

        @Override // l.f
        protected void h(int i3) {
            C0589a.this.k(i3);
        }

        @Override // l.f
        protected Object i(int i3, Object obj) {
            return C0589a.this.l(i3, obj);
        }
    }

    public C0589a() {
    }

    private f n() {
        if (this.f9609i == null) {
            this.f9609i = new C0135a();
        }
        return this.f9609i;
    }

    @Override // java.util.Map
    public Set entrySet() {
        return n().l();
    }

    @Override // java.util.Map
    public Set keySet() {
        return n().m();
    }

    public boolean o(Collection collection) {
        return f.p(this, collection);
    }

    @Override // java.util.Map
    public void putAll(Map map) {
        c(this.f9659d + map.size());
        for (Map.Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map
    public Collection values() {
        return n().n();
    }

    public C0589a(int i3) {
        super(i3);
    }

    public C0589a(g gVar) {
        super(gVar);
    }
}
