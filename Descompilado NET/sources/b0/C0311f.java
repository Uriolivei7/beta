package b0;

import java.lang.ref.SoftReference;

/* JADX INFO: renamed from: b0.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0311f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    SoftReference f5580a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    SoftReference f5581b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    SoftReference f5582c = null;

    public void a() {
        SoftReference softReference = this.f5580a;
        if (softReference != null) {
            softReference.clear();
            this.f5580a = null;
        }
        SoftReference softReference2 = this.f5581b;
        if (softReference2 != null) {
            softReference2.clear();
            this.f5581b = null;
        }
        SoftReference softReference3 = this.f5582c;
        if (softReference3 != null) {
            softReference3.clear();
            this.f5582c = null;
        }
    }

    public Object b() {
        SoftReference softReference = this.f5580a;
        if (softReference == null) {
            return null;
        }
        return softReference.get();
    }

    public void c(Object obj) {
        this.f5580a = new SoftReference(obj);
        this.f5581b = new SoftReference(obj);
        this.f5582c = new SoftReference(obj);
    }
}
