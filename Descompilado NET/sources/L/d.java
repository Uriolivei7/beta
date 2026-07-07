package l;

/* JADX INFO: loaded from: classes.dex */
public class d implements Cloneable {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final Object f9625f = new Object();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f9626b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long[] f9627c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Object[] f9628d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f9629e;

    public d() {
        this(10);
    }

    private void d() {
        int i3 = this.f9629e;
        long[] jArr = this.f9627c;
        Object[] objArr = this.f9628d;
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            Object obj = objArr[i5];
            if (obj != f9625f) {
                if (i5 != i4) {
                    jArr[i4] = jArr[i5];
                    objArr[i4] = obj;
                    objArr[i5] = null;
                }
                i4++;
            }
        }
        this.f9626b = false;
        this.f9629e = i4;
    }

    public void b() {
        int i3 = this.f9629e;
        Object[] objArr = this.f9628d;
        for (int i4 = 0; i4 < i3; i4++) {
            objArr[i4] = null;
        }
        this.f9629e = 0;
        this.f9626b = false;
    }

    /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
    public d clone() {
        try {
            d dVar = (d) super.clone();
            dVar.f9627c = (long[]) this.f9627c.clone();
            dVar.f9628d = (Object[]) this.f9628d.clone();
            return dVar;
        } catch (CloneNotSupportedException e4) {
            throw new AssertionError(e4);
        }
    }

    public Object e(long j3) {
        return f(j3, null);
    }

    public Object f(long j3, Object obj) {
        Object obj2;
        int iB = c.b(this.f9627c, this.f9629e, j3);
        return (iB < 0 || (obj2 = this.f9628d[iB]) == f9625f) ? obj : obj2;
    }

    public long g(int i3) {
        if (this.f9626b) {
            d();
        }
        return this.f9627c[i3];
    }

    public void h(long j3, Object obj) {
        int iB = c.b(this.f9627c, this.f9629e, j3);
        if (iB >= 0) {
            this.f9628d[iB] = obj;
            return;
        }
        int i3 = ~iB;
        int i4 = this.f9629e;
        if (i3 < i4) {
            Object[] objArr = this.f9628d;
            if (objArr[i3] == f9625f) {
                this.f9627c[i3] = j3;
                objArr[i3] = obj;
                return;
            }
        }
        if (this.f9626b && i4 >= this.f9627c.length) {
            d();
            i3 = ~c.b(this.f9627c, this.f9629e, j3);
        }
        int i5 = this.f9629e;
        if (i5 >= this.f9627c.length) {
            int iF = c.f(i5 + 1);
            long[] jArr = new long[iF];
            Object[] objArr2 = new Object[iF];
            long[] jArr2 = this.f9627c;
            System.arraycopy(jArr2, 0, jArr, 0, jArr2.length);
            Object[] objArr3 = this.f9628d;
            System.arraycopy(objArr3, 0, objArr2, 0, objArr3.length);
            this.f9627c = jArr;
            this.f9628d = objArr2;
        }
        int i6 = this.f9629e;
        if (i6 - i3 != 0) {
            long[] jArr3 = this.f9627c;
            int i7 = i3 + 1;
            System.arraycopy(jArr3, i3, jArr3, i7, i6 - i3);
            Object[] objArr4 = this.f9628d;
            System.arraycopy(objArr4, i3, objArr4, i7, this.f9629e - i3);
        }
        this.f9627c[i3] = j3;
        this.f9628d[i3] = obj;
        this.f9629e++;
    }

    public void j(long j3) {
        int iB = c.b(this.f9627c, this.f9629e, j3);
        if (iB >= 0) {
            Object[] objArr = this.f9628d;
            Object obj = objArr[iB];
            Object obj2 = f9625f;
            if (obj != obj2) {
                objArr[iB] = obj2;
                this.f9626b = true;
            }
        }
    }

    public int k() {
        if (this.f9626b) {
            d();
        }
        return this.f9629e;
    }

    public Object l(int i3) {
        if (this.f9626b) {
            d();
        }
        return this.f9628d[i3];
    }

    public String toString() {
        if (k() <= 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.f9629e * 28);
        sb.append('{');
        for (int i3 = 0; i3 < this.f9629e; i3++) {
            if (i3 > 0) {
                sb.append(", ");
            }
            sb.append(g(i3));
            sb.append('=');
            Object objL = l(i3);
            if (objL != this) {
                sb.append(objL);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public d(int i3) {
        this.f9626b = false;
        if (i3 == 0) {
            this.f9627c = c.f9623b;
            this.f9628d = c.f9624c;
        } else {
            int iF = c.f(i3);
            this.f9627c = new long[iF];
            this.f9628d = new Object[iF];
        }
    }
}
