package com.oblador.vectoricons;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;

/* JADX INFO: loaded from: classes.dex */
public class VectorIconsModule extends NativeRNVectorIconsSpec {
    VectorIconsModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @Override // com.oblador.vectoricons.NativeRNVectorIconsSpec
    public void getImageForFont(String str, String str2, double d4, double d5, Promise promise) {
        try {
            promise.resolve(a.a(str, str2, Integer.valueOf((int) d4), Integer.valueOf((int) d5), getReactApplicationContext()));
        } catch (Throwable th) {
            promise.reject("E_UNKNOWN_ERROR", th);
        }
    }

    @Override // com.oblador.vectoricons.NativeRNVectorIconsSpec
    public String getImageForFontSync(String str, String str2, double d4, double d5) {
        try {
            return a.a(str, str2, Integer.valueOf((int) d4), Integer.valueOf((int) d5), getReactApplicationContext());
        } catch (Throwable unused) {
            return null;
        }
    }

    @Override // com.oblador.vectoricons.NativeRNVectorIconsSpec, com.facebook.react.bridge.NativeModule
    public String getName() {
        return NativeRNVectorIconsSpec.NAME;
    }

    @Override // com.oblador.vectoricons.NativeRNVectorIconsSpec
    public void loadFontWithFileName(String str, String str2, Promise promise) {
        promise.reject("E_NOT_IMPLEMENTED");
    }
}
