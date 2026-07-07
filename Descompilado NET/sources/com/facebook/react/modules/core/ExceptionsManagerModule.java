package com.facebook.react.modules.core;

import D2.h;
import com.facebook.fbreact.specs.NativeExceptionsManagerSpec;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.JavascriptException;
import k1.e;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = NativeExceptionsManagerSpec.NAME)
public class ExceptionsManagerModule extends NativeExceptionsManagerSpec {
    private final e devSupportManager;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ExceptionsManagerModule(e eVar) {
        super(null);
        h.f(eVar, "devSupportManager");
        this.devSupportManager = eVar;
    }

    @Override // com.facebook.fbreact.specs.NativeExceptionsManagerSpec
    public void dismissRedbox() {
        if (this.devSupportManager.n()) {
            this.devSupportManager.q();
        }
    }

    @Override // com.facebook.fbreact.specs.NativeExceptionsManagerSpec
    public void reportException(ReadableMap readableMap) {
        h.f(readableMap, "data");
        String string = readableMap.getString("message");
        if (string == null) {
            string = "";
        }
        ReadableArray array = readableMap.getArray("stack");
        if (array == null) {
            array = Arguments.createArray();
        }
        boolean z3 = readableMap.hasKey("isFatal") ? readableMap.getBoolean("isFatal") : false;
        String strA = T1.a.a(readableMap);
        if (z3) {
            h.c(array);
            JavascriptException javascriptException = new JavascriptException(T1.b.a(string, array));
            javascriptException.a(strA);
            throw javascriptException;
        }
        h.c(array);
        Y.a.m("ReactNative", T1.b.a(string, array));
        if (strA != null) {
            Y.a.c("ReactNative", "extraData: %s", strA);
        }
    }

    @Override // com.facebook.fbreact.specs.NativeExceptionsManagerSpec
    public void reportFatalException(String str, ReadableArray readableArray, double d4) {
        JavaOnlyMap javaOnlyMap = new JavaOnlyMap();
        javaOnlyMap.putString("message", str);
        javaOnlyMap.putArray("stack", readableArray);
        javaOnlyMap.putInt("id", (int) d4);
        javaOnlyMap.putBoolean("isFatal", true);
        reportException(javaOnlyMap);
    }

    @Override // com.facebook.fbreact.specs.NativeExceptionsManagerSpec
    public void reportSoftException(String str, ReadableArray readableArray, double d4) {
        JavaOnlyMap javaOnlyMap = new JavaOnlyMap();
        javaOnlyMap.putString("message", str);
        javaOnlyMap.putArray("stack", readableArray);
        javaOnlyMap.putInt("id", (int) d4);
        javaOnlyMap.putBoolean("isFatal", false);
        reportException(javaOnlyMap);
    }
}
