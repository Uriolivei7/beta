package q2;

/* JADX INFO: loaded from: classes.dex */
public class r {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float f10426a;

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final int f10427a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final int f10428b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final float f10429c;

        a(int i3, int i4, float f3) {
            this.f10427a = i3;
            this.f10428b = i4;
            this.f10429c = f3;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            a aVar = (a) obj;
            return this.f10427a == aVar.f10427a && this.f10428b == aVar.f10428b && Float.compare(aVar.f10429c, this.f10429c) == 0;
        }

        public int hashCode() {
            int i3 = ((this.f10427a * 31) + this.f10428b) * 31;
            float f3 = this.f10429c;
            return i3 + (f3 != 0.0f ? Float.floatToIntBits(f3) : 0);
        }

        public String toString() {
            return "Size{width=" + this.f10427a + ", height=" + this.f10428b + ", scaleFactor=" + this.f10429c + '}';
        }
    }

    public r(float f3) {
        this.f10426a = f3;
    }

    private int a(float f3) {
        return (int) Math.ceil(f3 / this.f10426a);
    }

    private int c(int i3) {
        int i4 = i3 % 64;
        return i4 == 0 ? i3 : (i3 - i4) + 64;
    }

    boolean b(int i3, int i4) {
        return a((float) i4) == 0 || a((float) i3) == 0;
    }

    a d(int i3, int i4) {
        float f3 = i3;
        int iC = c(a(f3));
        return new a(iC, (int) Math.ceil(i4 / r4), f3 / iC);
    }
}
