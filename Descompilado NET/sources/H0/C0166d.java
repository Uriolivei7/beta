package H0;

import java.util.LinkedHashSet;

/* JADX INFO: renamed from: H0.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0166d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f272a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final LinkedHashSet f273b;

    public C0166d(int i3) {
        this.f272a = i3;
        this.f273b = new LinkedHashSet(i3);
    }

    public final synchronized boolean a(Object obj) {
        try {
            if (this.f273b.size() == this.f272a) {
                LinkedHashSet linkedHashSet = this.f273b;
                linkedHashSet.remove(linkedHashSet.iterator().next());
            }
            this.f273b.remove(obj);
        } catch (Throwable th) {
            throw th;
        }
        return this.f273b.add(obj);
    }

    public final synchronized boolean b(Object obj) {
        return this.f273b.contains(obj);
    }
}
