package s2;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: s2.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0710g extends AbstractC0707d {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f10610e = new a(null);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final Object[] f10611f = new Object[0];

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f10612b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Object[] f10613c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f10614d;

    /* JADX INFO: renamed from: s2.g$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public C0710g(int i3) {
        Object[] objArr;
        if (i3 == 0) {
            objArr = f10611f;
        } else {
            if (i3 <= 0) {
                throw new IllegalArgumentException("Illegal Capacity: " + i3);
            }
            objArr = new Object[i3];
        }
        this.f10613c = objArr;
    }

    private final void c(int i3, Collection collection) {
        Iterator it = collection.iterator();
        int length = this.f10613c.length;
        while (i3 < length && it.hasNext()) {
            this.f10613c[i3] = it.next();
            i3++;
        }
        int i4 = this.f10612b;
        for (int i5 = 0; i5 < i4 && it.hasNext(); i5++) {
            this.f10613c[i5] = it.next();
        }
        this.f10614d = size() + collection.size();
    }

    private final void e(int i3) {
        Object[] objArr = new Object[i3];
        Object[] objArr2 = this.f10613c;
        C0714k.f(objArr2, objArr, 0, this.f10612b, objArr2.length);
        Object[] objArr3 = this.f10613c;
        int length = objArr3.length;
        int i4 = this.f10612b;
        C0714k.f(objArr3, objArr, length - i4, 0, i4);
        this.f10612b = 0;
        this.f10613c = objArr;
    }

    private final int f(int i3) {
        return i3 == 0 ? AbstractC0711h.r(this.f10613c) : i3 - 1;
    }

    private final void h(int i3) {
        if (i3 < 0) {
            throw new IllegalStateException("Deque is too big.");
        }
        Object[] objArr = this.f10613c;
        if (i3 <= objArr.length) {
            return;
        }
        if (objArr == f10611f) {
            this.f10613c = new Object[H2.d.c(i3, 10)];
        } else {
            e(AbstractC0705b.f10601b.d(objArr.length, i3));
        }
    }

    private final int i(int i3) {
        if (i3 == AbstractC0711h.r(this.f10613c)) {
            return 0;
        }
        return i3 + 1;
    }

    private final int j(int i3) {
        return i3 < 0 ? i3 + this.f10613c.length : i3;
    }

    private final void k(int i3, int i4) {
        if (i3 < i4) {
            AbstractC0711h.j(this.f10613c, null, i3, i4);
            return;
        }
        Object[] objArr = this.f10613c;
        AbstractC0711h.j(objArr, null, i3, objArr.length);
        AbstractC0711h.j(this.f10613c, null, 0, i4);
    }

    private final int l(int i3) {
        Object[] objArr = this.f10613c;
        return i3 >= objArr.length ? i3 - objArr.length : i3;
    }

    private final void m() {
        ((AbstractList) this).modCount++;
    }

    private final void n(int i3, int i4) {
        int iL = l(this.f10612b + (i3 - 1));
        int iL2 = l(this.f10612b + (i4 - 1));
        while (i3 > 0) {
            int i5 = iL + 1;
            int iMin = Math.min(i3, Math.min(i5, iL2 + 1));
            Object[] objArr = this.f10613c;
            int i6 = iL2 - iMin;
            int i7 = iL - iMin;
            C0714k.f(objArr, objArr, i6 + 1, i7 + 1, i5);
            iL = j(i7);
            iL2 = j(i6);
            i3 -= iMin;
        }
    }

    private final void o(int i3, int i4) {
        int iL = l(this.f10612b + i4);
        int iL2 = l(this.f10612b + i3);
        int size = size();
        while (true) {
            size -= i4;
            if (size <= 0) {
                return;
            }
            Object[] objArr = this.f10613c;
            i4 = Math.min(size, Math.min(objArr.length - iL, objArr.length - iL2));
            Object[] objArr2 = this.f10613c;
            int i5 = iL + i4;
            C0714k.f(objArr2, objArr2, iL2, iL, i5);
            iL = l(i5);
            iL2 = l(iL2 + i4);
        }
    }

    @Override // s2.AbstractC0707d
    public int a() {
        return this.f10614d;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Object obj) {
        addLast(obj);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection collection) {
        D2.h.f(collection, "elements");
        if (collection.isEmpty()) {
            return false;
        }
        m();
        h(size() + collection.size());
        c(l(this.f10612b + size()), collection);
        return true;
    }

    public final void addFirst(Object obj) {
        m();
        h(size() + 1);
        int iF = f(this.f10612b);
        this.f10612b = iF;
        this.f10613c[iF] = obj;
        this.f10614d = size() + 1;
    }

    public final void addLast(Object obj) {
        m();
        h(size() + 1);
        this.f10613c[l(this.f10612b + size())] = obj;
        this.f10614d = size() + 1;
    }

    @Override // s2.AbstractC0707d
    public Object b(int i3) {
        AbstractC0705b.f10601b.a(i3, size());
        if (i3 == AbstractC0717n.i(this)) {
            return removeLast();
        }
        if (i3 == 0) {
            return removeFirst();
        }
        m();
        int iL = l(this.f10612b + i3);
        Object obj = this.f10613c[iL];
        if (i3 < (size() >> 1)) {
            int i4 = this.f10612b;
            if (iL >= i4) {
                Object[] objArr = this.f10613c;
                C0714k.f(objArr, objArr, i4 + 1, i4, iL);
            } else {
                Object[] objArr2 = this.f10613c;
                C0714k.f(objArr2, objArr2, 1, 0, iL);
                Object[] objArr3 = this.f10613c;
                objArr3[0] = objArr3[objArr3.length - 1];
                int i5 = this.f10612b;
                C0714k.f(objArr3, objArr3, i5 + 1, i5, objArr3.length - 1);
            }
            Object[] objArr4 = this.f10613c;
            int i6 = this.f10612b;
            objArr4[i6] = null;
            this.f10612b = i(i6);
        } else {
            int iL2 = l(this.f10612b + AbstractC0717n.i(this));
            if (iL <= iL2) {
                Object[] objArr5 = this.f10613c;
                C0714k.f(objArr5, objArr5, iL, iL + 1, iL2 + 1);
            } else {
                Object[] objArr6 = this.f10613c;
                C0714k.f(objArr6, objArr6, iL, iL + 1, objArr6.length);
                Object[] objArr7 = this.f10613c;
                objArr7[objArr7.length - 1] = objArr7[0];
                C0714k.f(objArr7, objArr7, 0, 1, iL2 + 1);
            }
            this.f10613c[iL2] = null;
        }
        this.f10614d = size() - 1;
        return obj;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        if (!isEmpty()) {
            m();
            k(this.f10612b, l(this.f10612b + size()));
        }
        this.f10612b = 0;
        this.f10614d = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int i3) {
        AbstractC0705b.f10601b.a(i3, size());
        return this.f10613c[l(this.f10612b + i3)];
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object obj) {
        int i3;
        int iL = l(this.f10612b + size());
        int length = this.f10612b;
        if (length < iL) {
            while (length < iL) {
                if (D2.h.b(obj, this.f10613c[length])) {
                    i3 = this.f10612b;
                } else {
                    length++;
                }
            }
            return -1;
        }
        if (length < iL) {
            return -1;
        }
        int length2 = this.f10613c.length;
        while (true) {
            if (length >= length2) {
                for (int i4 = 0; i4 < iL; i4++) {
                    if (D2.h.b(obj, this.f10613c[i4])) {
                        length = i4 + this.f10613c.length;
                        i3 = this.f10612b;
                    }
                }
                return -1;
            }
            if (D2.h.b(obj, this.f10613c[length])) {
                i3 = this.f10612b;
                break;
            }
            length++;
        }
        return length - i3;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object obj) {
        int iR;
        int i3;
        int iL = l(this.f10612b + size());
        int i4 = this.f10612b;
        if (i4 < iL) {
            iR = iL - 1;
            if (i4 <= iR) {
                while (!D2.h.b(obj, this.f10613c[iR])) {
                    if (iR != i4) {
                        iR--;
                    }
                }
                i3 = this.f10612b;
                return iR - i3;
            }
            return -1;
        }
        if (i4 > iL) {
            int i5 = iL - 1;
            while (true) {
                if (-1 >= i5) {
                    iR = AbstractC0711h.r(this.f10613c);
                    int i6 = this.f10612b;
                    if (i6 <= iR) {
                        while (!D2.h.b(obj, this.f10613c[iR])) {
                            if (iR != i6) {
                                iR--;
                            }
                        }
                        i3 = this.f10612b;
                    }
                } else {
                    if (D2.h.b(obj, this.f10613c[i5])) {
                        iR = i5 + this.f10613c.length;
                        i3 = this.f10612b;
                        break;
                    }
                    i5--;
                }
            }
        }
        return -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object obj) {
        int iIndexOf = indexOf(obj);
        if (iIndexOf == -1) {
            return false;
        }
        remove(iIndexOf);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean removeAll(Collection collection) {
        int iL;
        D2.h.f(collection, "elements");
        boolean z3 = false;
        z3 = false;
        z3 = false;
        if (!isEmpty() && this.f10613c.length != 0) {
            int iL2 = l(this.f10612b + size());
            int i3 = this.f10612b;
            if (i3 < iL2) {
                iL = i3;
                while (i3 < iL2) {
                    Object obj = this.f10613c[i3];
                    if (collection.contains(obj)) {
                        z3 = true;
                    } else {
                        this.f10613c[iL] = obj;
                        iL++;
                    }
                    i3++;
                }
                AbstractC0711h.j(this.f10613c, null, iL, iL2);
            } else {
                int length = this.f10613c.length;
                boolean z4 = false;
                int i4 = i3;
                while (i3 < length) {
                    Object[] objArr = this.f10613c;
                    Object obj2 = objArr[i3];
                    objArr[i3] = null;
                    if (collection.contains(obj2)) {
                        z4 = true;
                    } else {
                        this.f10613c[i4] = obj2;
                        i4++;
                    }
                    i3++;
                }
                iL = l(i4);
                for (int i5 = 0; i5 < iL2; i5++) {
                    Object[] objArr2 = this.f10613c;
                    Object obj3 = objArr2[i5];
                    objArr2[i5] = null;
                    if (collection.contains(obj3)) {
                        z4 = true;
                    } else {
                        this.f10613c[iL] = obj3;
                        iL = i(iL);
                    }
                }
                z3 = z4;
            }
            if (z3) {
                m();
                this.f10614d = j(iL - this.f10612b);
            }
        }
        return z3;
    }

    public final Object removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty.");
        }
        m();
        Object[] objArr = this.f10613c;
        int i3 = this.f10612b;
        Object obj = objArr[i3];
        objArr[i3] = null;
        this.f10612b = i(i3);
        this.f10614d = size() - 1;
        return obj;
    }

    public final Object removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty.");
        }
        m();
        int iL = l(this.f10612b + AbstractC0717n.i(this));
        Object[] objArr = this.f10613c;
        Object obj = objArr[iL];
        objArr[iL] = null;
        this.f10614d = size() - 1;
        return obj;
    }

    @Override // java.util.AbstractList
    protected void removeRange(int i3, int i4) {
        AbstractC0705b.f10601b.c(i3, i4, size());
        int i5 = i4 - i3;
        if (i5 == 0) {
            return;
        }
        if (i5 == size()) {
            clear();
            return;
        }
        if (i5 == 1) {
            remove(i3);
            return;
        }
        m();
        if (i3 < size() - i4) {
            n(i3, i4);
            int iL = l(this.f10612b + i5);
            k(this.f10612b, iL);
            this.f10612b = iL;
        } else {
            o(i3, i4);
            int iL2 = l(this.f10612b + size());
            k(j(iL2 - i5), iL2);
        }
        this.f10614d = size() - i5;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean retainAll(Collection collection) {
        int iL;
        D2.h.f(collection, "elements");
        boolean z3 = false;
        z3 = false;
        z3 = false;
        if (!isEmpty() && this.f10613c.length != 0) {
            int iL2 = l(this.f10612b + size());
            int i3 = this.f10612b;
            if (i3 < iL2) {
                iL = i3;
                while (i3 < iL2) {
                    Object obj = this.f10613c[i3];
                    if (collection.contains(obj)) {
                        this.f10613c[iL] = obj;
                        iL++;
                    } else {
                        z3 = true;
                    }
                    i3++;
                }
                AbstractC0711h.j(this.f10613c, null, iL, iL2);
            } else {
                int length = this.f10613c.length;
                boolean z4 = false;
                int i4 = i3;
                while (i3 < length) {
                    Object[] objArr = this.f10613c;
                    Object obj2 = objArr[i3];
                    objArr[i3] = null;
                    if (collection.contains(obj2)) {
                        this.f10613c[i4] = obj2;
                        i4++;
                    } else {
                        z4 = true;
                    }
                    i3++;
                }
                iL = l(i4);
                for (int i5 = 0; i5 < iL2; i5++) {
                    Object[] objArr2 = this.f10613c;
                    Object obj3 = objArr2[i5];
                    objArr2[i5] = null;
                    if (collection.contains(obj3)) {
                        this.f10613c[iL] = obj3;
                        iL = i(iL);
                    } else {
                        z4 = true;
                    }
                }
                z3 = z4;
            }
            if (z3) {
                m();
                this.f10614d = j(iL - this.f10612b);
            }
        }
        return z3;
    }

    @Override // java.util.AbstractList, java.util.List
    public Object set(int i3, Object obj) {
        AbstractC0705b.f10601b.a(i3, size());
        int iL = l(this.f10612b + i3);
        Object[] objArr = this.f10613c;
        Object obj2 = objArr[iL];
        objArr[iL] = obj;
        return obj2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray(Object[] objArr) {
        D2.h.f(objArr, "array");
        if (objArr.length < size()) {
            objArr = C0712i.a(objArr, size());
        }
        int iL = l(this.f10612b + size());
        int i3 = this.f10612b;
        if (i3 < iL) {
            C0714k.h(this.f10613c, objArr, 0, i3, iL, 2, null);
        } else if (!isEmpty()) {
            Object[] objArr2 = this.f10613c;
            C0714k.f(objArr2, objArr, 0, this.f10612b, objArr2.length);
            Object[] objArr3 = this.f10613c;
            C0714k.f(objArr3, objArr, objArr3.length - this.f10612b, 0, iL);
        }
        return C0718o.c(size(), objArr);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i3, Object obj) {
        AbstractC0705b.f10601b.b(i3, size());
        if (i3 == size()) {
            addLast(obj);
            return;
        }
        if (i3 == 0) {
            addFirst(obj);
            return;
        }
        m();
        h(size() + 1);
        int iL = l(this.f10612b + i3);
        if (i3 < ((size() + 1) >> 1)) {
            int iF = f(iL);
            int iF2 = f(this.f10612b);
            int i4 = this.f10612b;
            if (iF >= i4) {
                Object[] objArr = this.f10613c;
                objArr[iF2] = objArr[i4];
                C0714k.f(objArr, objArr, i4, i4 + 1, iF + 1);
            } else {
                Object[] objArr2 = this.f10613c;
                C0714k.f(objArr2, objArr2, i4 - 1, i4, objArr2.length);
                Object[] objArr3 = this.f10613c;
                objArr3[objArr3.length - 1] = objArr3[0];
                C0714k.f(objArr3, objArr3, 0, 1, iF + 1);
            }
            this.f10613c[iF] = obj;
            this.f10612b = iF2;
        } else {
            int iL2 = l(this.f10612b + size());
            if (iL < iL2) {
                Object[] objArr4 = this.f10613c;
                C0714k.f(objArr4, objArr4, iL + 1, iL, iL2);
            } else {
                Object[] objArr5 = this.f10613c;
                C0714k.f(objArr5, objArr5, 1, 0, iL2);
                Object[] objArr6 = this.f10613c;
                objArr6[0] = objArr6[objArr6.length - 1];
                C0714k.f(objArr6, objArr6, iL + 1, iL, objArr6.length - 1);
            }
            this.f10613c[iL] = obj;
        }
        this.f10614d = size() + 1;
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int i3, Collection collection) {
        D2.h.f(collection, "elements");
        AbstractC0705b.f10601b.b(i3, size());
        if (collection.isEmpty()) {
            return false;
        }
        if (i3 == size()) {
            return addAll(collection);
        }
        m();
        h(size() + collection.size());
        int iL = l(this.f10612b + size());
        int iL2 = l(this.f10612b + i3);
        int size = collection.size();
        if (i3 < ((size() + 1) >> 1)) {
            int i4 = this.f10612b;
            int length = i4 - size;
            if (iL2 < i4) {
                Object[] objArr = this.f10613c;
                C0714k.f(objArr, objArr, length, i4, objArr.length);
                if (size >= iL2) {
                    Object[] objArr2 = this.f10613c;
                    C0714k.f(objArr2, objArr2, objArr2.length - size, 0, iL2);
                } else {
                    Object[] objArr3 = this.f10613c;
                    C0714k.f(objArr3, objArr3, objArr3.length - size, 0, size);
                    Object[] objArr4 = this.f10613c;
                    C0714k.f(objArr4, objArr4, 0, size, iL2);
                }
            } else if (length >= 0) {
                Object[] objArr5 = this.f10613c;
                C0714k.f(objArr5, objArr5, length, i4, iL2);
            } else {
                Object[] objArr6 = this.f10613c;
                length += objArr6.length;
                int i5 = iL2 - i4;
                int length2 = objArr6.length - length;
                if (length2 >= i5) {
                    C0714k.f(objArr6, objArr6, length, i4, iL2);
                } else {
                    C0714k.f(objArr6, objArr6, length, i4, i4 + length2);
                    Object[] objArr7 = this.f10613c;
                    C0714k.f(objArr7, objArr7, 0, this.f10612b + length2, iL2);
                }
            }
            this.f10612b = length;
            c(j(iL2 - size), collection);
        } else {
            int i6 = iL2 + size;
            if (iL2 < iL) {
                int i7 = size + iL;
                Object[] objArr8 = this.f10613c;
                if (i7 <= objArr8.length) {
                    C0714k.f(objArr8, objArr8, i6, iL2, iL);
                } else if (i6 >= objArr8.length) {
                    C0714k.f(objArr8, objArr8, i6 - objArr8.length, iL2, iL);
                } else {
                    int length3 = iL - (i7 - objArr8.length);
                    C0714k.f(objArr8, objArr8, 0, length3, iL);
                    Object[] objArr9 = this.f10613c;
                    C0714k.f(objArr9, objArr9, i6, iL2, length3);
                }
            } else {
                Object[] objArr10 = this.f10613c;
                C0714k.f(objArr10, objArr10, size, 0, iL);
                Object[] objArr11 = this.f10613c;
                if (i6 >= objArr11.length) {
                    C0714k.f(objArr11, objArr11, i6 - objArr11.length, iL2, objArr11.length);
                } else {
                    C0714k.f(objArr11, objArr11, 0, objArr11.length - size, objArr11.length);
                    Object[] objArr12 = this.f10613c;
                    C0714k.f(objArr12, objArr12, i6, iL2, objArr12.length - size);
                }
            }
            c(iL2, collection);
        }
        return true;
    }

    public C0710g() {
        this.f10613c = f10611f;
    }

    public C0710g(Collection<Object> collection) {
        D2.h.f(collection, "elements");
        Object[] array = collection.toArray(new Object[0]);
        this.f10613c = array;
        this.f10614d = array.length;
        if (array.length == 0) {
            this.f10613c = f10611f;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }
}
