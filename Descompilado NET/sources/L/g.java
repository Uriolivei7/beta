package l;

import java.util.ConcurrentModificationException;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class g {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    static Object[] f9653e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    static int f9654f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    static Object[] f9655g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    static int f9656h;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    int[] f9657b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    Object[] f9658c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    int f9659d;

    public g() {
        this.f9657b = c.f9622a;
        this.f9658c = c.f9624c;
        this.f9659d = 0;
    }

    private void a(int i3) {
        if (i3 == 8) {
            synchronized (g.class) {
                try {
                    Object[] objArr = f9655g;
                    if (objArr != null) {
                        this.f9658c = objArr;
                        f9655g = (Object[]) objArr[0];
                        this.f9657b = (int[]) objArr[1];
                        objArr[1] = null;
                        objArr[0] = null;
                        f9656h--;
                        return;
                    }
                } finally {
                }
            }
        } else if (i3 == 4) {
            synchronized (g.class) {
                try {
                    Object[] objArr2 = f9653e;
                    if (objArr2 != null) {
                        this.f9658c = objArr2;
                        f9653e = (Object[]) objArr2[0];
                        this.f9657b = (int[]) objArr2[1];
                        objArr2[1] = null;
                        objArr2[0] = null;
                        f9654f--;
                        return;
                    }
                } finally {
                }
            }
        }
        this.f9657b = new int[i3];
        this.f9658c = new Object[i3 << 1];
    }

    private static int b(int[] iArr, int i3, int i4) {
        try {
            return c.a(iArr, i3, i4);
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new ConcurrentModificationException();
        }
    }

    private static void d(int[] iArr, Object[] objArr, int i3) {
        if (iArr.length == 8) {
            synchronized (g.class) {
                try {
                    if (f9656h < 10) {
                        objArr[0] = f9655g;
                        objArr[1] = iArr;
                        for (int i4 = (i3 << 1) - 1; i4 >= 2; i4--) {
                            objArr[i4] = null;
                        }
                        f9655g = objArr;
                        f9656h++;
                    }
                } finally {
                }
            }
            return;
        }
        if (iArr.length == 4) {
            synchronized (g.class) {
                try {
                    if (f9654f < 10) {
                        objArr[0] = f9653e;
                        objArr[1] = iArr;
                        for (int i5 = (i3 << 1) - 1; i5 >= 2; i5--) {
                            objArr[i5] = null;
                        }
                        f9653e = objArr;
                        f9654f++;
                    }
                } finally {
                }
            }
        }
    }

    public void c(int i3) {
        int i4 = this.f9659d;
        int[] iArr = this.f9657b;
        if (iArr.length < i3) {
            Object[] objArr = this.f9658c;
            a(i3);
            if (this.f9659d > 0) {
                System.arraycopy(iArr, 0, this.f9657b, 0, i4);
                System.arraycopy(objArr, 0, this.f9658c, 0, i4 << 1);
            }
            d(iArr, objArr, i4);
        }
        if (this.f9659d != i4) {
            throw new ConcurrentModificationException();
        }
    }

    public void clear() {
        int i3 = this.f9659d;
        if (i3 > 0) {
            int[] iArr = this.f9657b;
            Object[] objArr = this.f9658c;
            this.f9657b = c.f9622a;
            this.f9658c = c.f9624c;
            this.f9659d = 0;
            d(iArr, objArr, i3);
        }
        if (this.f9659d > 0) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean containsKey(Object obj) {
        return f(obj) >= 0;
    }

    public boolean containsValue(Object obj) {
        return h(obj) >= 0;
    }

    int e(Object obj, int i3) {
        int i4 = this.f9659d;
        if (i4 == 0) {
            return -1;
        }
        int iB = b(this.f9657b, i4, i3);
        if (iB < 0 || obj.equals(this.f9658c[iB << 1])) {
            return iB;
        }
        int i5 = iB + 1;
        while (i5 < i4 && this.f9657b[i5] == i3) {
            if (obj.equals(this.f9658c[i5 << 1])) {
                return i5;
            }
            i5++;
        }
        for (int i6 = iB - 1; i6 >= 0 && this.f9657b[i6] == i3; i6--) {
            if (obj.equals(this.f9658c[i6 << 1])) {
                return i6;
            }
        }
        return ~i5;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof g) {
            g gVar = (g) obj;
            if (size() != gVar.size()) {
                return false;
            }
            for (int i3 = 0; i3 < this.f9659d; i3++) {
                try {
                    Object objI = i(i3);
                    Object objM = m(i3);
                    Object obj2 = gVar.get(objI);
                    if (objM == null) {
                        if (obj2 != null || !gVar.containsKey(objI)) {
                            return false;
                        }
                    } else if (!objM.equals(obj2)) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused) {
                    return false;
                }
            }
            return true;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (size() != map.size()) {
                return false;
            }
            for (int i4 = 0; i4 < this.f9659d; i4++) {
                try {
                    Object objI2 = i(i4);
                    Object objM2 = m(i4);
                    Object obj3 = map.get(objI2);
                    if (objM2 == null) {
                        if (obj3 != null || !map.containsKey(objI2)) {
                            return false;
                        }
                    } else if (!objM2.equals(obj3)) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused2) {
                }
            }
            return true;
        }
        return false;
    }

    public int f(Object obj) {
        return obj == null ? g() : e(obj, obj.hashCode());
    }

    int g() {
        int i3 = this.f9659d;
        if (i3 == 0) {
            return -1;
        }
        int iB = b(this.f9657b, i3, 0);
        if (iB < 0 || this.f9658c[iB << 1] == null) {
            return iB;
        }
        int i4 = iB + 1;
        while (i4 < i3 && this.f9657b[i4] == 0) {
            if (this.f9658c[i4 << 1] == null) {
                return i4;
            }
            i4++;
        }
        for (int i5 = iB - 1; i5 >= 0 && this.f9657b[i5] == 0; i5--) {
            if (this.f9658c[i5 << 1] == null) {
                return i5;
            }
        }
        return ~i4;
    }

    public Object get(Object obj) {
        return getOrDefault(obj, null);
    }

    public Object getOrDefault(Object obj, Object obj2) {
        int iF = f(obj);
        return iF >= 0 ? this.f9658c[(iF << 1) + 1] : obj2;
    }

    int h(Object obj) {
        int i3 = this.f9659d * 2;
        Object[] objArr = this.f9658c;
        if (obj == null) {
            for (int i4 = 1; i4 < i3; i4 += 2) {
                if (objArr[i4] == null) {
                    return i4 >> 1;
                }
            }
            return -1;
        }
        for (int i5 = 1; i5 < i3; i5 += 2) {
            if (obj.equals(objArr[i5])) {
                return i5 >> 1;
            }
        }
        return -1;
    }

    public int hashCode() {
        int[] iArr = this.f9657b;
        Object[] objArr = this.f9658c;
        int i3 = this.f9659d;
        int i4 = 1;
        int i5 = 0;
        int iHashCode = 0;
        while (i5 < i3) {
            Object obj = objArr[i4];
            iHashCode += (obj == null ? 0 : obj.hashCode()) ^ iArr[i5];
            i5++;
            i4 += 2;
        }
        return iHashCode;
    }

    public Object i(int i3) {
        return this.f9658c[i3 << 1];
    }

    public boolean isEmpty() {
        return this.f9659d <= 0;
    }

    public void j(g gVar) {
        int i3 = gVar.f9659d;
        c(this.f9659d + i3);
        if (this.f9659d != 0) {
            for (int i4 = 0; i4 < i3; i4++) {
                put(gVar.i(i4), gVar.m(i4));
            }
        } else if (i3 > 0) {
            System.arraycopy(gVar.f9657b, 0, this.f9657b, 0, i3);
            System.arraycopy(gVar.f9658c, 0, this.f9658c, 0, i3 << 1);
            this.f9659d = i3;
        }
    }

    public Object k(int i3) {
        Object[] objArr = this.f9658c;
        int i4 = i3 << 1;
        Object obj = objArr[i4 + 1];
        int i5 = this.f9659d;
        int i6 = 0;
        if (i5 <= 1) {
            d(this.f9657b, objArr, i5);
            this.f9657b = c.f9622a;
            this.f9658c = c.f9624c;
        } else {
            int i7 = i5 - 1;
            int[] iArr = this.f9657b;
            if (iArr.length <= 8 || i5 >= iArr.length / 3) {
                if (i3 < i7) {
                    int i8 = i3 + 1;
                    int i9 = i7 - i3;
                    System.arraycopy(iArr, i8, iArr, i3, i9);
                    Object[] objArr2 = this.f9658c;
                    System.arraycopy(objArr2, i8 << 1, objArr2, i4, i9 << 1);
                }
                Object[] objArr3 = this.f9658c;
                int i10 = i7 << 1;
                objArr3[i10] = null;
                objArr3[i10 + 1] = null;
            } else {
                a(i5 > 8 ? i5 + (i5 >> 1) : 8);
                if (i5 != this.f9659d) {
                    throw new ConcurrentModificationException();
                }
                if (i3 > 0) {
                    System.arraycopy(iArr, 0, this.f9657b, 0, i3);
                    System.arraycopy(objArr, 0, this.f9658c, 0, i4);
                }
                if (i3 < i7) {
                    int i11 = i3 + 1;
                    int i12 = i7 - i3;
                    System.arraycopy(iArr, i11, this.f9657b, i3, i12);
                    System.arraycopy(objArr, i11 << 1, this.f9658c, i4, i12 << 1);
                }
            }
            i6 = i7;
        }
        if (i5 != this.f9659d) {
            throw new ConcurrentModificationException();
        }
        this.f9659d = i6;
        return obj;
    }

    public Object l(int i3, Object obj) {
        int i4 = (i3 << 1) + 1;
        Object[] objArr = this.f9658c;
        Object obj2 = objArr[i4];
        objArr[i4] = obj;
        return obj2;
    }

    public Object m(int i3) {
        return this.f9658c[(i3 << 1) + 1];
    }

    public Object put(Object obj, Object obj2) {
        int i3;
        int iE;
        int i4 = this.f9659d;
        if (obj == null) {
            iE = g();
            i3 = 0;
        } else {
            int iHashCode = obj.hashCode();
            i3 = iHashCode;
            iE = e(obj, iHashCode);
        }
        if (iE >= 0) {
            int i5 = (iE << 1) + 1;
            Object[] objArr = this.f9658c;
            Object obj3 = objArr[i5];
            objArr[i5] = obj2;
            return obj3;
        }
        int i6 = ~iE;
        int[] iArr = this.f9657b;
        if (i4 >= iArr.length) {
            int i7 = 8;
            if (i4 >= 8) {
                i7 = (i4 >> 1) + i4;
            } else if (i4 < 4) {
                i7 = 4;
            }
            Object[] objArr2 = this.f9658c;
            a(i7);
            if (i4 != this.f9659d) {
                throw new ConcurrentModificationException();
            }
            int[] iArr2 = this.f9657b;
            if (iArr2.length > 0) {
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                System.arraycopy(objArr2, 0, this.f9658c, 0, objArr2.length);
            }
            d(iArr, objArr2, i4);
        }
        if (i6 < i4) {
            int[] iArr3 = this.f9657b;
            int i8 = i6 + 1;
            System.arraycopy(iArr3, i6, iArr3, i8, i4 - i6);
            Object[] objArr3 = this.f9658c;
            System.arraycopy(objArr3, i6 << 1, objArr3, i8 << 1, (this.f9659d - i6) << 1);
        }
        int i9 = this.f9659d;
        if (i4 == i9) {
            int[] iArr4 = this.f9657b;
            if (i6 < iArr4.length) {
                iArr4[i6] = i3;
                Object[] objArr4 = this.f9658c;
                int i10 = i6 << 1;
                objArr4[i10] = obj;
                objArr4[i10 + 1] = obj2;
                this.f9659d = i9 + 1;
                return null;
            }
        }
        throw new ConcurrentModificationException();
    }

    public Object putIfAbsent(Object obj, Object obj2) {
        Object obj3 = get(obj);
        return obj3 == null ? put(obj, obj2) : obj3;
    }

    public Object remove(Object obj) {
        int iF = f(obj);
        if (iF >= 0) {
            return k(iF);
        }
        return null;
    }

    public Object replace(Object obj, Object obj2) {
        int iF = f(obj);
        if (iF >= 0) {
            return l(iF, obj2);
        }
        return null;
    }

    public int size() {
        return this.f9659d;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.f9659d * 28);
        sb.append('{');
        for (int i3 = 0; i3 < this.f9659d; i3++) {
            if (i3 > 0) {
                sb.append(", ");
            }
            Object objI = i(i3);
            if (objI != this) {
                sb.append(objI);
            } else {
                sb.append("(this Map)");
            }
            sb.append('=');
            Object objM = m(i3);
            if (objM != this) {
                sb.append(objM);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public boolean remove(Object obj, Object obj2) {
        int iF = f(obj);
        if (iF < 0) {
            return false;
        }
        Object objM = m(iF);
        if (obj2 != objM && (obj2 == null || !obj2.equals(objM))) {
            return false;
        }
        k(iF);
        return true;
    }

    public boolean replace(Object obj, Object obj2, Object obj3) {
        int iF = f(obj);
        if (iF < 0) {
            return false;
        }
        Object objM = m(iF);
        if (objM != obj2 && (obj2 == null || !obj2.equals(objM))) {
            return false;
        }
        l(iF, obj3);
        return true;
    }

    public g(int i3) {
        if (i3 == 0) {
            this.f9657b = c.f9622a;
            this.f9658c = c.f9624c;
        } else {
            a(i3);
        }
        this.f9659d = 0;
    }

    public g(g gVar) {
        this();
        if (gVar != null) {
            j(gVar);
        }
    }
}
