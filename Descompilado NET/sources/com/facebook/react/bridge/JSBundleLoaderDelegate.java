package com.facebook.react.bridge;

import android.content.res.AssetManager;

/* JADX INFO: loaded from: classes.dex */
public interface JSBundleLoaderDelegate {
    void loadScriptFromAssets(AssetManager assetManager, String str, boolean z3);

    void loadScriptFromFile(String str, String str2, boolean z3);

    void loadSplitBundleFromFile(String str, String str2);

    void setSourceURLs(String str, String str2);
}
