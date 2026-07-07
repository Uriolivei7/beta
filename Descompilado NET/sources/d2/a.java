package D2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
final class a implements Iterator, E2.a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Object[] f164b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f165c;

    public a(Object[] objArr) {
        h.f(objArr, "array");
        this.f164b = objArr;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.f165c < this.f164b.length;
    }

    @Override // java.util.Iterator
    public Object next() {
        try {
            Object[] objArr = this.f164b;
            int i3 = this.f165c;
            this.f165c = i3 + 1;
            return objArr[i3];
        } catch (ArrayIndexOutOfBoundsException e4) {
            this.f165c--;
            throw new NoSuchElementException(e4.getMessage());
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
