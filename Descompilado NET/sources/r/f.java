package R;

import X.k;
import android.net.Uri;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class f implements d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final List f1907a;

    public f(List<d> list) {
        this.f1907a = (List) k.g(list);
    }

    @Override // R.d
    public boolean a() {
        return false;
    }

    @Override // R.d
    public boolean b(Uri uri) {
        for (int i3 = 0; i3 < this.f1907a.size(); i3++) {
            if (((d) this.f1907a.get(i3)).b(uri)) {
                return true;
            }
        }
        return false;
    }

    @Override // R.d
    public String c() {
        return ((d) this.f1907a.get(0)).c();
    }

    public List d() {
        return this.f1907a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof f) {
            return this.f1907a.equals(((f) obj).f1907a);
        }
        return false;
    }

    public int hashCode() {
        return this.f1907a.hashCode();
    }

    public String toString() {
        return "MultiCacheKey:" + this.f1907a.toString();
    }
}
