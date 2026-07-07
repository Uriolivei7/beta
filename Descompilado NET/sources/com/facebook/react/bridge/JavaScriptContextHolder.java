package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public class JavaScriptContextHolder {
    private long mContext;

    public JavaScriptContextHolder(long j3) {
        this.mContext = j3;
    }

    public synchronized void clear() {
        this.mContext = 0L;
    }

    public long get() {
        return this.mContext;
    }
}
