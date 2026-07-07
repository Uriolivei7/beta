package com.facebook.yoga;

/* JADX INFO: loaded from: classes.dex */
public abstract class e extends c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    long f8305a;

    private e(long j3) {
        if (j3 == 0) {
            throw new IllegalStateException("Failed to allocate native memory");
        }
        this.f8305a = j3;
    }

    @Override // com.facebook.yoga.c
    public void a(k kVar) {
        YogaNative.jni_YGConfigSetErrataJNI(this.f8305a, kVar.b());
    }

    @Override // com.facebook.yoga.c
    public void b(float f3) {
        YogaNative.jni_YGConfigSetPointScaleFactorJNI(this.f8305a, f3);
    }

    e() {
        this(YogaNative.jni_YGConfigNewJNI());
    }
}
