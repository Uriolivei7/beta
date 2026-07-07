package com.oblador.vectoricons;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;

/* JADX INFO: loaded from: classes.dex */
public abstract class NativeRNVectorIconsSpec extends ReactContextBaseJavaModule implements TurboModule {
    public static final String NAME = "RNVectorIcons";

    public NativeRNVectorIconsSpec(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public abstract void getImageForFont(String str, String str2, double d4, double d5, Promise promise);

    @ReactMethod(isBlockingSynchronousMethod = true)
    public abstract String getImageForFontSync(String str, String str2, double d4, double d5);

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public abstract void loadFontWithFileName(String str, String str2, Promise promise);
}
