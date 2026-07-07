package D2;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public abstract class i implements g, Serializable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f182b;

    public i(int i3) {
        this.f182b = i3;
    }

    public String toString() {
        String strD = s.d(this);
        h.e(strD, "renderLambdaToString(...)");
        return strD;
    }
}
