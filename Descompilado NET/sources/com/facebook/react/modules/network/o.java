package com.facebook.react.modules.network;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import java.net.SocketTimeoutException;
import r2.r;

/* JADX INFO: loaded from: classes.dex */
public final class o {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final o f7029a = new o();

    private o() {
    }

    public static final void a(ReactApplicationContext reactApplicationContext, int i3, WritableMap writableMap) {
        if (reactApplicationContext != null) {
            WritableArray writableArrayCreateArray = Arguments.createArray();
            writableArrayCreateArray.pushInt(i3);
            writableArrayCreateArray.pushMap(writableMap);
            r rVar = r.f10584a;
            reactApplicationContext.emitDeviceEvent("didReceiveNetworkData", writableArrayCreateArray);
        }
    }

    public static final void b(ReactApplicationContext reactApplicationContext, int i3, String str) {
        if (reactApplicationContext != null) {
            WritableArray writableArrayCreateArray = Arguments.createArray();
            writableArrayCreateArray.pushInt(i3);
            writableArrayCreateArray.pushString(str);
            r rVar = r.f10584a;
            reactApplicationContext.emitDeviceEvent("didReceiveNetworkData", writableArrayCreateArray);
        }
    }

    public static final void c(ReactApplicationContext reactApplicationContext, int i3, long j3, long j4) {
        if (reactApplicationContext != null) {
            WritableArray writableArrayCreateArray = Arguments.createArray();
            writableArrayCreateArray.pushInt(i3);
            writableArrayCreateArray.pushInt((int) j3);
            writableArrayCreateArray.pushInt((int) j4);
            r rVar = r.f10584a;
            reactApplicationContext.emitDeviceEvent("didReceiveNetworkDataProgress", writableArrayCreateArray);
        }
    }

    public static final void d(ReactApplicationContext reactApplicationContext, int i3, long j3, long j4) {
        if (reactApplicationContext != null) {
            WritableArray writableArrayCreateArray = Arguments.createArray();
            writableArrayCreateArray.pushInt(i3);
            writableArrayCreateArray.pushInt((int) j3);
            writableArrayCreateArray.pushInt((int) j4);
            r rVar = r.f10584a;
            reactApplicationContext.emitDeviceEvent("didSendNetworkData", writableArrayCreateArray);
        }
    }

    public static final void e(ReactApplicationContext reactApplicationContext, int i3, String str, long j3, long j4) {
        if (reactApplicationContext != null) {
            WritableArray writableArrayCreateArray = Arguments.createArray();
            writableArrayCreateArray.pushInt(i3);
            writableArrayCreateArray.pushString(str);
            writableArrayCreateArray.pushInt((int) j3);
            writableArrayCreateArray.pushInt((int) j4);
            r rVar = r.f10584a;
            reactApplicationContext.emitDeviceEvent("didReceiveNetworkIncrementalData", writableArrayCreateArray);
        }
    }

    public static final void f(ReactApplicationContext reactApplicationContext, int i3, String str, Throwable th) {
        if (reactApplicationContext != null) {
            WritableArray writableArrayCreateArray = Arguments.createArray();
            writableArrayCreateArray.pushInt(i3);
            writableArrayCreateArray.pushString(str);
            if (D2.h.b(th != null ? th.getClass() : null, SocketTimeoutException.class)) {
                writableArrayCreateArray.pushBoolean(true);
            }
            r rVar = r.f10584a;
            reactApplicationContext.emitDeviceEvent("didCompleteNetworkResponse", writableArrayCreateArray);
        }
    }

    public static final void g(ReactApplicationContext reactApplicationContext, int i3) {
        if (reactApplicationContext != null) {
            WritableArray writableArrayCreateArray = Arguments.createArray();
            writableArrayCreateArray.pushInt(i3);
            writableArrayCreateArray.pushNull();
            r rVar = r.f10584a;
            reactApplicationContext.emitDeviceEvent("didCompleteNetworkResponse", writableArrayCreateArray);
        }
    }

    public static final void h(ReactApplicationContext reactApplicationContext, int i3, int i4, WritableMap writableMap, String str) {
        if (reactApplicationContext != null) {
            WritableArray writableArrayCreateArray = Arguments.createArray();
            writableArrayCreateArray.pushInt(i3);
            writableArrayCreateArray.pushInt(i4);
            writableArrayCreateArray.pushMap(writableMap);
            writableArrayCreateArray.pushString(str);
            r rVar = r.f10584a;
            reactApplicationContext.emitDeviceEvent("didReceiveNetworkResponse", writableArrayCreateArray);
        }
    }
}
