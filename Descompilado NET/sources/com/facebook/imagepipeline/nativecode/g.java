package com.facebook.imagepipeline.nativecode;

import a2.AbstractC0211a;

/* JADX INFO: loaded from: classes.dex */
public class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static boolean f5954a;

    public static synchronized void a() {
        if (!f5954a) {
            AbstractC0211a.d("native-imagetranscoder");
            f5954a = true;
        }
    }
}
