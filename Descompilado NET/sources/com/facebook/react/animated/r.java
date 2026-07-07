package com.facebook.react.animated;

import com.facebook.react.bridge.ReadableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class r extends e {

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public static final a f6459u = new a(null);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private long f6460e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f6461f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private double f6462g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private double f6463h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private double f6464i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private double f6465j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f6466k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final b f6467l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private double f6468m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private double f6469n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private double f6470o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private double f6471p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private double f6472q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f6473r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f6474s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private double f6475t;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    private static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private double f6476a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private double f6477b;

        public b() {
            this(0.0d, 0.0d, 3, null);
        }

        public final double a() {
            return this.f6476a;
        }

        public final double b() {
            return this.f6477b;
        }

        public final void c(double d4) {
            this.f6476a = d4;
        }

        public final void d(double d4) {
            this.f6477b = d4;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof b)) {
                return false;
            }
            b bVar = (b) obj;
            return Double.compare(this.f6476a, bVar.f6476a) == 0 && Double.compare(this.f6477b, bVar.f6477b) == 0;
        }

        public int hashCode() {
            return (Double.hashCode(this.f6476a) * 31) + Double.hashCode(this.f6477b);
        }

        public String toString() {
            return "PhysicsState(position=" + this.f6476a + ", velocity=" + this.f6477b + ")";
        }

        public b(double d4, double d5) {
            this.f6476a = d4;
            this.f6477b = d5;
        }

        public /* synthetic */ b(double d4, double d5, int i3, DefaultConstructorMarker defaultConstructorMarker) {
            this((i3 & 1) != 0 ? 0.0d : d4, (i3 & 2) != 0 ? 0.0d : d5);
        }
    }

    public r(ReadableMap readableMap) {
        D2.h.f(readableMap, "config");
        b bVar = new b(0.0d, 0.0d, 3, null);
        this.f6467l = bVar;
        bVar.d(readableMap.getDouble("initialVelocity"));
        a(readableMap);
    }

    private final void c(double d4) {
        double dSin;
        double dSin2;
        if (e()) {
            return;
        }
        this.f6472q += d4 <= 0.064d ? d4 : 0.064d;
        double d5 = this.f6463h;
        double d6 = this.f6464i;
        double d7 = this.f6462g;
        double d8 = -this.f6465j;
        double dSqrt = d5 / (((double) 2) * Math.sqrt(d7 * d6));
        double dSqrt2 = Math.sqrt(d7 / d6);
        double dSqrt3 = Math.sqrt(1.0d - (dSqrt * dSqrt)) * dSqrt2;
        double d9 = this.f6469n - this.f6468m;
        double d10 = this.f6472q;
        if (dSqrt < 1.0d) {
            double dExp = Math.exp((-dSqrt) * dSqrt2 * d10);
            double d11 = dSqrt * dSqrt2;
            double d12 = d8 + (d11 * d9);
            double d13 = d10 * dSqrt3;
            dSin2 = this.f6469n - ((((d12 / dSqrt3) * Math.sin(d13)) + (Math.cos(d13) * d9)) * dExp);
            dSin = ((d11 * dExp) * (((Math.sin(d13) * d12) / dSqrt3) + (Math.cos(d13) * d9))) - (((Math.cos(d13) * d12) - ((dSqrt3 * d9) * Math.sin(d13))) * dExp);
        } else {
            double dExp2 = Math.exp((-dSqrt2) * d10);
            double d14 = this.f6469n - (((((dSqrt2 * d9) + d8) * d10) + d9) * dExp2);
            dSin = dExp2 * ((d8 * ((d10 * dSqrt2) - ((double) 1))) + (d10 * d9 * dSqrt2 * dSqrt2));
            dSin2 = d14;
        }
        this.f6467l.c(dSin2);
        this.f6467l.d(dSin);
        if (e() || (this.f6466k && f())) {
            if (this.f6462g > 0.0d) {
                double d15 = this.f6469n;
                this.f6468m = d15;
                this.f6467l.c(d15);
            } else {
                double dA = this.f6467l.a();
                this.f6469n = dA;
                this.f6468m = dA;
            }
            this.f6467l.d(0.0d);
        }
    }

    private final double d(b bVar) {
        return Math.abs(this.f6469n - bVar.a());
    }

    private final boolean e() {
        return Math.abs(this.f6467l.b()) <= this.f6470o && (d(this.f6467l) <= this.f6471p || this.f6462g == 0.0d);
    }

    private final boolean f() {
        return this.f6462g > 0.0d && ((this.f6468m < this.f6469n && this.f6467l.a() > this.f6469n) || (this.f6468m > this.f6469n && this.f6467l.a() < this.f6469n));
    }

    @Override // com.facebook.react.animated.e
    public void a(ReadableMap readableMap) {
        D2.h.f(readableMap, "config");
        this.f6462g = readableMap.getDouble("stiffness");
        this.f6463h = readableMap.getDouble("damping");
        this.f6464i = readableMap.getDouble("mass");
        this.f6465j = this.f6467l.b();
        this.f6469n = readableMap.getDouble("toValue");
        this.f6470o = readableMap.getDouble("restSpeedThreshold");
        this.f6471p = readableMap.getDouble("restDisplacementThreshold");
        this.f6466k = readableMap.getBoolean("overshootClamping");
        int i3 = readableMap.hasKey("iterations") ? readableMap.getInt("iterations") : 1;
        this.f6473r = i3;
        this.f6382a = i3 == 0;
        this.f6474s = 0;
        this.f6472q = 0.0d;
        this.f6461f = false;
    }

    @Override // com.facebook.react.animated.e
    public void b(long j3) {
        w wVar = this.f6383b;
        if (wVar == null) {
            throw new IllegalArgumentException("Animated value should not be null");
        }
        long j4 = j3 / ((long) 1000000);
        if (!this.f6461f) {
            if (this.f6474s == 0) {
                this.f6475t = wVar.f6495f;
                this.f6474s = 1;
            }
            this.f6467l.c(wVar.f6495f);
            this.f6468m = this.f6467l.a();
            this.f6460e = j4;
            this.f6472q = 0.0d;
            this.f6461f = true;
        }
        c((j4 - this.f6460e) / 1000.0d);
        this.f6460e = j4;
        wVar.f6495f = this.f6467l.a();
        if (e()) {
            int i3 = this.f6473r;
            if (i3 != -1 && this.f6474s >= i3) {
                this.f6382a = true;
                return;
            }
            this.f6461f = false;
            wVar.f6495f = this.f6475t;
            this.f6474s++;
        }
    }
}
