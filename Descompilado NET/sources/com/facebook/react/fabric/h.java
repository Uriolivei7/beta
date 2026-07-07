package com.facebook.react.fabric;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f6835a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f6836b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f6837c;

    public h(int i3, int i4, String str) {
        D2.h.f(str, "eventName");
        this.f6835a = i3;
        this.f6836b = i4;
        this.f6837c = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof h)) {
            return false;
        }
        h hVar = (h) obj;
        return this.f6835a == hVar.f6835a && this.f6836b == hVar.f6836b && D2.h.b(this.f6837c, hVar.f6837c);
    }

    public int hashCode() {
        return (((Integer.hashCode(this.f6835a) * 31) + Integer.hashCode(this.f6836b)) * 31) + this.f6837c.hashCode();
    }

    public String toString() {
        return "SynchronousEvent(surfaceId=" + this.f6835a + ", viewTag=" + this.f6836b + ", eventName=" + this.f6837c + ")";
    }
}
