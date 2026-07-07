package J2;

import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class a implements c, b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final c f799a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f800b;

    /* JADX INFO: renamed from: J2.a$a, reason: collision with other inner class name */
    public static final class C0009a implements Iterator, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Iterator f801b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f802c;

        C0009a(a aVar) {
            this.f801b = aVar.f799a.iterator();
            this.f802c = aVar.f800b;
        }

        private final void a() {
            while (this.f802c > 0 && this.f801b.hasNext()) {
                this.f801b.next();
                this.f802c--;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            a();
            return this.f801b.hasNext();
        }

        @Override // java.util.Iterator
        public Object next() {
            a();
            return this.f801b.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    public a(c cVar, int i3) {
        D2.h.f(cVar, "sequence");
        this.f799a = cVar;
        this.f800b = i3;
        if (i3 >= 0) {
            return;
        }
        throw new IllegalArgumentException(("count must be non-negative, but was " + i3 + '.').toString());
    }

    @Override // J2.b
    public c a(int i3) {
        int i4 = this.f800b + i3;
        return i4 < 0 ? new a(this, i3) : new a(this.f799a, i4);
    }

    @Override // J2.c
    public Iterator iterator() {
        return new C0009a(this);
    }
}
