package k;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: renamed from: k.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0581b implements Iterable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    c f9565b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private c f9566c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final WeakHashMap f9567d = new WeakHashMap();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f9568e = 0;

    /* JADX INFO: renamed from: k.b$a */
    static class a extends e {
        a(c cVar, c cVar2) {
            super(cVar, cVar2);
        }

        @Override // k.C0581b.e
        c b(c cVar) {
            return cVar.f9572e;
        }

        @Override // k.C0581b.e
        c c(c cVar) {
            return cVar.f9571d;
        }
    }

    /* JADX INFO: renamed from: k.b$b, reason: collision with other inner class name */
    private static class C0134b extends e {
        C0134b(c cVar, c cVar2) {
            super(cVar, cVar2);
        }

        @Override // k.C0581b.e
        c b(c cVar) {
            return cVar.f9571d;
        }

        @Override // k.C0581b.e
        c c(c cVar) {
            return cVar.f9572e;
        }
    }

    /* JADX INFO: renamed from: k.b$c */
    static class c implements Map.Entry {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final Object f9569b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final Object f9570c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        c f9571d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        c f9572e;

        c(Object obj, Object obj2) {
            this.f9569b = obj;
            this.f9570c = obj2;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof c)) {
                return false;
            }
            c cVar = (c) obj;
            return this.f9569b.equals(cVar.f9569b) && this.f9570c.equals(cVar.f9570c);
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            return this.f9569b;
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return this.f9570c;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.f9569b.hashCode() ^ this.f9570c.hashCode();
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object obj) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }

        public String toString() {
            return this.f9569b + "=" + this.f9570c;
        }
    }

    /* JADX INFO: renamed from: k.b$d */
    public class d extends f implements Iterator {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private c f9573b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f9574c = true;

        d() {
        }

        @Override // k.C0581b.f
        void a(c cVar) {
            c cVar2 = this.f9573b;
            if (cVar == cVar2) {
                c cVar3 = cVar2.f9572e;
                this.f9573b = cVar3;
                this.f9574c = cVar3 == null;
            }
        }

        @Override // java.util.Iterator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public Map.Entry next() {
            if (this.f9574c) {
                this.f9574c = false;
                this.f9573b = C0581b.this.f9565b;
            } else {
                c cVar = this.f9573b;
                this.f9573b = cVar != null ? cVar.f9571d : null;
            }
            return this.f9573b;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f9574c) {
                return C0581b.this.f9565b != null;
            }
            c cVar = this.f9573b;
            return (cVar == null || cVar.f9571d == null) ? false : true;
        }
    }

    /* JADX INFO: renamed from: k.b$e */
    private static abstract class e extends f implements Iterator {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        c f9576b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        c f9577c;

        e(c cVar, c cVar2) {
            this.f9576b = cVar2;
            this.f9577c = cVar;
        }

        private c e() {
            c cVar = this.f9577c;
            c cVar2 = this.f9576b;
            if (cVar == cVar2 || cVar2 == null) {
                return null;
            }
            return c(cVar);
        }

        @Override // k.C0581b.f
        public void a(c cVar) {
            if (this.f9576b == cVar && cVar == this.f9577c) {
                this.f9577c = null;
                this.f9576b = null;
            }
            c cVar2 = this.f9576b;
            if (cVar2 == cVar) {
                this.f9576b = b(cVar2);
            }
            if (this.f9577c == cVar) {
                this.f9577c = e();
            }
        }

        abstract c b(c cVar);

        abstract c c(c cVar);

        @Override // java.util.Iterator
        /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
        public Map.Entry next() {
            c cVar = this.f9577c;
            this.f9577c = e();
            return cVar;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f9577c != null;
        }
    }

    /* JADX INFO: renamed from: k.b$f */
    public static abstract class f {
        abstract void a(c cVar);
    }

    public Iterator a() {
        C0134b c0134b = new C0134b(this.f9566c, this.f9565b);
        this.f9567d.put(c0134b, Boolean.FALSE);
        return c0134b;
    }

    public Map.Entry b() {
        return this.f9565b;
    }

    protected c c(Object obj) {
        c cVar = this.f9565b;
        while (cVar != null && !cVar.f9569b.equals(obj)) {
            cVar = cVar.f9571d;
        }
        return cVar;
    }

    public d e() {
        d dVar = new d();
        this.f9567d.put(dVar, Boolean.FALSE);
        return dVar;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof C0581b)) {
            return false;
        }
        C0581b c0581b = (C0581b) obj;
        if (size() != c0581b.size()) {
            return false;
        }
        Iterator it = iterator();
        Iterator it2 = c0581b.iterator();
        while (it.hasNext() && it2.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object next = it2.next();
            if ((entry == null && next != null) || (entry != null && !entry.equals(next))) {
                return false;
            }
        }
        return (it.hasNext() || it2.hasNext()) ? false : true;
    }

    public Map.Entry f() {
        return this.f9566c;
    }

    c h(Object obj, Object obj2) {
        c cVar = new c(obj, obj2);
        this.f9568e++;
        c cVar2 = this.f9566c;
        if (cVar2 == null) {
            this.f9565b = cVar;
            this.f9566c = cVar;
            return cVar;
        }
        cVar2.f9571d = cVar;
        cVar.f9572e = cVar2;
        this.f9566c = cVar;
        return cVar;
    }

    public int hashCode() {
        Iterator it = iterator();
        int iHashCode = 0;
        while (it.hasNext()) {
            iHashCode += ((Map.Entry) it.next()).hashCode();
        }
        return iHashCode;
    }

    public Object i(Object obj, Object obj2) {
        c cVarC = c(obj);
        if (cVarC != null) {
            return cVarC.f9570c;
        }
        h(obj, obj2);
        return null;
    }

    @Override // java.lang.Iterable
    public Iterator iterator() {
        a aVar = new a(this.f9565b, this.f9566c);
        this.f9567d.put(aVar, Boolean.FALSE);
        return aVar;
    }

    public Object j(Object obj) {
        c cVarC = c(obj);
        if (cVarC == null) {
            return null;
        }
        this.f9568e--;
        if (!this.f9567d.isEmpty()) {
            Iterator it = this.f9567d.keySet().iterator();
            while (it.hasNext()) {
                ((f) it.next()).a(cVarC);
            }
        }
        c cVar = cVarC.f9572e;
        if (cVar != null) {
            cVar.f9571d = cVarC.f9571d;
        } else {
            this.f9565b = cVarC.f9571d;
        }
        c cVar2 = cVarC.f9571d;
        if (cVar2 != null) {
            cVar2.f9572e = cVar;
        } else {
            this.f9566c = cVar;
        }
        cVarC.f9571d = null;
        cVarC.f9572e = null;
        return cVarC.f9570c;
    }

    public int size() {
        return this.f9568e;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Iterator it = iterator();
        while (it.hasNext()) {
            sb.append(((Map.Entry) it.next()).toString());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
