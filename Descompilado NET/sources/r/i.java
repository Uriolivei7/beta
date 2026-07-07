package R;

import X.k;
import android.net.Uri;

/* JADX INFO: loaded from: classes.dex */
public class i implements d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final String f1910a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final boolean f1911b;

    public i(String str) {
        this(str, false);
    }

    @Override // R.d
    public boolean a() {
        return this.f1911b;
    }

    @Override // R.d
    public boolean b(Uri uri) {
        return this.f1910a.contains(uri.toString());
    }

    @Override // R.d
    public String c() {
        return this.f1910a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof i) {
            return this.f1910a.equals(((i) obj).f1910a);
        }
        return false;
    }

    public int hashCode() {
        return this.f1910a.hashCode();
    }

    public String toString() {
        return this.f1910a;
    }

    public i(String str, boolean z3) {
        this.f1910a = (String) k.g(str);
        this.f1911b = z3;
    }
}
