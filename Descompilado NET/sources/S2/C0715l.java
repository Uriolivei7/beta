package s2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: s2.l, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0715l extends C0714k {
    public static Object A(Object[] objArr) {
        D2.h.f(objArr, "<this>");
        if (objArr.length == 1) {
            return objArr[0];
        }
        return null;
    }

    public static List B(Object[] objArr) {
        D2.h.f(objArr, "<this>");
        int length = objArr.length;
        return length != 0 ? length != 1 ? AbstractC0711h.C(objArr) : AbstractC0717n.b(objArr[0]) : AbstractC0717n.g();
    }

    public static List C(Object[] objArr) {
        D2.h.f(objArr, "<this>");
        return new ArrayList(p.d(objArr));
    }

    public static final boolean l(Object[] objArr, Object obj) {
        D2.h.f(objArr, "<this>");
        return t(objArr, obj) >= 0;
    }

    public static List m(Object[] objArr) {
        D2.h.f(objArr, "<this>");
        return (List) AbstractC0711h.n(objArr, new ArrayList());
    }

    public static Collection n(Object[] objArr, Collection collection) {
        D2.h.f(objArr, "<this>");
        D2.h.f(collection, "destination");
        for (Object obj : objArr) {
            if (obj != null) {
                collection.add(obj);
            }
        }
        return collection;
    }

    public static H2.c o(byte[] bArr) {
        D2.h.f(bArr, "<this>");
        return new H2.c(0, q(bArr));
    }

    public static H2.c p(Object[] objArr) {
        D2.h.f(objArr, "<this>");
        return new H2.c(0, AbstractC0711h.r(objArr));
    }

    public static final int q(byte[] bArr) {
        D2.h.f(bArr, "<this>");
        return bArr.length - 1;
    }

    public static int r(Object[] objArr) {
        D2.h.f(objArr, "<this>");
        return objArr.length - 1;
    }

    public static Object s(Object[] objArr, int i3) {
        D2.h.f(objArr, "<this>");
        if (i3 < 0 || i3 >= objArr.length) {
            return null;
        }
        return objArr[i3];
    }

    public static final int t(Object[] objArr, Object obj) {
        D2.h.f(objArr, "<this>");
        int i3 = 0;
        if (obj == null) {
            int length = objArr.length;
            while (i3 < length) {
                if (objArr[i3] == null) {
                    return i3;
                }
                i3++;
            }
            return -1;
        }
        int length2 = objArr.length;
        while (i3 < length2) {
            if (D2.h.b(obj, objArr[i3])) {
                return i3;
            }
            i3++;
        }
        return -1;
    }

    public static final Appendable u(int[] iArr, Appendable appendable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i3, CharSequence charSequence4, C2.l lVar) throws IOException {
        D2.h.f(iArr, "<this>");
        D2.h.f(appendable, "buffer");
        D2.h.f(charSequence, "separator");
        D2.h.f(charSequence2, "prefix");
        D2.h.f(charSequence3, "postfix");
        D2.h.f(charSequence4, "truncated");
        appendable.append(charSequence2);
        int i4 = 0;
        for (int i5 : iArr) {
            i4++;
            if (i4 > 1) {
                appendable.append(charSequence);
            }
            if (i3 >= 0 && i4 > i3) {
                break;
            }
            if (lVar != null) {
                appendable.append((CharSequence) lVar.d(Integer.valueOf(i5)));
            } else {
                appendable.append(String.valueOf(i5));
            }
        }
        if (i3 >= 0 && i4 > i3) {
            appendable.append(charSequence4);
        }
        appendable.append(charSequence3);
        return appendable;
    }

    public static final String v(int[] iArr, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i3, CharSequence charSequence4, C2.l lVar) {
        D2.h.f(iArr, "<this>");
        D2.h.f(charSequence, "separator");
        D2.h.f(charSequence2, "prefix");
        D2.h.f(charSequence3, "postfix");
        D2.h.f(charSequence4, "truncated");
        String string = ((StringBuilder) u(iArr, new StringBuilder(), charSequence, charSequence2, charSequence3, i3, charSequence4, lVar)).toString();
        D2.h.e(string, "toString(...)");
        return string;
    }

    public static /* synthetic */ String w(int[] iArr, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i3, CharSequence charSequence4, C2.l lVar, int i4, Object obj) {
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
        return v(iArr, charSequence, charSequence5, charSequence6, i5, charSequence7, lVar);
    }

    public static Object x(Object[] objArr) {
        D2.h.f(objArr, "<this>");
        if (objArr.length != 0) {
            return objArr[AbstractC0711h.r(objArr)];
        }
        throw new NoSuchElementException("Array is empty.");
    }

    public static Comparable y(Comparable[] comparableArr) {
        D2.h.f(comparableArr, "<this>");
        if (comparableArr.length == 0) {
            return null;
        }
        Comparable comparable = comparableArr[0];
        int iR = AbstractC0711h.r(comparableArr);
        int i3 = 1;
        if (1 <= iR) {
            while (true) {
                Comparable comparable2 = comparableArr[i3];
                if (comparable.compareTo(comparable2) < 0) {
                    comparable = comparable2;
                }
                if (i3 == iR) {
                    break;
                }
                i3++;
            }
        }
        return comparable;
    }

    public static char z(char[] cArr) {
        D2.h.f(cArr, "<this>");
        int length = cArr.length;
        if (length == 0) {
            throw new NoSuchElementException("Array is empty.");
        }
        if (length == 1) {
            return cArr[0];
        }
        throw new IllegalArgumentException("Array has more than one element.");
    }
}
