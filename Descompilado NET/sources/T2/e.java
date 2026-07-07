package t2;

import D2.h;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import s2.AbstractC0708e;

/* JADX INFO: loaded from: classes.dex */
public final class e extends AbstractC0708e implements Set, E2.b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final c f10793b;

    public e(c cVar) {
        h.f(cVar, "backing");
        this.f10793b = cVar;
    }

    @Override // s2.AbstractC0708e
    public int a() {
        return this.f10793b.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(Collection collection) {
        h.f(collection, "elements");
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.f10793b.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.f10793b.containsKey(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.f10793b.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator iterator() {
        return this.f10793b.D();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return this.f10793b.M(obj);
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean removeAll(Collection collection) {
        h.f(collection, "elements");
        this.f10793b.m();
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean retainAll(Collection collection) {
        h.f(collection, "elements");
        this.f10793b.m();
        return super.retainAll(collection);
    }
}
