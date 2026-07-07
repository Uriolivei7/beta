package com.facebook.jni;

import a2.AbstractC0211a;
import com.facebook.jni.DestructorThread;

/* JADX INFO: loaded from: classes.dex */
public class HybridData {
    private final Destructor mDestructor = new Destructor(this);

    public static class Destructor extends DestructorThread.Destructor {
        private volatile long mNativePointer;

        Destructor(Object obj) {
            super(obj);
        }

        static native void deleteNative(long j3);

        @Override // com.facebook.jni.DestructorThread.Destructor
        protected final void destruct() {
            deleteNative(this.mNativePointer);
            this.mNativePointer = 0L;
        }
    }

    static {
        AbstractC0211a.d("fbjni");
    }

    public boolean isValid() {
        return this.mDestructor.mNativePointer != 0;
    }

    public synchronized void resetNative() {
        this.mDestructor.destruct();
    }
}
