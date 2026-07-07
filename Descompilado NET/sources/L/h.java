package l;

/* JADX INFO: loaded from: classes.dex */
public class h implements Cloneable {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final Object f9660f = new Object();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f9661b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int[] f9662c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Object[] f9663d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f9664e;

    public h() {
        this(10);
    }

    private void f() {
        int i3 = this.f9664e;
        int[] iArr = this.f9662c;
        Object[] objArr = this.f9663d;
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            Object obj = objArr[i5];
            if (obj != f9660f) {
                if (i5 != i4) {
                    iArr[i4] = iArr[i5];
                    objArr[i4] = obj;
                    objArr[i5] = null;
                }
                i4++;
            }
        }
        this.f9661b = false;
        this.f9664e = i4;
    }

    public void b(int i3, Object obj) {
        int i4 = this.f9664e;
        if (i4 != 0 && i3 <= this.f9662c[i4 - 1]) {
            m(i3, obj);
            return;
        }
        if (this.f9661b && i4 >= this.f9662c.length) {
            f();
        }
        int i5 = this.f9664e;
        if (i5 >= this.f9662c.length) {
            int iE = c.e(i5 + 1);
            int[] iArr = new int[iE];
            Object[] objArr = new Object[iE];
            int[] iArr2 = this.f9662c;
            System.arraycopy(iArr2, 0, iArr, 0, iArr2.length);
            Object[] objArr2 = this.f9663d;
            System.arraycopy(objArr2, 0, objArr, 0, objArr2.length);
            this.f9662c = iArr;
            this.f9663d = objArr;
        }
        this.f9662c[i5] = i3;
        this.f9663d[i5] = obj;
        this.f9664e = i5 + 1;
    }

    public void c() {
        int i3 = this.f9664e;
        Object[] objArr = this.f9663d;
        for (int i4 = 0; i4 < i3; i4++) {
            objArr[i4] = null;
        }
        this.f9664e = 0;
        this.f9661b = false;
    }

    /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
    public h clone() {
        try {
            h hVar = (h) super.clone();
            hVar.f9662c = (int[]) this.f9662c.clone();
            hVar.f9663d = (Object[]) this.f9663d.clone();
            return hVar;
        } catch (CloneNotSupportedException e4) {
            throw new AssertionError(e4);
        }
    }

    public boolean e(int i3) {
        return j(i3) >= 0;
    }

    public Object g(int i3) {
        return h(i3, null);
    }

    public Object h(int i3, Object obj) {
        Object obj2;
        int iA = c.a(this.f9662c, this.f9664e, i3);
        return (iA < 0 || (obj2 = this.f9663d[iA]) == f9660f) ? obj : obj2;
    }

    public int j(int i3) {
        if (this.f9661b) {
            f();
        }
        return c.a(this.f9662c, this.f9664e, i3);
    }

    public int k(Object obj) {
        if (this.f9661b) {
            f();
        }
        for (int i3 = 0; i3 < this.f9664e; i3++) {
            if (this.f9663d[i3] == obj) {
                return i3;
            }
        }
        return -1;
    }

    public int l(int i3) {
        if (this.f9661b) {
            f();
        }
        return this.f9662c[i3];
    }

    public void m(int i3, Object obj) {
        int iA = c.a(this.f9662c, this.f9664e, i3);
        if (iA >= 0) {
            this.f9663d[iA] = obj;
            return;
        }
        int i4 = ~iA;
        int i5 = this.f9664e;
        if (i4 < i5) {
            Object[] objArr = this.f9663d;
            if (objArr[i4] == f9660f) {
                this.f9662c[i4] = i3;
                objArr[i4] = obj;
                return;
            }
        }
        if (this.f9661b && i5 >= this.f9662c.length) {
            f();
            i4 = ~c.a(this.f9662c, this.f9664e, i3);
        }
        int i6 = this.f9664e;
        if (i6 >= this.f9662c.length) {
            int iE = c.e(i6 + 1);
            int[] iArr = new int[iE];
            Object[] objArr2 = new Object[iE];
            int[] iArr2 = this.f9662c;
            System.arraycopy(iArr2, 0, iArr, 0, iArr2.length);
            Object[] objArr3 = this.f9663d;
            System.arraycopy(objArr3, 0, objArr2, 0, objArr3.length);
            this.f9662c = iArr;
            this.f9663d = objArr2;
        }
        int i7 = this.f9664e;
        if (i7 - i4 != 0) {
            int[] iArr3 = this.f9662c;
            int i8 = i4 + 1;
            System.arraycopy(iArr3, i4, iArr3, i8, i7 - i4);
            Object[] objArr4 = this.f9663d;
            System.arraycopy(objArr4, i4, objArr4, i8, this.f9664e - i4);
        }
        this.f9662c[i4] = i3;
        this.f9663d[i4] = obj;
        this.f9664e++;
    }

    public int n() {
        if (this.f9661b) {
            f();
        }
        return this.f9664e;
    }

    public Object p(int i3) {
        if (this.f9661b) {
            f();
        }
        return this.f9663d[i3];
    }

    public String toString() {
        if (n() <= 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.f9664e * 28);
        sb.append('{');
        for (int i3 = 0; i3 < this.f9664e; i3++) {
            if (i3 > 0) {
                sb.append(", ");
            }
            sb.append(l(i3));
            sb.append('=');
            Object objP = p(i3);
            if (objP != this) {
                sb.append(objP);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public h(int i3) {
        this.f9661b = false;
        if (i3 == 0) {
            this.f9662c = c.f9622a;
            this.f9663d = c.f9624c;
        } else {
            int iE = c.e(i3);
            this.f9662c = new int[iE];
            this.f9663d = new Object[iE];
        }
    }
}
