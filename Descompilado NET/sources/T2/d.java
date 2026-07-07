package t2;

import D2.h;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class d extends AbstractC0729a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final c f10792b;

    public d(c cVar) {
        h.f(cVar, "backing");
        this.f10792b = cVar;
    }

    @Override // s2.AbstractC0708e
    public int a() {
        return this.f10792b.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(Collection collection) {
        h.f(collection, "elements");
        throw new UnsupportedOperationException();
    }

    @Override // t2.AbstractC0729a
    public boolean c(Map.Entry entry) {
        h.f(entry, "element");
        return this.f10792b.p(entry);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.f10792b.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean containsAll(Collection collection) {
        h.f(collection, "elements");
        return this.f10792b.o(collection);
    }

    @Override // t2.AbstractC0729a
    public boolean e(Map.Entry entry) {
        h.f(entry, "element");
        return this.f10792b.J(entry);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
    public boolean add(Map.Entry entry) {
        h.f(entry, "element");
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.f10792b.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator iterator() {
        return this.f10792b.t();
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean removeAll(Collection collection) {
        h.f(collection, "elements");
        this.f10792b.m();
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean retainAll(Collection collection) {
        h.f(collection, "elements");
        this.f10792b.m();
        return super.retainAll(collection);
    }
}
