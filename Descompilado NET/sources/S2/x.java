package s2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class x extends w {

    public static final class a implements J2.c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Iterable f10615a;

        public a(Iterable iterable) {
            this.f10615a = iterable;
        }

        @Override // J2.c
        public Iterator iterator() {
            return this.f10615a.iterator();
        }
    }

    public static J2.c H(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        return new a(iterable);
    }

    public static double I(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        Iterator it = iterable.iterator();
        double dFloatValue = 0.0d;
        int i3 = 0;
        while (it.hasNext()) {
            dFloatValue += (double) ((Number) it.next()).floatValue();
            i3++;
            if (i3 < 0) {
                p.o();
            }
        }
        if (i3 == 0) {
            return Double.NaN;
        }
        return dFloatValue / ((double) i3);
    }

    public static boolean J(Iterable iterable, Object obj) {
        D2.h.f(iterable, "<this>");
        return iterable instanceof Collection ? ((Collection) iterable).contains(obj) : O(iterable, obj) >= 0;
    }

    public static List K(List list, int i3) {
        D2.h.f(list, "<this>");
        if (i3 >= 0) {
            return AbstractC0717n.b0(list, H2.d.c(list.size() - i3, 0));
        }
        throw new IllegalArgumentException(("Requested element count " + i3 + " is less than zero.").toString());
    }

    public static Collection L(Iterable iterable, Collection collection) {
        D2.h.f(iterable, "<this>");
        D2.h.f(collection, "destination");
        for (Object obj : iterable) {
            if (obj != null) {
                collection.add(obj);
            }
        }
        return collection;
    }

    public static final Object M(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        if (iterable instanceof List) {
            return AbstractC0717n.N((List) iterable);
        }
        Iterator it = iterable.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        throw new NoSuchElementException("Collection is empty.");
    }

    public static Object N(List list) {
        D2.h.f(list, "<this>");
        if (list.isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        return list.get(0);
    }

    public static final int O(Iterable iterable, Object obj) {
        D2.h.f(iterable, "<this>");
        if (iterable instanceof List) {
            return ((List) iterable).indexOf(obj);
        }
        int i3 = 0;
        for (Object obj2 : iterable) {
            if (i3 < 0) {
                AbstractC0717n.p();
            }
            if (D2.h.b(obj, obj2)) {
                return i3;
            }
            i3++;
        }
        return -1;
    }

    public static final Appendable P(Iterable iterable, Appendable appendable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i3, CharSequence charSequence4, C2.l lVar) throws IOException {
        D2.h.f(iterable, "<this>");
        D2.h.f(appendable, "buffer");
        D2.h.f(charSequence, "separator");
        D2.h.f(charSequence2, "prefix");
        D2.h.f(charSequence3, "postfix");
        D2.h.f(charSequence4, "truncated");
        appendable.append(charSequence2);
        int i4 = 0;
        for (Object obj : iterable) {
            i4++;
            if (i4 > 1) {
                appendable.append(charSequence);
            }
            if (i3 >= 0 && i4 > i3) {
                break;
            }
            K2.o.a(appendable, obj, lVar);
        }
        if (i3 >= 0 && i4 > i3) {
            appendable.append(charSequence4);
        }
        appendable.append(charSequence3);
        return appendable;
    }

    public static final String R(Iterable iterable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i3, CharSequence charSequence4, C2.l lVar) {
        D2.h.f(iterable, "<this>");
        D2.h.f(charSequence, "separator");
        D2.h.f(charSequence2, "prefix");
        D2.h.f(charSequence3, "postfix");
        D2.h.f(charSequence4, "truncated");
        String string = ((StringBuilder) P(iterable, new StringBuilder(), charSequence, charSequence2, charSequence3, i3, charSequence4, lVar)).toString();
        D2.h.e(string, "toString(...)");
        return string;
    }

    public static /* synthetic */ String S(Iterable iterable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i3, CharSequence charSequence4, C2.l lVar, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            charSequence = ", ";
        }
        CharSequence charSequence5 = (i4 & 2) != 0 ? "" : charSequence2;
        CharSequence charSequence6 = (i4 & 4) == 0 ? charSequence3 : "";
        if ((i4 & 8) != 0) {
            i3 = -1;
        }
        int i5 = i3;
        if ((i4 & 16) != 0) {
            charSequence4 = "...";
        }
        CharSequence charSequence7 = charSequence4;
        if ((i4 & 32) != 0) {
            lVar = null;
        }
        return R(iterable, charSequence, charSequence5, charSequence6, i5, charSequence7, lVar);
    }

    public static Object T(List list) {
        D2.h.f(list, "<this>");
        if (list.isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        return list.get(AbstractC0717n.i(list));
    }

    public static Object U(List list) {
        D2.h.f(list, "<this>");
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    public static Comparable V(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        Iterator it = iterable.iterator();
        if (!it.hasNext()) {
            return null;
        }
        Comparable comparable = (Comparable) it.next();
        while (it.hasNext()) {
            Comparable comparable2 = (Comparable) it.next();
            if (comparable.compareTo(comparable2) > 0) {
                comparable = comparable2;
            }
        }
        return comparable;
    }

    public static List W(Collection collection, Iterable iterable) {
        D2.h.f(collection, "<this>");
        D2.h.f(iterable, "elements");
        if (!(iterable instanceof Collection)) {
            ArrayList arrayList = new ArrayList(collection);
            AbstractC0717n.t(arrayList, iterable);
            return arrayList;
        }
        Collection collection2 = (Collection) iterable;
        ArrayList arrayList2 = new ArrayList(collection.size() + collection2.size());
        arrayList2.addAll(collection);
        arrayList2.addAll(collection2);
        return arrayList2;
    }

    public static List X(Collection collection, Object obj) {
        D2.h.f(collection, "<this>");
        ArrayList arrayList = new ArrayList(collection.size() + 1);
        arrayList.addAll(collection);
        arrayList.add(obj);
        return arrayList;
    }

    public static List Y(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        if ((iterable instanceof Collection) && ((Collection) iterable).size() <= 1) {
            return AbstractC0717n.e0(iterable);
        }
        List listF0 = f0(iterable);
        w.G(listF0);
        return listF0;
    }

    public static Object Z(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        if (iterable instanceof List) {
            return a0((List) iterable);
        }
        Iterator it = iterable.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("Collection is empty.");
        }
        Object next = it.next();
        if (it.hasNext()) {
            throw new IllegalArgumentException("Collection has more than one element.");
        }
        return next;
    }

    public static final Object a0(List list) {
        D2.h.f(list, "<this>");
        int size = list.size();
        if (size == 0) {
            throw new NoSuchElementException("List is empty.");
        }
        if (size == 1) {
            return list.get(0);
        }
        throw new IllegalArgumentException("List has more than one element.");
    }

    public static List b0(Iterable iterable, int i3) {
        D2.h.f(iterable, "<this>");
        if (i3 < 0) {
            throw new IllegalArgumentException(("Requested element count " + i3 + " is less than zero.").toString());
        }
        if (i3 == 0) {
            return AbstractC0717n.g();
        }
        if (iterable instanceof Collection) {
            if (i3 >= ((Collection) iterable).size()) {
                return AbstractC0717n.e0(iterable);
            }
            if (i3 == 1) {
                return AbstractC0717n.b(M(iterable));
            }
        }
        ArrayList arrayList = new ArrayList(i3);
        Iterator it = iterable.iterator();
        int i4 = 0;
        while (it.hasNext()) {
            arrayList.add(it.next());
            i4++;
            if (i4 == i3) {
                break;
            }
        }
        return p.m(arrayList);
    }

    public static final Collection c0(Iterable iterable, Collection collection) {
        D2.h.f(iterable, "<this>");
        D2.h.f(collection, "destination");
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            collection.add(it.next());
        }
        return collection;
    }

    public static float[] d0(Collection collection) {
        D2.h.f(collection, "<this>");
        float[] fArr = new float[collection.size()];
        Iterator it = collection.iterator();
        int i3 = 0;
        while (it.hasNext()) {
            fArr[i3] = ((Number) it.next()).floatValue();
            i3++;
        }
        return fArr;
    }

    public static List e0(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        if (!(iterable instanceof Collection)) {
            return p.m(f0(iterable));
        }
        Collection collection = (Collection) iterable;
        int size = collection.size();
        if (size == 0) {
            return AbstractC0717n.g();
        }
        if (size != 1) {
            return AbstractC0717n.g0(collection);
        }
        return AbstractC0717n.b(iterable instanceof List ? ((List) iterable).get(0) : iterable.iterator().next());
    }

    public static final List f0(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        return iterable instanceof Collection ? AbstractC0717n.g0((Collection) iterable) : (List) c0(iterable, new ArrayList());
    }

    public static List g0(Collection collection) {
        D2.h.f(collection, "<this>");
        return new ArrayList(collection);
    }

    public static Set h0(Iterable iterable) {
        D2.h.f(iterable, "<this>");
        if (!(iterable instanceof Collection)) {
            return N.c((Set) c0(iterable, new LinkedHashSet()));
        }
        Collection collection = (Collection) iterable;
        int size = collection.size();
        if (size == 0) {
            return L.b();
        }
        if (size != 1) {
            return (Set) c0(iterable, new LinkedHashSet(AbstractC0696D.c(collection.size())));
        }
        return M.a(iterable instanceof List ? ((List) iterable).get(0) : iterable.iterator().next());
    }

    public static List i0(Iterable iterable, Iterable iterable2) {
        D2.h.f(iterable, "<this>");
        D2.h.f(iterable2, "other");
        Iterator it = iterable.iterator();
        Iterator it2 = iterable2.iterator();
        ArrayList arrayList = new ArrayList(Math.min(AbstractC0717n.q(iterable, 10), AbstractC0717n.q(iterable2, 10)));
        while (it.hasNext() && it2.hasNext()) {
            arrayList.add(r2.n.a(it.next(), it2.next()));
        }
        return arrayList;
    }
}
