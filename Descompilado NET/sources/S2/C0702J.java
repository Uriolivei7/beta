package s2;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/* JADX INFO: renamed from: s2.J, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
final class C0702J extends AbstractC0707d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final List f10594b;

    /* JADX INFO: renamed from: s2.J$a */
    public static final class a implements ListIterator, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final ListIterator f10595b;

        a(int i3) {
            this.f10595b = C0702J.this.f10594b.listIterator(v.F(C0702J.this, i3));
        }

        @Override // java.util.ListIterator
        public void add(Object obj) {
            this.f10595b.add(obj);
            this.f10595b.previous();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.f10595b.hasPrevious();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.f10595b.hasNext();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public Object next() {
            return this.f10595b.previous();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return v.E(C0702J.this, this.f10595b.previousIndex());
        }

        @Override // java.util.ListIterator
        public Object previous() {
            return this.f10595b.next();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return v.E(C0702J.this, this.f10595b.nextIndex());
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            this.f10595b.remove();
        }

        @Override // java.util.ListIterator
        public void set(Object obj) {
            this.f10595b.set(obj);
        }
    }

    public C0702J(List<Object> list) {
        D2.h.f(list, "delegate");
        this.f10594b = list;
    }

    @Override // s2.AbstractC0707d
    public int a() {
        return this.f10594b.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i3, Object obj) {
        this.f10594b.add(v.F(this, i3), obj);
    }

    @Override // s2.AbstractC0707d
    public Object b(int i3) {
        return this.f10594b.remove(v.D(this, i3));
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f10594b.clear();
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int i3) {
        return this.f10594b.get(v.D(this, i3));
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator iterator() {
        return listIterator(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator listIterator() {
        return listIterator(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public Object set(int i3, Object obj) {
        return this.f10594b.set(v.D(this, i3), obj);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator listIterator(int i3) {
        return new a(i3);
    }
}
