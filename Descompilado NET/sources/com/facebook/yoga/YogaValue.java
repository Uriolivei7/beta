package com.facebook.yoga;

/* JADX INFO: loaded from: classes.dex */
public class YogaValue {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static final YogaValue f8288c = new YogaValue(Float.NaN, w.UNDEFINED);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    static final YogaValue f8289d = new YogaValue(0.0f, w.POINT);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    static final YogaValue f8290e = new YogaValue(Float.NaN, w.AUTO);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final float f8291a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final w f8292b;

    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f8293a;

        static {
            int[] iArr = new int[w.values().length];
            f8293a = iArr;
            try {
                iArr[w.UNDEFINED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f8293a[w.POINT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f8293a[w.PERCENT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f8293a[w.AUTO.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public YogaValue(float f3, w wVar) {
        this.f8291a = f3;
        this.f8292b = wVar;
    }

    public static YogaValue a(String str) {
        if (str == null) {
            return null;
        }
        return "undefined".equals(str) ? f8288c : "auto".equals(str) ? f8290e : str.endsWith("%") ? new YogaValue(Float.parseFloat(str.substring(0, str.length() - 1)), w.PERCENT) : new YogaValue(Float.parseFloat(str), w.POINT);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof YogaValue)) {
            return false;
        }
        YogaValue yogaValue = (YogaValue) obj;
        w wVar = this.f8292b;
        if (wVar == yogaValue.f8292b) {
            return wVar == w.UNDEFINED || wVar == w.AUTO || Float.compare(this.f8291a, yogaValue.f8291a) == 0;
        }
        return false;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.f8291a) + this.f8292b.c();
    }

    public String toString() {
        int i3 = a.f8293a[this.f8292b.ordinal()];
        if (i3 == 1) {
            return "undefined";
        }
        if (i3 == 2) {
            return Float.toString(this.f8291a);
        }
        if (i3 != 3) {
            if (i3 == 4) {
                return "auto";
            }
            throw new IllegalStateException();
        }
        return this.f8291a + "%";
    }

    YogaValue(float f3, int i3) {
        this(f3, w.b(i3));
    }
}
