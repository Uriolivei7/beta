package t2;

import D2.h;
import java.util.Collection;
import java.util.Iterator;
import s2.AbstractC0706c;

/* JADX INFO: loaded from: classes.dex */
public final class f extends AbstractC0706c implements Collection, E2.b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final c f10794b;

    public f(c cVar) {
        h.f(cVar, "backing");
        this.f10794b = cVar;
    }

    @Override // s2.AbstractC0706c
    public int a() {
        return this.f10794b.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean add(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection collection) {
        h.f(collection, "elements");
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.f10794b.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return this.f10794b.containsValue(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.f10794b.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return this.f10794b.P();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean remove(Object obj) {
        return this.f10794b.N(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection collection) {
        h.f(collection, "elements");
        this.f10794b.m();
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection collection) {
        h.f(collection, "elements");
        this.f10794b.m();
        return super.retainAll(collection);
    }
}
