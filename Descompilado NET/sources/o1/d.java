package O1;

/* JADX INFO: loaded from: classes.dex */
public abstract class d {
    public static final float a(float f3, float f4) {
        float fPow;
        if (f3 < Math.abs(f4)) {
            fPow = 1 + ((float) Math.pow((f3 / Math.abs(f4)) - r0, 3));
        } else {
            fPow = 1.0f;
        }
        return H2.d.b(f3 + (f4 * fPow), 0.0f);
    }
}
