package J2;

import C2.l;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class j implements c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final c f804a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final l f805b;

    public static final class a implements Iterator, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Iterator f806b;

        a() {
            this.f806b = j.this.f804a.iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f806b.hasNext();
        }

        @Override // java.util.Iterator
        public Object next() {
            return j.this.f805b.d(this.f806b.next());
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    public j(c cVar, l lVar) {
        D2.h.f(cVar, "sequence");
        D2.h.f(lVar, "transformer");
        this.f804a = cVar;
        this.f805b = lVar;
    }

    @Override // J2.c
    public Iterator iterator() {
        return new a();
    }
}
