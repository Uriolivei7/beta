package androidx.appcompat.widget;

/* JADX INFO: loaded from: classes.dex */
class Z {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f4047a = 0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f4048b = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f4049c = Integer.MIN_VALUE;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f4050d = Integer.MIN_VALUE;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f4051e = 0;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f4052f = 0;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f4053g = false;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f4054h = false;

    Z() {
    }

    public int a() {
        return this.f4053g ? this.f4047a : this.f4048b;
    }

    public int b() {
        return this.f4047a;
    }

    public int c() {
        return this.f4048b;
    }

    public int d() {
        return this.f4053g ? this.f4048b : this.f4047a;
    }

    public void e(int i3, int i4) {
        this.f4054h = false;
        if (i3 != Integer.MIN_VALUE) {
            this.f4051e = i3;
            this.f4047a = i3;
        }
        if (i4 != Integer.MIN_VALUE) {
            this.f4052f = i4;
            this.f4048b = i4;
        }
    }

    public void f(boolean z3) {
        if (z3 == this.f4053g) {
            return;
        }
        this.f4053g = z3;
        if (!this.f4054h) {
            this.f4047a = this.f4051e;
            this.f4048b = this.f4052f;
            return;
        }
        if (z3) {
            int i3 = this.f4050d;
            if (i3 == Integer.MIN_VALUE) {
                i3 = this.f4051e;
            }
            this.f4047a = i3;
            int i4 = this.f4049c;
            if (i4 == Integer.MIN_VALUE) {
                i4 = this.f4052f;
            }
            this.f4048b = i4;
            return;
        }
        int i5 = this.f4049c;
        if (i5 == Integer.MIN_VALUE) {
            i5 = this.f4051e;
        }
        this.f4047a = i5;
        int i6 = this.f4050d;
        if (i6 == Integer.MIN_VALUE) {
            i6 = this.f4052f;
        }
        this.f4048b = i6;
    }

    public void g(int i3, int i4) {
        this.f4049c = i3;
        this.f4050d = i4;
        this.f4054h = true;
        if (this.f4053g) {
            if (i4 != Integer.MIN_VALUE) {
                this.f4047a = i4;
            }
            if (i3 != Integer.MIN_VALUE) {
                this.f4048b = i3;
                return;
            }
            return;
        }
        if (i3 != Integer.MIN_VALUE) {
            this.f4047a = i3;
        }
        if (i4 != Integer.MIN_VALUE) {
            this.f4048b = i4;
        }
    }
}
