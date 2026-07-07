package s2;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/* JADX INFO: renamed from: s2.K, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0703K extends AbstractC0705b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final List f10597c;

    /* JADX INFO: renamed from: s2.K$a */
    public static final class a implements ListIterator, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final ListIterator f10598b;

        a(int i3) {
            this.f10598b = C0703K.this.f10597c.listIterator(v.F(C0703K.this, i3));
        }

        @Override // java.util.ListIterator
        public void add(Object obj) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.f10598b.hasPrevious();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.f10598b.hasNext();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public Object next() {
            return this.f10598b.previous();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return v.E(C0703K.this, this.f10598b.previousIndex());
        }

        @Override // java.util.ListIterator
        public Object previous() {
            return this.f10598b.next();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return v.E(C0703K.this, this.f10598b.nextIndex());
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override // java.util.ListIterator
        public void set(Object obj) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    public C0703K(List<Object> list) {
        D2.h.f(list, "delegate");
        this.f10597c = list;
    }

    @Override // s2.AbstractC0704a
    public int a() {
        return this.f10597c.size();
    }

    @Override // s2.AbstractC0705b, java.util.List
    public Object get(int i3) {
        return this.f10597c.get(v.D(this, i3));
    }

    @Override // s2.AbstractC0705b, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator iterator() {
        return listIterator(0);
    }

    @Override // s2.AbstractC0705b, java.util.List
    public ListIterator listIterator() {
        return listIterator(0);
    }

    @Override // s2.AbstractC0705b, java.util.List
    public ListIterator listIterator(int i3) {
        return new a(i3);
    }
}
