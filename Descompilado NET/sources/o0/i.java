package O0;

import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
public final class i extends g implements f {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Drawable f1459e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f1460f;

    public i(Drawable drawable) {
        this.f1459e = drawable;
    }

    @Override // O0.d
    public boolean b() {
        return this.f1460f;
    }

    @Override // O0.d
    public int b0() {
        return h() * d() * 4;
    }

    @Override // O0.d, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f1459e = null;
        this.f1460f = true;
    }

    @Override // O0.d, O0.l
    public int d() {
        Drawable drawable = this.f1459e;
        if (drawable != null) {
            Integer numValueOf = Integer.valueOf(drawable.getIntrinsicHeight());
            if (numValueOf.intValue() < 0) {
                numValueOf = null;
            }
            if (numValueOf != null) {
                return numValueOf.intValue();
            }
        }
        return 0;
    }

    @Override // O0.f
    public Drawable f0() {
        Drawable.ConstantState constantState;
        Drawable drawable = this.f1459e;
        if (drawable == null || (constantState = drawable.getConstantState()) == null) {
            return null;
        }
        return constantState.newDrawable();
    }

    @Override // O0.d, O0.l
    public int h() {
        Drawable drawable = this.f1459e;
        if (drawable != null) {
            Integer numValueOf = Integer.valueOf(drawable.getIntrinsicWidth());
            if (numValueOf.intValue() < 0) {
                numValueOf = null;
            }
            if (numValueOf != null) {
                return numValueOf.intValue();
            }
        }
        return 0;
    }
}
