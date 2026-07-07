package com.facebook.soloader;

import android.os.StrictMode;

/* JADX INFO: loaded from: classes.dex */
public class F extends E {
    @Override // com.facebook.soloader.E
    public String c() {
        return "SystemLoadWrapperSoSource";
    }

    @Override // com.facebook.soloader.E
    public int d(String str, int i3, StrictMode.ThreadPolicy threadPolicy) {
        try {
            System.loadLibrary(str.substring(3, str.length() - 3));
            return 1;
        } catch (Exception e4) {
            p.c("SoLoader", "Error loading library: " + str, e4);
            return 0;
        }
    }

    @Override // com.facebook.soloader.E
    public String toString() {
        return c() + "[" + SysUtil.getClassLoaderLdLoadLibrary() + "]";
    }
}
