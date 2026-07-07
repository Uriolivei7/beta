package com.facebook.yoga;

/* JADX INFO: loaded from: classes.dex */
public class t extends YogaNodeJNIBase {
    public t() {
    }

    protected void finalize() throws Throwable {
        try {
            r0();
        } finally {
            super.finalize();
        }
    }

    public void r0() {
        long j3 = this.f8284g;
        if (j3 != 0) {
            this.f8284g = 0L;
            YogaNative.jni_YGNodeFinalizeJNI(j3);
        }
    }

    public t(c cVar) {
        super(cVar);
    }
}
