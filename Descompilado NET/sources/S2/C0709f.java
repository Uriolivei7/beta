package s2;

import java.util.Collection;
import java.util.Iterator;

/* JADX INFO: renamed from: s2.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
final class C0709f implements Collection, E2.a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Object[] f10608b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f10609c;

    public C0709f(Object[] objArr, boolean z3) {
        D2.h.f(objArr, "values");
        this.f10608b = objArr;
        this.f10609c = z3;
    }

    public int a() {
        return this.f10608b.length;
    }

    @Override // java.util.Collection
    public boolean add(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean contains(Object obj) {
        return C0715l.l(this.f10608b, obj);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection collection) {
        D2.h.f(collection, "elements");
        if (collection.isEmpty()) {
            return true;
        }
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return this.f10608b.length == 0;
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return D2.b.a(this.f10608b);
    }

    @Override // java.util.Collection
    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ int size() {
        return a();
    }

    @Override // java.util.Collection
    public Object[] toArray(Object[] objArr) {
        D2.h.f(objArr, "array");
        return D2.f.b(this, objArr);
    }

    @Override // java.util.Collection
    public final Object[] toArray() {
        return C0718o.a(this.f10608b, this.f10609c);
    }
}
