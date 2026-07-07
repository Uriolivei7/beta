package com.facebook.yoga;

/* JADX INFO: loaded from: classes.dex */
public class f extends e {
    public void c() {
        long j3 = this.f8305a;
        if (j3 != 0) {
            this.f8305a = 0L;
            YogaNative.jni_YGConfigFreeJNI(j3);
        }
    }

    protected void finalize() throws Throwable {
        try {
            c();
        } finally {
            super.finalize();
        }
    }
}
