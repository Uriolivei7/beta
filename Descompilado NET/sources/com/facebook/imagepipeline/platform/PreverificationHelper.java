package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public final class PreverificationHelper {
    public final boolean shouldUseHardwareBitmapConfig(Bitmap.Config config) {
        return config == Bitmap.Config.HARDWARE;
    }
}
